import http from './http';

export function buyOrder(payload) {
  return http.post('/api/client/orders/buy', payload);
}

export function sellOrder(payload) {
  return http.post('/api/client/orders/sell', payload);
}
