import { defineStore } from 'pinia';

const TOKEN_KEY = 'token';
const USER_KEY = 'user';

function readUser() {
  const raw = localStorage.getItem(USER_KEY);
  if (!raw) {
    return null;
  }
  try {
    return JSON.parse(raw);
  } catch {
    return null;
  }
}

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem(TOKEN_KEY) || '',
    user: readUser(),
  }),
  getters: {
    isAuthenticated: (state) => Boolean(state.token),
  },
  actions: {
    setAuth(token, user) {
      this.token = token;
      this.user = user;
      localStorage.setItem(TOKEN_KEY, token);
      localStorage.setItem(USER_KEY, JSON.stringify(user));
    },
    clear() {
      this.token = '';
      this.user = null;
      localStorage.removeItem(TOKEN_KEY);
      localStorage.removeItem(USER_KEY);
    },
    loadFromStorage() {
      if (!this.token) {
        this.token = localStorage.getItem(TOKEN_KEY) || '';
      }
      if (!this.user) {
        this.user = readUser();
      }
    },
  },
});
