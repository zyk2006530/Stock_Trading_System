<template>
  <section class="staff-page">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="Create User" name="user">
        <el-card class="panel">
          <h2>New User</h2>
          <el-form :model="userForm" label-position="top">
            <el-form-item label="Username">
              <el-input v-model="userForm.username" />
            </el-form-item>
            <el-form-item label="Password">
              <el-input v-model="userForm.password" type="password" />
            </el-form-item>
            <el-form-item label="Real Name">
              <el-input v-model="userForm.realName" />
            </el-form-item>
            <el-form-item label="Phone">
              <el-input v-model="userForm.phone" />
            </el-form-item>
            <el-form-item label="Email">
              <el-input v-model="userForm.email" />
            </el-form-item>
            <el-form-item label="Certificate Code (optional)">
              <el-input v-model="userForm.certificateCode" />
            </el-form-item>
            <el-button type="primary" @click="submitUser">Create User</el-button>
          </el-form>
        </el-card>
      </el-tab-pane>
      <el-tab-pane label="Fund Accounts" name="fund">
        <el-card class="panel">
          <h2>Fund Account Operations</h2>
          <el-form :model="fundCreateForm" label-position="top" class="form-block">
            <h3>Create Fund Account</h3>
            <el-form-item label="User ID">
              <el-input v-model="fundCreateForm.userId" />
            </el-form-item>
            <el-form-item label="Password">
              <el-input v-model="fundCreateForm.password" type="password" />
            </el-form-item>
            <el-form-item label="Initial Balance">
              <el-input-number v-model="fundCreateForm.initialBalance" :min="0" :step="100" />
            </el-form-item>
            <el-button type="primary" @click="submitFundCreate">Create</el-button>
          </el-form>
          <el-divider />
          <el-form :model="fundActionForm" label-position="top" class="form-block">
            <h3>Manage Fund Account</h3>
            <el-form-item label="Fund Account ID">
              <el-input v-model="fundActionForm.id" />
            </el-form-item>
            <el-form-item label="Amount">
              <el-input-number v-model="fundActionForm.amount" :min="0.01" :step="100" />
            </el-form-item>
            <div class="action-buttons">
              <el-button @click="approveFund">Approve</el-button>
              <el-button @click="depositFundAction">Deposit</el-button>
              <el-button @click="withdrawFundAction">Withdraw</el-button>
              <el-button @click="markFundLostAction">Lost</el-button>
              <el-button @click="reopenFundAction">Reopen</el-button>
              <el-button @click="closeFundAction">Close</el-button>
            </div>
          </el-form>
          <el-divider />
          <el-form :model="fundPasswordForm" label-position="top" class="form-block">
            <h3>Update Password</h3>
            <el-form-item label="Fund Account ID">
              <el-input v-model="fundPasswordForm.id" />
            </el-form-item>
            <el-form-item label="New Password">
              <el-input v-model="fundPasswordForm.newPassword" type="password" />
            </el-form-item>
            <el-button type="primary" @click="updateFundPasswordAction">Update</el-button>
          </el-form>
        </el-card>
      </el-tab-pane>
      <el-tab-pane label="Securities Accounts" name="securities">
        <el-card class="panel">
          <h2>Securities Account Operations</h2>
          <el-form :model="secCreateForm" label-position="top" class="form-block">
            <h3>Create Securities Account</h3>
            <el-form-item label="User ID">
              <el-input v-model="secCreateForm.userId" />
            </el-form-item>
            <el-form-item label="Fund Account ID (optional)">
              <el-input v-model="secCreateForm.fundAccountId" />
            </el-form-item>
            <el-button type="primary" @click="submitSecuritiesCreate">Create</el-button>
          </el-form>
          <el-divider />
          <el-form :model="secActionForm" label-position="top" class="form-block">
            <h3>Manage Securities Account</h3>
            <el-form-item label="Securities Account ID">
              <el-input v-model="secActionForm.id" />
            </el-form-item>
            <div class="action-buttons">
              <el-button @click="markSecuritiesLostAction">Lost</el-button>
              <el-button @click="reopenSecuritiesAction">Reopen</el-button>
              <el-button @click="closeSecuritiesAction">Close</el-button>
            </div>
          </el-form>
        </el-card>
      </el-tab-pane>
      <el-tab-pane label="Link Accounts" name="link">
        <el-card class="panel">
          <h2>Link Fund and Securities</h2>
          <el-form :model="linkForm" label-position="top">
            <el-form-item label="Fund Account ID">
              <el-input v-model="linkForm.fundAccountId" />
            </el-form-item>
            <el-form-item label="Securities Account ID">
              <el-input v-model="linkForm.securitiesAccountId" />
            </el-form-item>
            <el-button type="primary" @click="linkAccountsAction">Link</el-button>
          </el-form>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </section>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import {
  createUser,
  createFundAccount,
  approveFundAccount,
  updateFundPassword,
  depositFund,
  withdrawFund,
  markFundLost,
  reopenFund,
  closeFund,
  createSecuritiesAccount,
  markSecuritiesLost,
  reopenSecurities,
  closeSecurities,
  linkAccounts,
} from '../api/staff';

const activeTab = ref('user');

const userForm = reactive({
  username: '',
  password: '',
  realName: '',
  phone: '',
  email: '',
  certificateCode: '',
});

const fundCreateForm = reactive({
  userId: '',
  password: '',
  initialBalance: 0,
});

const fundActionForm = reactive({
  id: '',
  amount: 0,
});

const fundPasswordForm = reactive({
  id: '',
  newPassword: '',
});

const secCreateForm = reactive({
  userId: '',
  fundAccountId: '',
});

const secActionForm = reactive({
  id: '',
});

const linkForm = reactive({
  fundAccountId: '',
  securitiesAccountId: '',
});

async function submitUser() {
  try {
    await createUser(userForm);
    ElMessage.success('User created');
  } catch (error) {
    ElMessage.error(error.message || 'Create user failed');
  }
}

async function submitFundCreate() {
  try {
    await createFundAccount({
      userId: Number(fundCreateForm.userId),
      password: fundCreateForm.password,
      initialBalance: Number(fundCreateForm.initialBalance || 0),
    });
    ElMessage.success('Fund account created');
  } catch (error) {
    ElMessage.error(error.message || 'Create fund account failed');
  }
}

async function approveFund() {
  try {
    await approveFundAccount(Number(fundActionForm.id));
    ElMessage.success('Fund account approved');
  } catch (error) {
    ElMessage.error(error.message || 'Approve failed');
  }
}

async function depositFundAction() {
  try {
    await depositFund(Number(fundActionForm.id), { amount: Number(fundActionForm.amount) });
    ElMessage.success('Deposit done');
  } catch (error) {
    ElMessage.error(error.message || 'Deposit failed');
  }
}

async function withdrawFundAction() {
  try {
    await withdrawFund(Number(fundActionForm.id), { amount: Number(fundActionForm.amount) });
    ElMessage.success('Withdraw done');
  } catch (error) {
    ElMessage.error(error.message || 'Withdraw failed');
  }
}

async function markFundLostAction() {
  try {
    await markFundLost(Number(fundActionForm.id));
    ElMessage.success('Marked lost');
  } catch (error) {
    ElMessage.error(error.message || 'Operation failed');
  }
}

async function reopenFundAction() {
  try {
    await reopenFund(Number(fundActionForm.id));
    ElMessage.success('Reopened');
  } catch (error) {
    ElMessage.error(error.message || 'Operation failed');
  }
}

async function closeFundAction() {
  try {
    await closeFund(Number(fundActionForm.id));
    ElMessage.success('Closed');
  } catch (error) {
    ElMessage.error(error.message || 'Operation failed');
  }
}

async function updateFundPasswordAction() {
  try {
    await updateFundPassword(Number(fundPasswordForm.id), { newPassword: fundPasswordForm.newPassword });
    ElMessage.success('Password updated');
  } catch (error) {
    ElMessage.error(error.message || 'Update failed');
  }
}

async function submitSecuritiesCreate() {
  try {
    await createSecuritiesAccount({
      userId: Number(secCreateForm.userId),
      fundAccountId: secCreateForm.fundAccountId ? Number(secCreateForm.fundAccountId) : null,
    });
    ElMessage.success('Securities account created');
  } catch (error) {
    ElMessage.error(error.message || 'Create securities account failed');
  }
}

async function markSecuritiesLostAction() {
  try {
    await markSecuritiesLost(Number(secActionForm.id));
    ElMessage.success('Marked lost');
  } catch (error) {
    ElMessage.error(error.message || 'Operation failed');
  }
}

async function reopenSecuritiesAction() {
  try {
    await reopenSecurities(Number(secActionForm.id));
    ElMessage.success('Reopened');
  } catch (error) {
    ElMessage.error(error.message || 'Operation failed');
  }
}

async function closeSecuritiesAction() {
  try {
    await closeSecurities(Number(secActionForm.id));
    ElMessage.success('Closed');
  } catch (error) {
    ElMessage.error(error.message || 'Operation failed');
  }
}

async function linkAccountsAction() {
  try {
    await linkAccounts({
      fundAccountId: Number(linkForm.fundAccountId),
      securitiesAccountId: Number(linkForm.securitiesAccountId),
    });
    ElMessage.success('Accounts linked');
  } catch (error) {
    ElMessage.error(error.message || 'Link failed');
  }
}
</script>

<style scoped>
.panel {
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.9);
  box-shadow: 0 20px 60px rgba(15, 23, 42, 0.12);
}

.form-block {
  margin-bottom: 20px;
}

.action-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}
</style>
