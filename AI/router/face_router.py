from fastapi import APIRouter, File, UploadFile, HTTPException

# from crud import face_crud
from services import face_service

from database.db import session

import uuid
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
    service.face_detection_task(uid, rid, b64_string)
    pass


# 실제 사용
@router.post("/eval", summary="현재 태도에 따른 태도 분석")
async def evaluate_face_background_task(uid: int, rid: int, snapshot: str):
    # 이미지 저장
    content = await snapshot.read()
    with open(f'../snapshot/{uuid.uuid4()}', 'wb') as fp:
        fp.write(content)

    # 얼굴 인식 모듈 사용
    # face_service...
    pass
