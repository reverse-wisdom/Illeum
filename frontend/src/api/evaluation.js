import { instance, posts } from './index';

//평가조회
function fetchEval() {
  return posts.get('/evaluation/findAll');
}

//평가 생성
function insertEvaluation(evaluationData) {
  return posts.post('/evaluation/insert', evaluationData);
}

//평가 수정(채팅 정보 보내기용:participation,ranking)
function updateByVid(evaluationData) {
  return posts.put('/evaluation/updateByVid', evaluationData);
}
export { fetchEval, insertEvaluation, updateByVid };
