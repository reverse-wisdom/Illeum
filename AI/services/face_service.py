# 모듈을 gitpython 등을 사용해서 다운로드 후, import 연결.
import os
import sys
from typing import Optional

import git
import cv2
import numpy as np

import crud.evalutation_repository as repository

BASE_REPO_DIR = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), "OpenVtuber")

if not os.path.isdir(BASE_REPO_DIR):
    print("OpenVtuber 프로젝트 클론 중", end=' ')
    git.Repo.clone_from("https://github.com/getCurrentThread/OpenVtuber",
                        BASE_REPO_DIR)
    print("done")

sys.path.insert(1, BASE_REPO_DIR)
from OpenVtuber.SimpleFaceModule import SimpleFaceModule, SimpleFaceDetectionResult
import models.FaceDetectionResponse as FDR
from models.FaceDetectionResponse import FaceDetectionResponse as Response
from utils.image_util import data_uri_to_cv2_img



# 서비스 코드 시작 ##
module = SimpleFaceModule()


# AFK : 인식 실패의 경우 *TODO: 조건을 강하게 체크할 필요가 있음
# ASLEEP : blink 값이 0.1 보다 작아지면
# DISTRACTED : 얼굴과 눈 각도가 크게 뒤틀릴 때
# ATTENTION : 위의 상황에 포함되지 않는다면

def face_detection(image: np.ndarray) -> Response:
    global module
    result: SimpleFaceDetectionResult = module.face_detection(image)
    if result is None:
        return Response(FDR.RESULT_TYPE_AFK, '자리를 비웠거나, 얼굴이 인식되지 않는 상황입니다.', result)
    elif result.blink[0] < 0.1 and result.blink[1] < 0.1:
        return Response(FDR.RESULT_TYPE_ASLEEP, '눈을 감고 있거나, 자고 있는 상태 입니다.', result)
    elif result.mouth > 0.5: # TODO: 시선과 고개에 따른 딴짓 여부에 대해 데이터가 아직 안들어가 있는 상태
        return Response(FDR.RESULT_TYPE_DISTRACTED, '딴 짓을 하고 있는 상태 입니다. (하품, 시선, 고개)', result)
    else:
        return Response(FDR.RESULT_TYPE_ATTENTION, '집중 중', result)

def face_detection_from_base64_string(base64_string: str) -> Response:
    image = data_uri_to_cv2_img(base64_string)
    return face_detection(image)

def face_detection_task(uid: int, rid: int, b64_string: str) -> Response:
    eid: Optional[int] = repository.select_entrant_eid_by_uid_and_rid(uid, rid)
    if eid is None:
        return Response(result=FDR.RESULT_TYPE_UNAUTHORIZED, message="해당 방에 가입하지 않은 사용자 입니다.")


# 서비스 코드 종료 ##

# 테스트용 모듈 코드
if __name__ == "__main__":
    cap = cv2.VideoCapture(0)
    while True:
        _ret, _frame = cap.read()

        if not _ret:
            break
        data: Optional[SimpleFaceDetectionResult] = module.face_detection(_frame)
        print(data)

    cap.release()
    cv2.destroyAllWindows()
