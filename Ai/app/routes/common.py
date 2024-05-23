from openai import OpenAI
import config
import json
import time, re

client = OpenAI(api_key=config.API_KEY)

def send_request(instruction, content):
    assistant = client.beta.assistants.create(
            name="S106 Manager",
            instructions=instruction,
            tools=[{"type": "code_interpreter"}],
            model="gpt-4-turbo-preview"
        )

    # Thread 생성
    thread = client.beta.threads.create()

    client.beta.threads.messages.create(
    thread_id=thread.id,
    role="user",
    content=content
        )

    # run을 실행해야 create했던 message들이 API를 통해 chat gpt에게 질문
    run = client.beta.threads.runs.create(
    thread_id=thread.id,
    assistant_id=assistant.id
    )

    # 2초 마다 한번씩 API 응답이 return 되었는지 확인
    while True:
        run_check = client.beta.threads.runs.retrieve(
            thread_id=thread.id,
            run_id=run.id
        )
        if run_check.status not in ['queued','in_progress']:
            break
        else:
            time.sleep(2)
    
    response = client.beta.threads.messages.list(thread_id=thread.id).data[0].content[0].text.value
    response_sub = re.sub(r'^.*?({.*}).*$', r'\1', response, flags=re.DOTALL).strip()
    delete_connect(client, thread.id, assistant.id)
    return json.loads(response_sub)



def delete_connect(client, thread_id, assistant_id):
    client.beta.threads.delete(thread_id)
    client.beta.assistants.delete(assistant_id)