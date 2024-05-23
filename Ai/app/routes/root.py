from fastapi import HTTPException, APIRouter
import numpy as np
import pandas as pd
import category_encoders as ce
import json
from services.logs import get_logs
from services.data_preprocess_by_path import data_preprocessing_for_Conan
from services.data_preprocess_by_time import data_preprocessing_by_time
import os
import tensorflow as tf
from tensorflow.keras.models import load_model
from tqdm import tqdm
from multiprocessing import Pool
import time
import random
from routes.prompt import prompt_router


path_model = load_model('./ai_models/path_model.h5')
time_model = load_model('./ai_models/time_model.h5')


root_router = APIRouter()
root_router.include_router(prompt_router,  prefix="/prompt")


@root_router.get('/analytics/path')
async def analyze_by_Path(
    start_date: str,
    end_date: str
):
    # try:
        # 데이터 조회
        path_model.summary()
        logs = get_logs(start_date, end_date)
        # logs = temp_get_df()

        if len(logs) == 0:
            return {"congestion-info": []}

        # 정체 path 조회 - 데이터 전처리    
        preprocess_result = await data_preprocessing_for_Conan(logs)    
        deadlock_paths = preprocess_result["dataset"]    
        x_input = [path_matrix for (path_name, path_matrix) in deadlock_paths]

        if len(x_input) == 0:
            return {"congestion-info": []}
        
        analyse_results = path_model.predict(tf.convert_to_tensor(np.array(x_input, dtype=np.float32), dtype=tf.float32))
        results = []
        for analyse_result in analyse_results:
            result_obj = {}
            result_obj["etc-error-probability"] = float(analyse_result[0])
            result_obj["facility-error-probability"] = float(analyse_result[1])
            result_obj["oht-error-probability"] = float(analyse_result[2])
            results.append(result_obj)
        
        error_info = preprocess_result["error_info"]
        for idx, info in enumerate(error_info):
            info["analyse-result"] = results[idx]
    
        return {"congestion-info" : error_info}
        
    # except Exception as e:
    #     print(e)
    #     raise HTTPException(status_code=500, detail=str(e))
    

@root_router.get('/analytics/time')
async def analyze_by_Time(
    start_date: str,
    end_date: str
):
    try:
    #     데이터 조회
        # time_model.summary()
        random_number = random.randint(1, 10000)
        print(f"{random_number}번째 Time-AI Detection 요청 Parameter : {start_date} , {end_date}")

        # 시작 시간 기록
        start_time = time.time()

        logs = get_logs(start_date, end_date)
        print(f"{random_number} 요청 - get_logs 동작 시간: {round(time.time() - start_time)}초")
        if len(logs) == 0:
            return {"congestion-info": []}

        start_time = time.time()        
        deadlock_paths_and_error_info = await data_preprocessing_by_time(logs)        
        print(f"{random_number} 요청 - data_preprocessing_by_time 동작 시간: {round(time.time() - start_time)}초")

        if len(deadlock_paths_and_error_info["dataset"]) == 0:
            return {"congestion-info": []}
        start_time = time.time()        
        analyse_results = time_model.predict(tf.convert_to_tensor(np.array(deadlock_paths_and_error_info["dataset"], dtype=np.float32), dtype=tf.float32))
        results = []
        for analyse_result in analyse_results:
            result_obj = {}
            result_obj["etc-error-probability"] = float(analyse_result[0])
            result_obj["facility-error-probability"] = float(analyse_result[1])
            result_obj["oht-error-probability"] = float(analyse_result[2])
            results.append(result_obj)
        
        print(f"{random_number} 요청 - predict 동작 시간: {round(time.time() - start_time)}초")
        
        error_info = deadlock_paths_and_error_info["error_info"]

        for idx, info in enumerate(error_info):
            info["analyse-result"] = results[idx]
    
        return {"congestion-info" : error_info}
    except Exception as e:
        print(e)
        raise HTTPException(status_code=500, detail=str(e))
