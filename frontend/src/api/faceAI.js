import axios from 'axios';

import { ai } from './index';

function faceAITest(frm) {
  return ai.post('/api/face/test_eval', frm, { headers: { 'Access-Control-Allow-Origin': '*', 'Content-Type': 'multipart/form-data' } });
}

function faceAI(userData) {
  // return ai.post('/api/face/eval', frm, { headers: { 'Access-Control-Allow-Origin': '*', 'Content-Type': 'multipart/form-data' } });
  return ai.post('/api/face/eval', userData, {
    headers: {
      'content-type': 'application/json',
    },
  });
}
export { faceAITest, faceAI };
