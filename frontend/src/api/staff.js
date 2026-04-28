import http from './http';

export function createUser(payload) {
  return http.post('/api/staff/users', payload);
}

export function createFundAccount(payload) {
  return http.post('/api/staff/fund-accounts', payload);
}

export function approveFundAccount(id) {
  return http.put(`/api/staff/fund-accounts/${id}/approve`);
}

export function updateFundPassword(id, payload) {
  return http.put(`/api/staff/fund-accounts/${id}/password`, payload);
}

export function depositFund(id, payload) {
  return http.post(`/api/staff/fund-accounts/${id}/deposit`, payload);
}

export function withdrawFund(id, payload) {
  return http.post(`/api/staff/fund-accounts/${id}/withdraw`, payload);
}

export function markFundLost(id) {
  return http.put(`/api/staff/fund-accounts/${id}/lost`);
}

export function reopenFund(id) {
  return http.put(`/api/staff/fund-accounts/${id}/reopen`);
}

export function closeFund(id) {
  return http.put(`/api/staff/fund-accounts/${id}/close`);
}

export function getFundAccount(id) {
  return http.get(`/api/staff/fund-accounts/${id}`);
}

export function createSecuritiesAccount(payload) {
  return http.post('/api/staff/securities-accounts', payload);
}

export function markSecuritiesLost(id) {
  return http.put(`/api/staff/securities-accounts/${id}/lost`);
}

export function reopenSecurities(id) {
  return http.put(`/api/staff/securities-accounts/${id}/reopen`);
}

export function closeSecurities(id) {
  return http.put(`/api/staff/securities-accounts/${id}/close`);
}

export function getSecuritiesAccount(id) {
  return http.get(`/api/staff/securities-accounts/${id}`);
}

export function linkAccounts(payload) {
  return http.post('/api/staff/account-relations', payload);
}
