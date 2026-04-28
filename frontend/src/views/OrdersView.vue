<template>
  <section class="orders-page">
    <el-card class="panel">
      <div class="panel-header">
        <div>
          <h2>Order History</h2>
          <p class="subtitle">Track your submitted orders.</p>
        </div>
        <el-button type="primary" plain @click="loadData">Refresh</el-button>
      </div>
      <el-table v-loading="loading" :data="orders">
        <el-table-column prop="orderNo" label="Order No" width="200" />
        <el-table-column prop="stockCode" label="Code" width="120" />
        <el-table-column prop="direction" label="Side" width="120" />
        <el-table-column prop="price" label="Price" width="120" />
        <el-table-column prop="quantity" label="Qty" width="100" />
        <el-table-column prop="filledQuantity" label="Filled" width="100" />
        <el-table-column prop="remainingQuantity" label="Remaining" width="120" />
        <el-table-column prop="status" label="Status" width="140" />
        <el-table-column prop="createdAt" label="Time" />
      </el-table>
    </el-card>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { fetchOrders } from '../api/client';

const orders = ref([]);
const loading = ref(false);

async function loadData() {
  loading.value = true;
  try {
    orders.value = await fetchOrders();
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
</style>
