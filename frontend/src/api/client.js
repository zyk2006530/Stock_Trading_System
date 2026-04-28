import http from './http';

export function fetchFundAccount() {
  return http.get('/api/client/fund-account');
}

export function fetchSecuritiesAccount() {
  return http.get('/api/client/securities-account');
}

export function fetchHoldings() {
  return http.get('/api/client/holdings');
}

export function fetchOrders() {
  return http.get('/api/client/orders');
}

export function fetchTrades() {
  return http.get('/api/client/trades');
}
