import numpy as np
import pandas as pd
import category_encoders as ce
from concurrent.futures import ProcessPoolExecutor
from config import all_nodes, all_paths, path_and_before_path_info, length_info, facility_length_info, facility_length_info_excel, df_cols
import os
from tqdm import tqdm


async def data_preprocessing_by_time(
    df: pd.DataFrame,
):
    try:
        

        # MinMaxScailing
        df["speed"] = df["speed"] / 5
        df["point_x"] = (df["point_x"] - 60) / (2040 - 60)
        df["point_y"] = (df["point_y"] - 320) / (560 - 320)
        df["curr_node_offset"] = df["curr_node_offset"] / 8.5

        if 'oht_connect' not in df.columns:
            df['oht_connect'] = df['error'].apply(lambda x: 1 if x == 200 else 0)

        df = df.reindex(columns=df_cols)

        encoder = ce.BinaryEncoder(cols=['node'])  # 임시 컬럼 인덱스 node 사용
        encoder.fit(pd.DataFrame(all_nodes, columns=['node']))

        # 인코딩할 데이터프레임의 컬럼 이름을 'node'로 변경하여 인코더에 맞춤
        encoded_target = encoder.transform(df[['target_node']].rename(columns={'target_node': 'node'}))
        encoded_next = encoder.transform(df[['next_node']].rename(columns={'next_node': 'node'}))
        encoded_current = encoder.transform(df[['current_node']].rename(columns={'current_node': 'node'}))

        encoder_status = ce.BinaryEncoder(cols=['status'])
        encoder_status.fit(pd.DataFrame(["I", "G", "W", "A"], columns=['status']))
        encoded_status = encoder_status.transform(df['status'])

        df['is_idle'] = df['status'].apply(lambda x: True if x == 'I' else False)
        # "oht_connect" 컬럼이 존재하지 않으면 추가
        



        # 인코딩된 결과를 원래의 데이터프레임에 새로운 컬럼으로 추가
        df = pd.concat([df, encoded_target.add_suffix('_target'), encoded_current.add_suffix('_current'),
                        encoded_status.add_suffix('_status'), encoded_next.add_suffix('_next')], axis=1)

        # dataset에서 Fail일때의 deadline 범위 추출

        FAILURE_DEADLINE = 120

        n_ohts = 30

        SNAPSHOT_MATRIX = [[df.iloc[k*n_ohts + idx] for idx in range(n_ohts)] for k in range(len(df) // n_ohts)]
        dataset = []
        error_info = []

        # 설비 앞 path 위에 몇대가 있는지의 정보를 담은 객체를 모아놓을 list
        path_count_arr = []
        second_arr = []
        prev_deadlock_second = 0
        congestion_idx = 0
        congestion_time = 0

        # 탐지 판단
        for now_second, ohts_arr in enumerate(SNAPSHOT_MATRIX):

            # 설비 앞 path 위에 몇대가 있는지를 저장할 객체
            path_count = {}

            for idx, oht in enumerate(ohts_arr):

                path_name = oht['path']

                # nan 처리
                if type(path_name) == float: continue

                # 설비 앞 path이면서 유휴상태가 아닌 경우, 속도가 0.7보다 느린 경우 count
                if path_name in facility_length_info.keys() and oht['is_idle'] == 0 and oht['speed'] <= 0.7:
                    path_count.setdefault(path_name, 0)
                    path_count[path_name] += 1

            flag = False
            for path_name, val in path_count.items():
                if flag: break

                # 길이에 따른 oht 최대 수용 개수
                count_criterion = 3 if length_info[path_name] >= 5 else 2

                # 정체 발생했을 경우 (설비 앞 path에 3개 혹은  2개 이상의 oht가 존재할 경우)
                if val >= count_criterion:

                    for idx, oht in enumerate(ohts_arr):
                        # 설비에서 일하고 있는 oht를 찾는다 ( status = W )
                        if oht['path'] == path_name and oht['current_node'] == \
                                facility_length_info_excel[facility_length_info_excel["path"] == path_name]['node'].iloc[
                                    0] and now_second - FAILURE_DEADLINE >= 0:

                            # 지난 10초간 count_criterion 이상이면 혼잡/정체라고 판단
                            is_deadlock = True
                            for temp_count in path_count_arr[now_second - 10:now_second]:
                                temp_count.setdefault(path_name, 0)
                                if temp_count.setdefault(path_name, 0) < count_criterion:
                                    is_deadlock = False

                            if is_deadlock:
                                if abs(prev_deadlock_second - now_second) <= 1:
                                    congestion_time+=1
                                    prev_deadlock_second = now_second
                                    continue
                                elif congestion_time > 10:
                                        error_info[congestion_idx]["congestion-time"] = congestion_time
                                        congestion_time = 10
                                        congestion_idx+=1

                                prev_deadlock_second = now_second
                                second_arr.append(now_second)
                                # dataset.append(SNAPSHOT_MATRIX[now_second-FAILURE_DEADLINE:now_second])
                                
                                info_dict = {
                                    "oht-id": int(oht["oht_id"]),
                                    "time": int(now_second),
                                    "path": path_name,
                                    "error-code": int(oht['error']),
                                    "status": oht["status"],
                                    "carrier": bool(oht["carrier"]),
                                    "error": int(oht["error"]),
                                    "speed": float(oht["speed"]),
                                    "is-fail": bool(oht["is_fail"]),
                                    "current-node": oht["current_node"],
                                    "next-node": oht["next_node"],
                                    "congestion-time": int(10)
                                    }

                                error_info.append(info_dict)
                                
                                flag = True
                                break

            path_count_arr.append(path_count)

        # 필요없는 컬럼 drop하고 dataset 구성
        df.drop(['oht_id', 'path', 'target_node', 'current_node', 'curr_time', 'status', 'mode', 'start_time', 'error', 'next_node'], axis=1,
                inplace=True)
        
        NEW_SNAPSHOT_MATRIX = [[df.iloc[k*n_ohts + idx] for idx in range(n_ohts)] for k in range(len(df) // n_ohts)]
        # dataset 추가
        for now_second, ohts_arr in enumerate(NEW_SNAPSHOT_MATRIX):

            # NEW_SNAPSHOT_MATRIX: 매 초 상황 행렬
            # second_arr : 혼잡/정체가 발생한 시간 초가 담겨있는 arr
            # FAILURE_DEADLINE: 레이아웃 상에서의 최장경로 소요시간

            if now_second in second_arr:
                dataset.append(NEW_SNAPSHOT_MATRIX[now_second - FAILURE_DEADLINE:now_second])

        return {"dataset": dataset, "error_info": error_info}
    except Exception as e:
        print(e)
        raise e(status_code=500, detail=str(e))
 
