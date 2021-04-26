import axios from 'axios';
import { setInterceptors } from './common/interceptors';

function createInstance() {
  return axios.create({
    baseURL: 'http://k4d106.p.ssafy.io:8080/api',
  });
}

function createInstanceWithAuth(url) {
  const instance = axios.create({
    baseURL: 'http://k4d106.p.ssafy.io:8080/api',
  });
  return setInterceptors(instance);
}

export const instance = createInstance();
export const posts = createInstanceWithAuth();
