from collections import deque
from typing import Deque, Dict, Tuple

from bidict import bidict

from time import time

import models.FaceDetectionResponse as ty

RESULT_TYPE_TOTAL = "total"
RESULT_TYPE_TOTAL_SCODE = 0

status_mapping = bidict({
    RESULT_TYPE_TOTAL: RESULT_TYPE_TOTAL_SCODE,
    ty.RESULT_TYPE_ATTENTION: 1,
    ty.RESULT_TYPE_DISTRACTED: 2,
    ty.RESULT_TYPE_ASLEEP: 3,
    ty.RESULT_TYPE_AFK: 4
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