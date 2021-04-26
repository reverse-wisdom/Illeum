import { instance, posts } from './index';

// 로그인
function loginUser(userData) {
  return instance.post(``);
}

//회원가입
function register(userData) {
  return instance.post(``);
}
//정보수정
function editUser(userData) {
  return posts.put(``);
}

//회원탈퇴
function signout(email) {
  return posts.delete(``);
}

export { loginUser, register, editUser, signout };
