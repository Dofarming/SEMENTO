import numpy as np
import pandas as pd
import category_encoders as ce
from concurrent.futures import ProcessPoolExecutor
from config import all_nodes, all_paths, path_and_before_path_info, length_info, facility_length_info, CONGESTION_CRITERION_TIME, FAILURE_DEADLINE, N_OHTS
import os
from tqdm import tqdm


async def data_preprocessing_for_Conan(
    df: pd.DataFrame,
):
    # try:
        
        # MinMaxScailing
        df["speed"] = df["speed"] / 5
        df["point_x"] = (df["point_x"] - 60) / (2040 - 60)
        df["point_y"] = (df["point_y"] - 320) / (560 - 320)
        df["curr_node_offset"] = df["curr_node_offset"] / 8.5

        # "oht_connect" 컬럼이 존재하지 않으면 추가
        if 'oht_connect' not in df.columns:
            df['oht_connect'] = df['error'].apply(lambda x: 1 if x == 200 else 0)

        encoder = ce.BinaryEncoder(cols=['node'])  # 임시 컬럼 인덱스 node 사용
        encoder.fit(pd.DataFrame(all_nodes, columns=['node']))

        # 인코딩할 데이터프레임의 컬럼 이름을 'node'로 변경하여 인코더에 맞춤
        encoded_target = encoder.transform(df[['target_node']].rename(columns={'target_node': 'node'}))
        encoded_next = encoder.transform(df[['next_node']].rename(columns={'next_node': 'node'}))
        encoded_current = encoder.transform(df[['current_node']].rename(columns={'current_node': 'node'}))

        encoder_status = ce.BinaryEncoder(cols=['status'])
        encoded_status = encoder_status.fit_transform(df['status'])

        df['is_idle'] = df['status'].apply(lambda x: True if x == 'I' else False)

        # 인코딩된 결과를 원래의 데이터프레임에 새로운 컬럼으로 추가
        # dataset에서 Fail일때의 deadline 범위 추출
        df = pd.concat([df, encoded_target.add_suffix('_target'), encoded_current.add_suffix('_current'),
                             encoded_status.add_suffix('_status'), encoded_next.add_suffix('_next')], axis=1)

        SNAPSHOT_MATRIX = [[df.iloc[k*N_OHTS + idx] for idx in range(N_OHTS)] for k in range(len(df) // N_OHTS)]

        # 노드 앞 path 위에 몇대가 있는지의 정보를 담은 객체를 모아놓을 list
        path_count_arr = []
        path_and_second_arr = []
        path_visited = {}
        oht_visited = {}
        test = []
        error_info = []
        error_dict = {}
        oht_error_new_dict = {}
        path_dict_new_dict = {}

        for path_info in path_and_before_path_info.iloc[:, 0]:
            path_visited[path_info] = False

        # oht_visited 초기화
        for oht_id in df["oht_id"].unique():
            oht_visited[oht_id] = False


        print("탐지 판단 시작")

        # 탐지 판단
        for now_second, ohts_arr in enumerate(SNAPSHOT_MATRIX):

            # 노드 앞 path 위에 몇대가 있는지를 저장할 객체
            path_count = {}
            
            for oht in ohts_arr:
                path_name = oht['path']
                path_count.setdefault(path_name, 0)
                if oht["is_idle"] == 1: continue
                # nan 처리
                if type(path_name) == float: continue

                # 유휴상태가 아닌 경우, 속도가 0.7보다 느린 경우 count
                if path_name in length_info.keys() and oht['is_idle'] == 0 and oht['speed'] <= 0.7:
                    path_count[path_name]+=1

            #path 정체가 풀렸는지 확인
            for oht in ohts_arr:
                if oht["is_idle"] == 1: continue
                path_name = oht['path']
                if path_name in length_info and path_name in path_count:
                    temp_count_criterion = 3 if length_info[path_name] >= 5 else 2
                    if path_count[path_name] < temp_count_criterion and path_visited[path_name] and not oht_visited[oht["oht_id"]]:
                        path_visited[path_name] = False
                        try:
                            error_info.append(path_dict_new_dict[path_name])
                        except Exception as e:
                            if path_name in oht_error_new_dict:
                                error_info.append(oht_error_new_dict[path_name])

                
            for idx, oht in enumerate(ohts_arr):
                oht_id = oht["oht_id"]
                path_name = oht["path"]
                if oht["is_idle"] == 1: continue
                # oht 에러인 경우
                if oht['error'] == 200:
                    is_deadlock_oht_error = False

                    for idx, other_oht in enumerate(ohts_arr):
                        other_offset = other_oht["curr_node_offset"]
                        other_path = other_oht["path"]

                        if other_oht["oht_id"] == oht_id: continue
                        elif other_path == path_name and other_offset != 0 and other_offset < oht["curr_node_offset"]:
                            # 고장난 oht 뒤에 있는 다른 oht의 속도가 0이면 정체
                            is_deadlock_oht_error = True
                            break
                        elif other_path in path_and_before_path_info[path_and_before_path_info["before_node"] == oht["current_node"]]["path"].values \
                                                and other_offset != 0 and other_oht["status"] != "W" and other_oht["speed"] == 0:
                            # 뒷길확인 로직 - oht에러가 난 호기의 이전 path에 존재하는 경우
                            is_deadlock_oht_error = True
                            break

                    if is_deadlock_oht_error:
                        if oht_visited[oht["oht_id"]]:
                            oht_error_new_dict[oht["oht_id"]]["congestion-time"] += 1
                        else:
                            oht_visited[oht_id] = True
                            path_visited[path_name] = True
                            path_and_second_arr.append((now_second, path_name))
                            test.append()
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
                                            "target-node": oht["target_node"],
                                            "curr_node_offset": oht["curr_node_offset"],
                                            "congestion-time": int(1)
                            }
                            test.append(info_dict)
                            oht_error_new_dict[oht["oht_id"]] = info_dict
                        
                # 설비 길이만 참고하는 정체 로직 짜기 - facility_length_info
                elif path_name in facility_length_info and not path_visited[path_name]:

                    if oht_visited[oht_id]: 
                        error_info.append(oht_error_new_dict[oht["oht_id"]])
                        oht_visited[oht_id] = False
                    # 길이에 따른 oht 최대 수용 개수
                    count_criterion = 3 if facility_length_info[path_name] >= 5 else 2

                    # 정체 발생했을 경우 (노드 앞 path에 3개 혹은  2개 이상의 oht가 존재할 경우)
                    if path_count[path_name] >= count_criterion and now_second-FAILURE_DEADLINE >= 0:
                        # 지난 20초간 count_criterion 이상이면 혼잡/정체라고 판단
                        is_facility_deadlock = True
                        for temp_count in path_count_arr[now_second-CONGESTION_CRITERION_TIME:now_second]:
                            if temp_count.setdefault(path_name, 0) < count_criterion:
                                is_facility_deadlock = False

                        if is_facility_deadlock:
                            path_visited[path_name] = True
                            path_and_second_arr.append((now_second, path_name))
                            test.append(info_dict)
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
                                    "target-node": oht["target_node"],
                                    "curr_node_offset": oht["curr_node_offset"],
                                    "congestion-time": int(CONGESTION_CRITERION_TIME)
                                }

                            path_dict_new_dict[path_name] = info_dict

                elif path_name in length_info and not path_visited[path_name]:
                    if oht_visited[oht_id]: 
                        error_info.append(oht_error_new_dict[oht["oht_id"]])
                        oht_visited[oht_id] = False
                    
                     # 길이에 따른 oht 최대 수용 개수
                    count_criterion = 3 if length_info[path_name] >= 5 else 2

                    # 정체 발생했을 경우 (노드 앞 path에 3개 혹은  2개 이상의 oht가 존재할 경우)
                    if path_count[path_name] >= count_criterion and now_second-FAILURE_DEADLINE >= 0:
                        # 지난 20초간 count_criterion 이상이면 혼잡/정체라고 판단
                        is_deadlock = True
                        for temp_count in path_count_arr[now_second-20:now_second]:
                            if temp_count.setdefault(path_name, 0) < count_criterion:
                                is_deadlock = False

                        if is_deadlock:
                            path_visited[path_name] = True
                            path_and_second_arr.append((now_second, path_name))

                else:
                    
                    if oht_visited[oht_id]:
                        error_info.append(oht_error_new_dict[oht["oht_id"]])
                        oht_visited[oht_id] = False
                        path_visited[path_name] = False
                    elif type(path_name) == str and path_name is not None and path_visited[path_name]:
                        try:
                            path_dict_new_dict[path_name]["congestion-time"] += 1
                        except Exception as e:
                            if path_name in oht_error_new_dict:
                                oht_error_new_dict[path_name]["congestion-time"] += 1

            path_count_arr.append(path_count)

        DROP_LENGTH = len(df.drop(['oht_id', 'mode', 'status', 'current_node', 'next_node', 'target_node', 'curr_time', 'start_time', 'path', 'error'], axis=1).columns)
        
        print("make path data")
        # 프로세스 풀 생성 및 멀티프로세싱 실행
        SNAPSHOT_MATRIX = [[df.iloc[k*N_OHTS + idx] for idx in range(N_OHTS)] for k in range(len(df) // N_OHTS)]
        dataset = list(tqdm(make_matrix_by_path(df, path, moment_time, N_OHTS, DROP_LENGTH, SNAPSHOT_MATRIX) for (moment_time, path) in path_and_second_arr))

        return {"dataset": dataset, "error_info": error_info, "test":test}
    # except Exception as e:
    #     print(str(e) + "@")
    #     raise e(status_code=500, detail=str(e))


def process_path_data(args):
    df, path, moment_time, N_OHTS, DROP_LENGTH, NEW_SNAPSHOT_MATRIX = args

    return make_matrix_by_path(df, path, moment_time, N_OHTS, DROP_LENGTH, NEW_SNAPSHOT_MATRIX)

def make_matrix_by_path(df, path_name, moment_time, N_OHTS, DROP_LENGTH, NEW_SNAPSHOT_MATRIX):
    
    path_matrix = []
    oht_arr = NEW_SNAPSHOT_MATRIX[moment_time]
    
    working_oht = None
    on_facility_oht = None
    ohts = []
    for oht in oht_arr:
        if oht["path"] == path_name:
            if oht["status"] == "W":
                working_oht = oht
            elif oht["status"] != "I" and oht["curr_node_offset"] > 0:
                ohts.append(oht)
    
    # 'curr_node_offset'의 값을 기준으로 내림차순 정렬
    sorted_ohts = sorted(ohts, key=lambda x: x['curr_node_offset'], reverse=True)
    if working_oht is not None: 
        sorted_ohts.insert(0, working_oht)

    # 혼잡 / 정체 발생 시점으로부터 DEADLINE만큼의 시간동안
    for second in range(moment_time-FAILURE_DEADLINE+1, moment_time):
        temp_path_data = [None] * 4 
        # 혼잡 / 정체 발생 시점에 설비 path에 존재했던 oht들을 추적
        for i in range(4):
            if i >= len(sorted_ohts):
                temp_path_data[i] = [0] * DROP_LENGTH
                continue

            oht = sorted_ohts[i]
            
            if oht is None:
                temp_path_data[i] = [0] * DROP_LENGTH
                continue
            
            for temp_oht in NEW_SNAPSHOT_MATRIX[second]:
                if temp_oht["oht_id"] == oht["oht_id"]:
                    temp = temp_oht.drop(['oht_id', 'mode', 'status', 'current_node', 'next_node', 'target_node', 'curr_time', 'start_time', 'path', 'error'])
                    temp_path_data[i] = temp
        
        path_matrix.append(temp_path_data)
    
    while len(sorted_ohts) < 4:
        sorted_ohts.append([0] * DROP_LENGTH)

    last_path = []
    for oht in sorted_ohts:
        if type(oht) == pd.Series:
            last_path.append(oht.drop(['oht_id', 'mode', 'status', 'current_node', 'next_node', 'target_node', 'curr_time', 'start_time', 'path', 'error']))
        elif oht is None:
            last_path.append([0] * DROP_LENGTH)
        else:
            last_path.append(oht)
    
    path_matrix.append(last_path)

    return (path_name, path_matrix)


def temp_get_df():
    directory = './xlsx/test'

        # 디렉토리 내 모든 파일 리스트 가져오기
    files = os.listdir(directory)

        # 'facility_error'로 시작하는 CSV 파일들만 필터링
    csv_files = [file for file in files if file.endswith('.csv')]
        #     print(len(csv_files))
        # 데이터프레임 리스트 초기화
    dataframes = []

        # 필터링된 파일들을 읽어서 데이터프레임 리스트에 추가
    for file in csv_files:
        file_path = os.path.join(directory, file)
        try:
            df = pd.read_csv(file_path)
        except:
            print("?")
        dataframes.append(df)
    return pd.concat(dataframes, axis=0)