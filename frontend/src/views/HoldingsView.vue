<template>
  <section class="holdings-page">
    <el-card class="panel">
      <div class="panel-header">
        <div>
          <h2>Holdings</h2>
          <p class="subtitle">Securities account positions.</p>
        </div>
        <el-button type="primary" plain @click="loadData">Refresh</el-button>
      </div>
      <el-table v-loading="loading" :data="holdings">
        <el-table-column prop="stockCode" label="Code" width="140" />
        <el-table-column prop="stockName" label="Name" />
        <el-table-column prop="quantity" label="Available" width="140" />
        <el-table-column prop="frozenQuantity" label="Frozen" width="140" />
        <el-table-column prop="updatedAt" label="Updated" />
      </el-table>
    </el-card>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { fetchHoldings } from '../api/client';

const holdings = ref([]);
const loading = ref(false);

async function loadData() {
  loading.value = true;
  try {
    holdings.value = await fetchHoldings();
  } catch (error) {
    ElMessage.error(error.message || 'Failed to load holdings');
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
