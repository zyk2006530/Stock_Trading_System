<template>
  <header class="app-header">
    <div class="brand">Stock Trading System</div>
    <el-menu
      :default-active="activePath"
      mode="horizontal"
      class="nav-menu"
      :ellipsis="false"
      router
    >
      <el-menu-item index="/stocks">Market</el-menu-item>
      <el-menu-item v-if="authStore.isAuthenticated" index="/fund-account">Fund</el-menu-item>
      <el-menu-item v-if="authStore.isAuthenticated" index="/holdings">Holdings</el-menu-item>
      <el-menu-item v-if="authStore.isAuthenticated" index="/trade">Trade</el-menu-item>
      <el-menu-item v-if="authStore.isAuthenticated" index="/orders">Orders</el-menu-item>
      <el-menu-item v-if="authStore.isAuthenticated" index="/trades">Trades</el-menu-item>
      <el-menu-item v-if="authStore.user?.role === 'STAFF'" index="/staff">Staff</el-menu-item>
      <el-menu-item v-if="authStore.user?.role === 'ADMIN'" index="/admin">Admin</el-menu-item>
    </el-menu>
    <div class="auth-actions">
      <span v-if="authStore.user" class="user-name">
        {{ authStore.user.username }}
      </span>
      <el-button v-if="authStore.isAuthenticated" type="primary" plain @click="handleLogout">
        Logout
      </el-button>
      <el-button v-else type="primary" @click="router.push('/login')">
        Login
      </el-button>
    </div>
  </header>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { logout } from '../api/auth';
import { useAuthStore } from '../store/auth';

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();

const activePath = computed(() => route.path);

async function handleLogout() {
  try {
    await logout();
  } catch {
    ElMessage.warning('Logout completed locally');
  }
  authStore.clear();
  router.push('/login');
}
</script>

<style scoped>
.app-header {
  display: grid;
  grid-template-columns: auto 1fr auto;
  align-items: center;
  gap: 16px;
  padding: 18px 24px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(8px);
}

.brand {
  font-size: 1.1rem;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.nav-menu {
  background: transparent;
  border-bottom: none;
}

.auth-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-name {
  font-size: 0.9rem;
  color: rgba(31, 41, 55, 0.7);
}
</style>
