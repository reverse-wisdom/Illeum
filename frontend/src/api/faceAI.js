import axios from 'axios';

import { ai } from './index';

function faceAITest(frm) {
  return ai.post('/test_eval', frm, { headers: { 'Access-Control-Allow-Origin': '*', 'Content-Type': 'multipart/form-data' } });
}

function faceAI(userData) {
  // return ai.post('/api/face/eval', frm, { headers: { 'Access-Control-Allow-Origin': '*', 'Content-Type': 'multipart/form-data' } });
  return ai.post('/eval', userData, {
    headers: {
      'content-type': 'application/json',
    },
  });
}
export { faceAITest, faceAI };
