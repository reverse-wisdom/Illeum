import axios from 'axios';
import { setInterceptors } from './common/interceptors';

function createInstance() {
  return axios.create({
    baseURL: '/api',
  });
}

function createInstanceWithAuth(url) {
  const instance = axios.create({
    baseURL: '/api',
  });
  return setInterceptors(instance);
}

export const instance = createInstance();
export const posts = createInstanceWithAuth();
