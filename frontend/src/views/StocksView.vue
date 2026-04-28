<template>
  <section class="stocks-page">
    <el-row :gutter="24">
      <el-col :xs="24" :lg="14">
        <el-card class="panel">
          <div class="panel-header">
            <div>
              <h2>Market Watch</h2>
              <p class="subtitle">Browse stocks and latest prices.</p>
            </div>
            <div class="search">
              <el-input v-model="keyword" placeholder="Search by code or name" @keyup.enter="handleSearch" />
              <el-button type="primary" @click="handleSearch">Search</el-button>
            </div>
          </div>
          <el-table
            v-loading="loading"
            :data="stocks"
            highlight-current-row
            @row-click="handleSelect"
          >
            <el-table-column prop="code" label="Code" width="120" />
            <el-table-column prop="name" label="Name" />
            <el-table-column prop="latestPrice" label="Latest" width="140" />
            <el-table-column prop="status" label="Status" width="120" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="10">
        <el-card class="panel detail-panel">
          <div v-if="selected">
            <h3>{{ selected.name }} ({{ selected.code }})</h3>
            <div class="stats">
              <div class="stat">
                <span>Latest</span>
                <strong>{{ stats?.latestPrice ?? '--' }}</strong>
              </div>
              <div class="stat">
                <span>High</span>
                <strong>{{ stats?.highPrice ?? '--' }}</strong>
              </div>
              <div class="stat">
                <span>Low</span>
                <strong>{{ stats?.lowPrice ?? '--' }}</strong>
              </div>
              <div class="stat">
                <span>Volume</span>
                <strong>{{ stats?.totalVolume ?? '--' }}</strong>
              </div>
            </div>
            <div class="trade-list">
              <h4>Recent Trades</h4>
              <el-table :data="trades" size="small">
                <el-table-column prop="createdAt" label="Time" />
                <el-table-column prop="price" label="Price" width="120" />
                <el-table-column prop="quantity" label="Qty" width="100" />
              </el-table>
            </div>
          </div>
          <div v-else class="empty">Select a stock to view details.</div>
        </el-card>
      </el-col>
    </el-row>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { listStocks, searchStocks, getStockStats, getStockTrades } from '../api/stocks';

const stocks = ref([]);
const trades = ref([]);
const stats = ref(null);
const selected = ref(null);
const keyword = ref('');
const loading = ref(false);

async function loadStocks() {
  loading.value = true;
  try {
    stocks.value = await listStocks();
  } catch (error) {
    ElMessage.error(error.message || 'Failed to load stocks');
  } finally {
    loading.value = false;
  }
}

async function handleSearch() {
  loading.value = true;
  try {
    stocks.value = await searchStocks(keyword.value.trim());
  } catch (error) {
    ElMessage.error(error.message || 'Search failed');
  } finally {
    loading.value = false;
  }
}

async function handleSelect(row) {
  selected.value = row;
  try {
    [stats.value, trades.value] = await Promise.all([
      getStockStats(row.code),
      getStockTrades(row.code),
    ]);
  } catch (error) {
    ElMessage.error(error.message || 'Failed to load details');
  }
}

onMounted(loadStocks);
</script>

<style scoped>
.panel {
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.9);
  box-shadow: 0 20px 60px rgba(15, 23, 42, 0.12);
}

.panel-header {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.subtitle {
  margin: 4px 0 0;
  color: rgba(31, 41, 55, 0.6);
}

.search {
  display: flex;
  gap: 12px;
}

.detail-panel {
  min-height: 400px;
}

.stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
  gap: 12px;
  margin: 16px 0 24px;
}

.stat {
  padding: 12px;
  border-radius: 14px;
  background: rgba(248, 250, 252, 0.8);
}

.stat span {
  display: block;
  font-size: 0.75rem;
  color: rgba(31, 41, 55, 0.6);
}

.stat strong {
  font-size: 1.1rem;
}

.trade-list h4 {
  margin: 0 0 12px;
}

.empty {
  color: rgba(31, 41, 55, 0.6);
  padding: 32px 8px;
  text-align: center;
}
</style>
