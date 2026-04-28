<template>
  <section class="trade-page">
    <el-row :gutter="24">
      <el-col :xs="24" :lg="12">
        <el-card class="panel">
          <h2>Buy Order</h2>
          <el-form :model="buyForm" label-position="top">
            <el-form-item label="Stock Code">
              <el-input v-model="buyForm.stockCode" />
            </el-form-item>
            <el-form-item label="Price">
              <el-input-number v-model="buyForm.price" :min="0.01" :step="0.01" />
            </el-form-item>
            <el-form-item label="Quantity">
              <el-input-number v-model="buyForm.quantity" :min="1" :step="1" />
            </el-form-item>
            <el-button type="primary" :loading="loading" @click="submitBuy">Submit Buy</el-button>
          </el-form>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card class="panel">
          <h2>Sell Order</h2>
          <el-form :model="sellForm" label-position="top">
            <el-form-item label="Stock Code">
              <el-input v-model="sellForm.stockCode" />
            </el-form-item>
            <el-form-item label="Price">
              <el-input-number v-model="sellForm.price" :min="0.01" :step="0.01" />
            </el-form-item>
            <el-form-item label="Quantity">
              <el-input-number v-model="sellForm.quantity" :min="1" :step="1" />
            </el-form-item>
            <el-button type="primary" :loading="loading" @click="submitSell">Submit Sell</el-button>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
    <el-card v-if="lastOrder" class="panel result-panel">
      <h3>Order Submitted</h3>
      <div class="order-grid">
        <div>
          <span>Order No</span>
          <strong>{{ lastOrder.orderNo }}</strong>
        </div>
        <div>
          <span>Stock</span>
          <strong>{{ lastOrder.stockCode }}</strong>
        </div>
        <div>
          <span>Direction</span>
          <strong>{{ lastOrder.direction }}</strong>
        </div>
        <div>
          <span>Status</span>
          <strong>{{ lastOrder.status }}</strong>
        </div>
      </div>
    </el-card>
  </section>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { buyOrder, sellOrder } from '../api/orders';

const loading = ref(false);
const lastOrder = ref(null);

const buyForm = reactive({
  stockCode: '',
  price: 0,
  quantity: 1,
});

const sellForm = reactive({
  stockCode: '',
  price: 0,
  quantity: 1,
});

async function submitBuy() {
  if (!validateOrder(buyForm)) {
    return;
  }
  loading.value = true;
  try {
    lastOrder.value = await buyOrder({
      stockCode: buyForm.stockCode,
      price: Number(buyForm.price),
      quantity: Number(buyForm.quantity),
    });
    ElMessage.success('Buy order submitted');
  } catch (error) {
    ElMessage.error(error.message || 'Buy order failed');
  } finally {
    loading.value = false;
  }
}

async function submitSell() {
  if (!validateOrder(sellForm)) {
    return;
  }
  loading.value = true;
  try {
    lastOrder.value = await sellOrder({
      stockCode: sellForm.stockCode,
      price: Number(sellForm.price),
      quantity: Number(sellForm.quantity),
    });
    ElMessage.success('Sell order submitted');
  } catch (error) {
    ElMessage.error(error.message || 'Sell order failed');
  } finally {
    loading.value = false;
  }
}

function validateOrder(form) {
  if (!form.stockCode || !form.stockCode.trim()) {
    ElMessage.warning('Stock code is required');
    return false;
  }
  if (!form.price || Number(form.price) <= 0) {
    ElMessage.warning('Price must be greater than 0');
    return false;
  }
  if (!form.quantity || Number(form.quantity) <= 0) {
    ElMessage.warning('Quantity must be greater than 0');
    return false;
  }
  return true;
}
</script>

<style scoped>
.panel {
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.9);
  box-shadow: 0 20px 60px rgba(15, 23, 42, 0.12);
  margin-bottom: 24px;
}

.result-panel {
  max-width: 720px;
  margin: 24px auto 0;
}

.order-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
  gap: 16px;
  margin-top: 16px;
}

.order-grid span {
  display: block;
  font-size: 0.75rem;
  color: rgba(31, 41, 55, 0.6);
}
</style>
