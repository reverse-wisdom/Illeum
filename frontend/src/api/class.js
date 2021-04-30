import { instance, posts } from './index';

//방리스트조회 (2. WebRTC 전체 조회)
function classAll(classData) {
  return posts.get('/room/findAll', classData);
}

//방 참여 로직
function insertRoom(insertInfo) {
  return posts.post('/entrant/insert', insertInfo);
}

//방생성
function createClass(classData) {
  return posts.post('/room/insert', classData);
}
//방수정(rid만 필수)
function updateClass(classData) {
  return posts.put('/room/updateByRid', classData);
}
//방 삭제
function deleteClass(classData) {
  return posts.delete('/room/deleteByRid', classData);
}
//방에참여한 멤버목록조회
function getStudents(studentData) {
  return posts.get('/room/member', studentData);
}
//room_name으로 방 조회
function fetchRoomname(roomName) {
  return posts.get(`/room/findByRoomName?roomName=${roomName}`);
}
//방에 참여한 멤버의 평가 목록 조회
function evaluateList(roomId) {
  return posts.get(`/room/evaluation?rid=${roomId}`);
}

//개설자 uid로 방조회 (Rid)
function findByRidClass(rid) {
  return posts.get('/room/findByRid', rid);
}
//개설자 uid로 방조회 (uid)
function findByUidClass(classData) {
  return posts.get('/room/findByUid', uuid);
}

export { createClass, updateClass, deleteClass, getStudents, fetchRoomname, evaluateList, classAll, findByRidClass, findByUidClass, insertRoom };
