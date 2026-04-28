<template>
  <section class="admin-page">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="Orders" name="orders">
        <el-card class="panel">
          <div class="panel-header">
            <div>
              <h2>All Orders</h2>
              <p class="subtitle">Filter by stock code.</p>
            </div>
            <div class="filter">
              <el-input v-model="orderFilter" placeholder="Stock code" />
              <el-button type="primary" @click="loadOrders">Search</el-button>
            </div>
          </div>
          <el-table v-loading="loadingOrders" :data="orders">
            <el-table-column prop="orderNo" label="Order No" width="200" />
            <el-table-column prop="userId" label="User" width="120" />
            <el-table-column prop="stockCode" label="Code" width="120" />
            <el-table-column prop="direction" label="Side" width="120" />
            <el-table-column prop="price" label="Price" width="120" />
            <el-table-column prop="quantity" label="Qty" width="100" />
            <el-table-column prop="status" label="Status" width="140" />
            <el-table-column prop="createdAt" label="Time" />
          </el-table>
        </el-card>
      </el-tab-pane>
      <el-tab-pane label="Trades" name="trades">
        <el-card class="panel">
          <div class="panel-header">
            <div>
              <h2>All Trades</h2>
              <p class="subtitle">System trade log.</p>
            </div>
            <el-button type="primary" plain @click="loadTrades">Refresh</el-button>
          </div>
          <el-table v-loading="loadingTrades" :data="trades">
            <el-table-column prop="tradeNo" label="Trade No" width="200" />
            <el-table-column prop="stockCode" label="Code" width="120" />
            <el-table-column prop="buyerUserId" label="Buyer" width="120" />
            <el-table-column prop="sellerUserId" label="Seller" width="120" />
            <el-table-column prop="price" label="Price" width="120" />
            <el-table-column prop="quantity" label="Qty" width="100" />
            <el-table-column prop="amount" label="Amount" width="140" />
            <el-table-column prop="createdAt" label="Time" />
          </el-table>
        </el-card>
      </el-tab-pane>
      <el-tab-pane label="Stats" name="stats">
        <el-card class="panel">
          <div class="panel-header">
            <div>
              <h2>Order Statistics</h2>
              <p class="subtitle">Per-stock buy/sell totals.</p>
            </div>
            <el-button type="primary" plain @click="loadStats">Refresh</el-button>
          </div>
          <el-table v-loading="loadingStats" :data="stats">
            <el-table-column prop="stockCode" label="Code" width="120" />
            <el-table-column prop="buyOrders" label="Buy Orders" width="140" />
            <el-table-column prop="sellOrders" label="Sell Orders" width="140" />
            <el-table-column prop="buyQuantity" label="Buy Qty" width="140" />
            <el-table-column prop="sellQuantity" label="Sell Qty" width="140" />
          </el-table>
        </el-card>
      </el-tab-pane>
      <el-tab-pane label="Accounts" name="accounts">
        <el-card class="panel">
          <div class="panel-header">
            <div>
              <h2>Account Overview</h2>
              <p class="subtitle">Fund, securities, and holdings.</p>
            </div>
            <el-button type="primary" plain @click="loadAccounts">Refresh</el-button>
          </div>
          <h3>Fund Accounts</h3>
          <el-table v-loading="loadingAccounts" :data="accounts.fundAccounts" size="small">
            <el-table-column prop="accountNo" label="Account" width="200" />
            <el-table-column prop="userId" label="User" width="120" />
            <el-table-column prop="balance" label="Balance" width="140" />
            <el-table-column prop="frozenBalance" label="Frozen" width="140" />
            <el-table-column prop="status" label="Status" width="120" />
          </el-table>
          <h3 class="section">Securities Accounts</h3>
          <el-table v-loading="loadingAccounts" :data="accounts.securitiesAccounts" size="small">
            <el-table-column prop="accountNo" label="Account" width="200" />
            <el-table-column prop="userId" label="User" width="120" />
            <el-table-column prop="status" label="Status" width="120" />
          </el-table>
          <h3 class="section">Holdings</h3>
          <el-table v-loading="loadingAccounts" :data="accounts.holdings" size="small">
            <el-table-column prop="stockCode" label="Code" width="120" />
            <el-table-column prop="stockName" label="Name" />
            <el-table-column prop="quantity" label="Available" width="120" />
            <el-table-column prop="frozenQuantity" label="Frozen" width="120" />
          </el-table>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { ElMessage } from 'element-plus';
import {
  fetchAdminOrders,
  fetchAdminTrades,
  fetchAdminOrderStats,
  fetchAdminAccounts,
} from '../api/admin';

const activeTab = ref('orders');
const orderFilter = ref('');

const orders = ref([]);
const trades = ref([]);
const stats = ref([]);
const accounts = ref({ fundAccounts: [], securitiesAccounts: [], holdings: [] });

const loadingOrders = ref(false);
const loadingTrades = ref(false);
const loadingStats = ref(false);
const loadingAccounts = ref(false);

async function loadOrders() {
  loadingOrders.value = true;
  try {
    orders.value = await fetchAdminOrders(orderFilter.value.trim() || undefined);
  } catch (error) {
    ElMessage.error(error.message || 'Failed to load orders');
  } finally {
    loadingOrders.value = false;
  }
}

async function loadTrades() {
  loadingTrades.value = true;
  try {
    trades.value = await fetchAdminTrades();
  } catch (error) {
    ElMessage.error(error.message || 'Failed to load trades');
  } finally {
    loadingTrades.value = false;
  }
}

async function loadStats() {
  loadingStats.value = true;
  try {
    stats.value = await fetchAdminOrderStats();
  } catch (error) {
    ElMessage.error(error.message || 'Failed to load stats');
  } finally {
    loadingStats.value = false;
  }
}

async function loadAccounts() {
  loadingAccounts.value = true;
  try {
    accounts.value = await fetchAdminAccounts();
  } catch (error) {
    ElMessage.error(error.message || 'Failed to load accounts');
  } finally {
    loadingAccounts.value = false;
  }
}

onMounted(() => {
  loadOrders();
});
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
  gap: 12px;
  flex-wrap: wrap;
}

.subtitle {
  margin: 4px 0 0;
  color: rgba(31, 41, 55, 0.6);
}

.filter {
  display: flex;
  gap: 12px;
}

.section {
  margin-top: 20px;
}
</style>
