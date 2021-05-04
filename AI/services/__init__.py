# controller 에서 요청 받은 메세지를 처리하여, 반환하는 함수 또는 클래스
import os
import sys

import git

BASE_REPO_DIR = os.path.join("/volumes", "OpenVtuber")

if not os.path.isdir(BASE_REPO_DIR):
    print("OpenVtuber 프로젝트 클론 중", end=' ')
    git.Repo.clone_from("https://github.com/getCurrentThread/OpenVtuber",
                        BASE_REPO_DIR)
    print("done")

sys.path.insert(1, BASE_REPO_DIR)
from SimpleFaceModule import SimpleFaceModule, SimpleFaceDetectionResult