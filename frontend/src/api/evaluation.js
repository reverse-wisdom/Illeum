import { instance, posts } from './index';

//평가조회
function fetchEval() {
  return posts.get('/evaluation/findAll');
}

//평가 생성
function insertEvaluation(evaluationData) {
  return posts.post('/evaluation/insert', evaluationData);
}
export { fetchEval, insertEvaluation };
