import { instance, posts } from './index';

//평가생성
function createEval(classData) {
  return posts.post('/room/insert', classData);
}
