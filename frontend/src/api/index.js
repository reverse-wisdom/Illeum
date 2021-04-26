import axios from 'axios';
import { setInterceptors } from './common/interceptors';

function createInstance() {
  return axios.create({
    baseURL: '',
  });
}

function createInstanceWithAuth(url) {
  const instance = axios.create({
    baseURL: '',
  });
  return setInterceptors(instance);
}

export const instance = createInstance();
export const posts = createInstanceWithAuth();
