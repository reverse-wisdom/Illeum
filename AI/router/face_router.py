from fastapi import APIRouter, File, UploadFile, HTTPException

import base64
import cv2
import numpy as np
import services.face_service as service

router = APIRouter()


# 해당 함수는 swagger 상에서 사진을 넣었을 때에 결과가 잘 나오는지 체크하기 위한 모듈, 실제는 base64로 인코딩된 이미지를 넣을 예정
@router.post("/test_eval", summary="테스트용 - 현재 태도에 따른 태도 분석")
async def evaluate_face_background_task_test(uid: int, rid: int, snapshot: UploadFile = File(...)):
    # 이미지를 base64로 컨버팅
    content = await snapshot.read()
    nparr = np.frombuffer(content, np.int8)
    img_np = cv2.imencode(nparr, cv2.IMREAD_COLOR)
    # 얼굴 인식 모듈 사용
    # face_service...
    b64_string = base64.b64encode(img_np[1]).decode('utf-8')
    return service.face_detection_task(uid, rid, b64_string)


# 실제 사용
@router.post("/eval", summary="현재 태도에 따른 태도 분석")
async def evaluate_face_background_task(uid: int, rid: int, snapshot: str):
    return service.face_detection_task(uid, rid, snapshot)


if __name__ == "__main__":
    img = cv2.imread(r"..\some_folder\person.png", cv2.IMREAD_COLOR)
    jpg_img = cv2.imencode('.jpg', img)
    b64_string = base64.b64encode(jpg_img[1]).decode('utf-8')
    import asyncio

    loop = asyncio.get_event_loop()
    result = loop.run_until_complete(evaluate_face_background_task(2, 2, b64_string))
    print("Test result.")
    print(result)
    pass
