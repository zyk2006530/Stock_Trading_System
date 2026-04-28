import { createRouter, createWebHistory } from 'vue-router';
import LoginView from '../views/LoginView.vue';
import StocksView from '../views/StocksView.vue';
import FundAccountView from '../views/FundAccountView.vue';
import HoldingsView from '../views/HoldingsView.vue';
import TradeView from '../views/TradeView.vue';
import OrdersView from '../views/OrdersView.vue';
import TradesView from '../views/TradesView.vue';
import StaffDashboardView from '../views/StaffDashboardView.vue';
import AdminDashboardView from '../views/AdminDashboardView.vue';
import { useAuthStore } from '../store/auth';

const routes = [
  {
    path: '/',
    redirect: '/stocks',
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginView,
  },
  {
    path: '/stocks',
    name: 'Stocks',
    component: StocksView,
  },
  {
    path: '/fund-account',
    name: 'FundAccount',
    component: FundAccountView,
    meta: { requiresAuth: true },
  },
  {
    path: '/holdings',
    name: 'Holdings',
    component: HoldingsView,
    meta: { requiresAuth: true },
  },
  {
    path: '/trade',
    name: 'Trade',
    component: TradeView,
    meta: { requiresAuth: true },
  },
  {
    path: '/orders',
    name: 'Orders',
    component: OrdersView,
    meta: { requiresAuth: true },
  },
  {
    path: '/trades',
    name: 'Trades',
    component: TradesView,
    meta: { requiresAuth: true },
  },
  {
    path: '/staff',
    name: 'Staff',
    component: StaffDashboardView,
    meta: { requiresAuth: true, roles: ['STAFF'] },
  },
  {
    path: '/admin',
    name: 'Admin',
    component: AdminDashboardView,
    meta: { requiresAuth: true, roles: ['ADMIN'] },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to) => {
  const authStore = useAuthStore();
  authStore.loadFromStorage();
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    return { path: '/login', query: { redirect: to.fullPath } };
  }
  if (to.meta.roles && authStore.user && !to.meta.roles.includes(authStore.user.role)) {
    return { path: '/stocks' };
  }
  if (to.path === '/login' && authStore.isAuthenticated) {
    return { path: '/stocks' };
  }
  return true;
});

export default router;
