<template>
  <section class="fund-page">
    <el-card class="panel">
      <div class="panel-header">
        <div>
          <h2>Fund Account</h2>
          <p class="subtitle">Balance and recent fund flows.</p>
        </div>
        <el-button type="primary" plain @click="loadData">Refresh</el-button>
      </div>
      <div class="summary" v-if="account">
        <div class="summary-item">
          <span>Account No</span>
          <strong>{{ account.accountNo }}</strong>
        </div>
        <div class="summary-item">
          <span>Balance</span>
          <strong>{{ formatMoney(account.balance) }}</strong>
        </div>
        <div class="summary-item">
          <span>Frozen</span>
          <strong>{{ formatMoney(account.frozenBalance) }}</strong>
        </div>
        <div class="summary-item">
          <span>Status</span>
          <strong>{{ account.status }}</strong>
        </div>
      </div>
      <el-table v-loading="loading" :data="flows" size="small">
        <el-table-column prop="createdAt" label="Time" />
        <el-table-column prop="type" label="Type" width="140" />
        <el-table-column prop="amount" label="Amount" width="140" />
        <el-table-column prop="balanceAfter" label="Balance" width="140" />
        <el-table-column prop="remark" label="Remark" />
      </el-table>
    </el-card>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { fetchFundAccount } from '../api/client';
import { formatMoney } from '../utils/format';

const account = ref(null);
const flows = ref([]);
const loading = ref(false);

async function loadData() {
  loading.value = true;
  try {
    const data = await fetchFundAccount();
    account.value = data.account;
    flows.value = data.flows || [];
  } catch {
    // http.js 已统一处理错误提示
  } finally {
    loading.value = false;
  }
}

onMounted(loadData);
</script>

<style scoped>
.panel {
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.9);
  box-shadow: 0 20px 60px rgba(15, 23, 42, 0.12);
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.subtitle {
  margin: 4px 0 0;
  color: rgba(31, 41, 55, 0.6);
}

.summary {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
  gap: 16px;
  margin-bottom: 16px;
}

.summary-item {
  background: rgba(248, 250, 252, 0.8);
  border-radius: 16px;
  padding: 12px;
}

.summary-item span {
  display: block;
  font-size: 0.75rem;
  color: rgba(31, 41, 55, 0.6);
}

.summary-item strong {
  font-size: 1.05rem;
}
</style>
