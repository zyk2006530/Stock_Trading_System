<template>
  <section class="login-page">
    <el-card class="login-card">
      <h2 class="title">Sign in</h2>
      <el-tabs v-model="activeTab" class="login-tabs">
        <el-tab-pane label="Login" name="login">
          <el-form :model="loginForm" label-position="top">
            <el-form-item label="Username">
              <el-input v-model="loginForm.username" autocomplete="username" />
            </el-form-item>
            <el-form-item label="Password">
              <el-input v-model="loginForm.password" type="password" autocomplete="current-password" />
            </el-form-item>
            <el-button type="primary" :loading="loading" @click="handleLogin">
              Login
            </el-button>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="First Login" name="first">
          <el-form :model="firstLoginForm" label-position="top">
            <el-form-item label="Username">
              <el-input v-model="firstLoginForm.username" autocomplete="username" />
            </el-form-item>
            <el-form-item label="Password">
              <el-input v-model="firstLoginForm.password" type="password" autocomplete="current-password" />
            </el-form-item>
            <el-form-item label="Certificate Code">
              <el-input v-model="firstLoginForm.certificateCode" autocomplete="off" />
            </el-form-item>
            <el-button type="primary" :loading="loading" @click="handleFirstLogin">
              Verify & Login
            </el-button>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </section>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import { login, firstLogin } from '../api/auth';
import { useAuthStore } from '../store/auth';

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();

const activeTab = ref('login');
const loading = ref(false);

const loginForm = reactive({
  username: '',
  password: '',
});

const firstLoginForm = reactive({
  username: '',
  password: '',
  certificateCode: '',
});

async function handleLogin() {
  loading.value = true;
  try {
    const data = await login(loginForm);
    authStore.setAuth(data.token, data.user);
    router.push(route.query.redirect || '/stocks');
  } catch (error) {
    ElMessage.error(error.message || 'Login failed');
  } finally {
    loading.value = false;
  }
}

async function handleFirstLogin() {
  loading.value = true;
  try {
    const data = await firstLogin(firstLoginForm);
    authStore.setAuth(data.token, data.user);
    router.push(route.query.redirect || '/stocks');
  } catch (error) {
    ElMessage.error(error.message || 'Verification failed');
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 120px);
}

.login-card {
  width: min(420px, 90vw);
  padding: 8px 8px 24px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.88);
  box-shadow: 0 20px 60px rgba(15, 23, 42, 0.18);
}

.title {
  margin: 0 0 12px;
  font-size: 1.6rem;
}

.login-tabs {
  margin-top: 8px;
}
</style>
