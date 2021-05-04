import os
import git

def init():
    BASE_REPO_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "OpenVtuber")

    if not os.path.isdir(BASE_REPO_DIR):
        print("OpenVtuber 프로젝트 클론 중", end=' ')
        git.Repo.clone_from("https://github.com/getCurrentThread/OpenVtuber",
                            BASE_REPO_DIR)
        print("done")