import http from './http';

export function listStocks() {
  return http.get('/api/stocks');
}

export function searchStocks(keyword) {
  return http.get('/api/stocks/search', { params: { keyword } });
}

export function getStock(code) {
  return http.get(`/api/stocks/${code}`);
}

export function getStockTrades(code) {
  return http.get(`/api/stocks/${code}/trades`);
}

export function getStockStats(code) {
  return http.get(`/api/stocks/${code}/stats`);
}
