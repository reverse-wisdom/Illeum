import { instance, posts } from './index';

//평가조회
function fetchEval() {
  return posts.get('/evaluation/findAll');
}
export { fetchEval };
