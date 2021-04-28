import { instance, posts } from './index';

// 로그인
function loginUser(userData) {
  return instance.post('/member/user/login', userData);
}
// 로그아웃
function logoutUser(userData) {
  return posts.post('/member/user/logout', userData);
}

//회원가입
function registerUser(userData) {
  return instance.post('/member/user/signup', userData);
}
//이메일 중복체크
function checkEmail(userData) {
  return instance.get('/member/user/checkemail', userData);
}
//정보수정
function editUser(userData) {
  return posts.put('/member/user/update', userData);
}

//회원탈퇴
function signout(userData) {
  return posts.delete('/member/user/delete', userData);
}

//멤버가 참여한 방목록
function usePartinClass(userData) {
  return posts.get(`/member/user/room?uid=${userData}`);
}
//멤범의 평가 목록
function userEvalList(userData) {
  return posts.get(`/member/user/evaluation?uid=${userData}`);
}
//멤버가 개설한방 목록
function userClasslist(userData) {
  return posts.get('/member/user/founder', userData);
}

export { loginUser, registerUser, editUser, signout, checkEmail, logoutUser, usePartinClass, userClasslist, userEvalList };
