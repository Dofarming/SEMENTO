
from pydantic import BaseModel, Field
from datetime import datetime
from typing import List


# Pydantic 모델 정의
class DetectionResult(BaseModel):
    no: int = Field(..., description="정체탐지번호(임시)")
    path: str = Field(..., description="정체가 발생한 경로")
    cause: str = Field(..., description="에러 원인(F, O, E)")
    accuracy: float = Field(..., description="정확도")
    cause_oht: int = Field(..., description="에러 젤 앞 OHT")
    start_date: datetime = Field(..., description="에러 시작 시간")
    end_date: datetime = Field(..., description="에러 종료 시간")
    current_node: str = Field(..., description="현재 노드")
    next_node: str = Field(..., description="다음 노드")
    speed: float = Field(..., description="속도")
    carrier: bool = Field(..., description="캐리어 여부")
    status: str = Field(..., description="상태 ('W'- working, 'A' - Arrived, 'G' - going, 'I' - Idle)")
    oht_id: List[int] = Field(..., description="정체에 포함된 OHT_ID")