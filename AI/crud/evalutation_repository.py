from typing import Optional

from cachetools import cached, TTLCache

from database.db import db


# async def check_password_on_board(
#         password: str,
#         board_no: int
# ):
#     # 해당 게시물 비밀번호 얻어오기
#     sql = "SELECT password FROM board WHERE board_no = %s"
#
#     with db.query(sql, ) as cursor:
#         cursor.execute(sql, (board_no,))
#         res = cursor.fetchone()
#         for pwd in res:
#             if pwd == password:
#                 return True
#             else:
#                 return False

@cached(cache=TTLCache(maxsize=1024, ttl=600))
def select_entrant_eid_by_uid_and_rid(uid: int, rid: int) -> Optional[int]:
    with db.query("SELECT eid FROM entrant WHERE uid = %s AND rid = %s", (uid, rid,)) as cursor:
        data = cursor.fetchone()
        if data:
            return data[0]
    return None


@cached(cache=TTLCache(maxsize=1024, ttl=1200))
def select_evaluation_vid_recent_by_eid(eid: int):
    with db.query("SELECT vid FROM evaluation WHERE eid = %s ORDER BY eval_date DESC LIMIT 1", (eid,)) as cursor:
        data = cursor.fetchone()
        if data:
            return data[0]
    return None


if __name__ == "__main__":
    rid = 2
    uid = 2
    # 2번 방에 2번 멤버가 소속해 있는 번호를 확인 (소속이 아닌 경우 None 반환)
    id = select_entrant_eid_by_uid_and_rid(rid, uid)
    print(f"{rid}번 방의 {uid}번 멤버의 참가 번호 : {id}")
    if id is None: # 소속이 안되어 있으므로 하단 과정은 패스
        pass
    # 1번 방의 1번 멤버의 참가자가 가지고 있는 최근 평가 번호를 출력
    id = select_evaluation_vid_recent_by_eid(id)
    print(f"해당 참가자의 최근 평가 번호 : {id}")
