import { instance, posts } from './index';
import axios from 'axios';

// 평가기록이 있는 청강자 전체조회
function partinAll() {
  return instance.get('/entrant/findAll');
}

export { partinAll };
