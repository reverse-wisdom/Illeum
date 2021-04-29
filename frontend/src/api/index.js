import axios from 'axios';
import { setInterceptors } from './common/interceptors';

function createInstance() {
  return axios.create({
    baseURL: 'https:/api',
  });
}

function createInstanceWithAuth(url) {
  const instance = axios.create({
    baseURL: 'https:/api',
  });
  return setInterceptors(instance);
}

export const instance = createInstance();
export const posts = createInstanceWithAuth();
