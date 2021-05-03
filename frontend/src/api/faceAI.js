import axios from 'axios';

import { ai } from './index';

function faceAITest(frm) {
  return ai.post('/api/face/test_eval', frm, { headers: { 'Access-Control-Allow-Origin': '*', 'Content-Type': 'multipart/form-data' } });
}

function faceAI(frm) {
  return ai.post('/api/face/eval', frm, { headers: { 'Access-Control-Allow-Origin': '*', 'Content-Type': 'multipart/form-data' } });
}
export { faceAITest, faceAI };
