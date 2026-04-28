import http from './http';

export function login(payload) {
  return http.post('/api/auth/login', payload);
}

export function firstLogin(payload) {
  return http.post('/api/auth/first-login', payload);
}

export function fetchMe() {
  return http.get('/api/auth/me');
}

export function logout() {
  return http.post('/api/auth/logout');
}
