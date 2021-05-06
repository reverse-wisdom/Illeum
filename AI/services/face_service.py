# 모듈을 gitpython 등을 사용해서 다운로드 후, import 연결.
from typing import Optional

import cv2
import numpy as np

import crud.evalutation_repository as repository

import models.FaceDetectionResponse as FDR
from models.FaceDetectionResponse import FaceDetectionResponse as Response

from services import SimpleFaceModule, SimpleFaceDetectionResult

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
        return Response(FDR.RESULT_TYPE_AFK, '자리를 비웠거나, 얼굴이 인식되지 않는 상황입니다.', None)
    elif result.blink[0] < 0.1 and result.blink[1] < 0.1:
        return Response(FDR.RESULT_TYPE_ASLEEP, '눈을 감고 있거나, 자고 있는 상태 입니다.', result)
    elif result.mouth > 0.5:  # TODO: 시선과 고개에 따른 딴짓 여부에 대해 데이터가 아직 안들어가 있는 상태
        return Response(FDR.RESULT_TYPE_DISTRACTED, '딴 짓을 하고 있는 상태 입니다. (하품, 시선, 고개)', result)
    else:
        return Response(FDR.RESULT_TYPE_ATTENTION, '집중 중', result)


# 각 결과에 따라 repository 함수 매핑
repo_funcs = {
    FDR.RESULT_TYPE_AFK: repository.update_evaluation_increase_afk_by_eid,
    FDR.RESULT_TYPE_ASLEEP: repository.update_evaluation_increase_asleep_by_eid,
    FDR.RESULT_TYPE_ATTENTION: repository.update_evaluation_increase_attention_by_eid,
    FDR.RESULT_TYPE_DISTRACTED: repository.update_evaluation_increase_distracted_by_eid
}


def face_detection_task(uid: int, rid: int, content: bytes) -> Response:
    eid: Optional[int] = repository.select_entrant_eid_by_uid_and_rid(uid, rid)
    if eid is None:
        return Response(result=FDR.RESULT_TYPE_UNAUTHORIZED, message="해당 방에 가입하지 않은 사용자 입니다.")
    vid: Optional[int] = repository.select_evaluation_vid_recent_by_eid(eid)
    if vid is None:
        return Response(result=FDR.RESULT_TYPE_NOT_DEFINED_EVAL, message="아직 평가가 생성되지 않은 사용자 입니다.")
    nparr = np.frombuffer(content, np.int8)
    # print(type(nparr))
    img_np = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

    resp: Response = face_detection(img_np)
    repo_funcs[resp.result](eid)  # 각 결과에 따라 카운트 하는 쿼리문 실행
    return resp


# 서비스 코드 종료 ##

# 테스트용 모듈 코드
if __name__ == "__main__":
    cap = cv2.VideoCapture(2)
    while True:
        _ret, _frame = cap.read()

        if not _ret:
            break
        data: Optional[SimpleFaceDetectionResult] = module.face_detection(_frame)
        print(data)

    cap.release()
    cv2.destroyAllWindows()
