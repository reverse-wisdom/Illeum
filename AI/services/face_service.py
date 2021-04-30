# 모듈을 gitpython 등을 사용해서 다운로드 후, import 연결.
import os
import sys
from typing import Optional

import git
import cv2

BASE_REPO_DIR = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), "OpenVtuber")

if not os.path.isdir(BASE_REPO_DIR):
    print("OpenVtuber 프로젝트 클론 중", end=' ')
    git.Repo.clone_from("https://github.com/getCurrentThread/OpenVtuber",
                        BASE_REPO_DIR)
    print("done")

sys.path.insert(1, BASE_REPO_DIR)
from OpenVtuber.SimpleFaceModule import SimpleFaceModule, SimpleFaceDetectionResult

# 서비스 코드 시작 ##


# 서비스 코드 종료 ##

# 테스트용 모듈 코드
if __name__ == "__main__":
    cap = cv2.VideoCapture(0)
    module = SimpleFaceModule()
    while True:
        _ret, _frame = cap.read()

        if not _ret:
            break
        data: Optional[SimpleFaceDetectionResult] = module.face_detection(_frame)
        print(data)

    cap.release()
    cv2.destroyAllWindows()
