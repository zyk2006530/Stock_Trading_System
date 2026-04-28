import axios from 'axios';
import { ElMessage } from 'element-plus';

function getBaseURL() {
  if (import.meta.env.VITE_API_BASE) {
    return import.meta.env.VITE_API_BASE;
  }
  return `${window.location.protocol}//${window.location.hostname}:8080`;
}

const http = axios.create({
  baseURL: getBaseURL(),
  timeout: 10000,
});

http.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

const statusMessages = {
  401: '请先登录',
  403: '没有权限访问该功能',
  404: '请求的资源不存在',
  500: '服务器错误，请稍后再试',
};

http.interceptors.response.use(
  (response) => {
    const payload = response.data;
    if (payload && typeof payload.code !== 'undefined') {
      if (payload.code === 200) {
        return payload.data;
      }
      return Promise.reject(new Error(payload.message || '请求失败'));
    }
    return payload;
  },
  (error) => {
    if (error.response) {
      const status = error.response.status;
      const msg = statusMessages[status] || `请求失败 (${status})`;
      ElMessage.error(msg);
      return Promise.reject(new Error(msg));
    }
    if (error.message === 'Network Error') {
      ElMessage.error('网络连接失败，请检查网络');
      return Promise.reject(new Error('网络连接失败，请检查网络'));
    }
    ElMessage.error('请求异常，请稍后再试');
    return Promise.reject(error);
  }
);

export default http;
