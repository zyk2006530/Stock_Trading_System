import http from './http';

export function fetchAdminOrders(stockCode) {
  return http.get('/api/admin/orders', { params: { stockCode } });
}

export function fetchAdminTrades() {
  return http.get('/api/admin/trades');
}

export function fetchAdminOrderStats() {
  return http.get('/api/admin/stats/orders');
}

export function fetchAdminUsers() {
  return http.get('/api/admin/users');
}

export function fetchAdminAccounts() {
  return http.get('/api/admin/accounts');
}
