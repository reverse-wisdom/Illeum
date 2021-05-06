from collections import deque
from typing import Deque, Dict, Tuple
from multiprocessing.managers import SyncManager

from bidict import bidict

from time import time, sleep

if "_main__" in __name__:
    from FaceResultType import RESULT_TYPE_AFK, RESULT_TYPE_ASLEEP, RESULT_TYPE_DISTRACTED, RESULT_TYPE_ATTENTION
else:
    from .FaceResultType import RESULT_TYPE_AFK, RESULT_TYPE_ASLEEP, RESULT_TYPE_DISTRACTED, RESULT_TYPE_ATTENTION
RESULT_TYPE_TOTAL = "total"
RESULT_TYPE_TOTAL_SCODE = 0

status_mapping = bidict({
    RESULT_TYPE_TOTAL: RESULT_TYPE_TOTAL_SCODE,
    RESULT_TYPE_ATTENTION: 1,
    RESULT_TYPE_DISTRACTED: 2,
    RESULT_TYPE_ASLEEP: 3,
    RESULT_TYPE_AFK: 4
})


class Node:
    reg_time: float
    uid: int
    scode: int

    def __init__(self, uid: int, scode: int):
        self.reg_time = time()
        self.uid = uid
        self.scode = scode


class TTLStatusCounter:
    TTL: float
    queue: Deque[Node]
    map: Dict[int, list]

    def __init__(self, TTL: float = 600.0):
        self.queue = deque()
        self.map = dict()
        self.TTL = TTL

    def clear(self):
        self.queue = deque()
        self.map = dict()

    def get_total(self, uid: int) -> int:
        self.decay()
        return self.map[uid][RESULT_TYPE_TOTAL_SCODE]

    def _decrease(self, uid: int, scode: int, count: int = 1) -> None:
        self.map[uid][RESULT_TYPE_TOTAL_SCODE] \
            = self.map[uid][RESULT_TYPE_TOTAL_SCODE] - count if self.map[uid][RESULT_TYPE_TOTAL_SCODE] >= count else 0
        self.map[uid][scode] \
            = self.map[uid][scode] - count if self.map[uid][scode] >= count else 0

    def decay(self):
        while len(self.queue) > 0 and (self.queue[0].reg_time + self.TTL) < time():
            node = self.queue.popleft()
            self._decrease(node.uid, node.scode)

    def increase(self, uid: int, status: str) -> Tuple[int, int]:
        if uid not in self.map:  # uid에 해당하는 값이 없다면 초기화 리스트 할당
            self.map[uid] = [0 for i in range(len(status_mapping))]
        self.decay()  # 먼저 TTL로 소멸될 값들이 있나 체크
        self.map[uid][RESULT_TYPE_TOTAL_SCODE] += 1
        scode: int = status_mapping[status]
        self.map[uid][scode] += 1
        self.queue.append(Node(uid, scode))
        return self.map[uid][scode], self.map[uid][RESULT_TYPE_TOTAL_SCODE]

    def decrease(self, uid: int, status: str, count: int = 1) -> None:
        if uid not in self.map:  # 존재하지 않는 유저라면 skip
            return
        self.decay()
        self._decrease(uid, status_mapping[status], count)


ttl_status_counter = TTLStatusCounter()


def get_ttl_status_counter():
    global ttl_status_counter
    return ttl_status_counter


class MySyncManager(SyncManager):
    pass


if __name__ == "__main__":
    MySyncManager.register("TTLStatusCounter", get_ttl_status_counter)
    manager = MySyncManager(("127.0.0.1", 5678), authkey=b"ttl_status_counter123@")
    manager.start()
    while True:
        try:
            sleep(10)
        except Exception as e:
            from datetime import datetime
            print(str(datetime.time(datetime.now()))[:8] + str(e))
            break
    manager.shutdown()
elif __name__ == "__mp_main__":  # SyncManager 프로세스가 다른 실행문을 참조하지 않도록 방지
    pass
else:
    manager = MySyncManager(("127.0.0.1", 5678), authkey=b"ttl_status_counter123@")
    manager.connect()
    MySyncManager.register("TTLStatusCounter")
    ttl_status_counter = manager.TTLStatusCounter()
