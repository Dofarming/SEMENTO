from fastapi import APIRouter, HTTPException
from sse_starlette import EventSourceResponse
from routes.common import client
from models.pydantic_model import DetectionResult
import asyncio
import json

prompt_router = APIRouter()

@prompt_router.post("/")
def recipe_stream(
    detection_result: DetectionResult
):
    return get_stream(detection_result)


def get_stream(detection_result):
    instruction = """
                무조건 한글로 대답해.

                주어진 결과를 기반으로 OHT의 상태를 진단하고 간단한 제안을 제공하는 포맷을 작성해주세요. 다음과 같은 형식으로 결과를 제공해드릴테니 분석하여 답변해주세요:

                결과 예시:
                {
                    detection-result : 
                            {
                                no : int, //정체탐지번호(임시)
                                path : String, //정체가 발생한 경로
                                cause : String, //에러원인(F,O,E)
                                accuracy : double, //정확도
                                cause-oht : Long //에러 젤앞 OHT
                                start-date : Date, //에러 시작시간
                                end-date : Date, //에러 종료시간
                                current-node: String,
                                next-node: String,
                                speed: float,
                                carrier: boolean,
                                status: String // 
                                    "W"이면 작업중, 
                                    "A"이면 도착 상태,
                                    "G"이면 이동 중, 
                                    "I"이면 유휴 상태
                                oht-id : [int, int], //정체에 포함된 OHT_ID
                            }
                        
                }

                답변 포맷:

                OHT {cause-oht} ({path}):
                현재 상태: "{status}" 상태, 속도 {speed}m/s, 캐리어 있음/없음
                분석 결과: 시설 에러 확률이 {facility-error-probability}로 매우 높습니다.
                진단: 시설 문제로 인해 대기 중인 것으로 보입니다.
                제안: 해당 경로의 시설 점검을 진행해 보세요.
                이를 기반으로, 주어진 데이터를 분석하여 다음과 같이 답변을 생성하세요.
                """

    assistant = client.beta.assistants.create(
            name="S106 Manager",
            instructions=instruction,
            tools=[{"type": "code_interpreter"}],
            model="gpt-4o"
        )


    try:
        # Thread 생성
        thread = client.beta.threads.create()
        prompt = json.dumps(detection_result.json())

        message = client.beta.threads.messages.create(
            thread_id=thread.id,
            role="user",
            content=prompt
        )

        run = client.beta.threads.runs.create_and_poll(
            thread_id=thread.id,
            assistant_id=assistant.id,
            instructions=instruction
        )

        if run.status == 'completed': 
            messages = client.beta.threads.messages.list(
                thread_id=thread.id
            )

            response = {
                "solution" : messages.data[0].content[0].text.value
            }
            return response
        else:
            print(run.status)
        
        client.beta.assistants.delete(assistant.id)
    except Exception as e:
        print(e)
