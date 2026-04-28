import axios from 'axios';

const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE || 'http://localhost:8080',
  timeout: 10000,
});

http.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

http.interceptors.response.use(
  (response) => {
    const payload = response.data;
    if (payload && typeof payload.code !== 'undefined') {
      if (payload.code === 200) {
        return payload.data;
      }
      return Promise.reject(new Error(payload.message || 'Request failed'));
    }
    return payload;
  },
  (error) => Promise.reject(error)
);

export default http;
