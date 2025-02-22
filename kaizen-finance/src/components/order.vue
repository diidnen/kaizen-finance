<template>
  <div class="app-wrapper">
    <div class="sidebar-container">
      <!-- ... existing sidebar code ... -->
    </div>

    <div class="main-container">
      <div class="service-cards">
        <div v-for="service in services" 
             :key="service.id" 
             class="service-card"
             @click="navigateToService(service.path)">
          <div class="card-icon">
            <el-icon>
              <component :is="icon[service.id - 1]" />
            </el-icon>
          </div>
          <div class="card-content">
            <h3>{{ service.title }}</h3>
            <p>{{ service.description }}</p>
            <div class="card-stats">
              <span>
                <i class="el-icon-time"></i>
                {{ service.count }} requested service
              </span>
              <span>
                <i class="el-icon-date"></i>
                {{ service.lastUpdate }}
              </span>
            </div>
          </div>
        </div>
      </div>
      
      <router-view></router-view>

      <!-- 订单列表 -->
      <div class="orders-section">
        <h2>Your Orders</h2>
        
        <!-- 订单表格 -->
        <el-table :data="orders" style="width: 100%">
          <el-table-column width="70">
            <template #default="scope">
              <el-button
                type="danger"
                size="small"
                icon="Delete"
                circle
                @click="handleDelete(scope.row)"
              />
            </template>
          </el-table-column>
          <el-table-column prop="serviceName" label="Service" />
          <el-table-column prop="price" label="Price" />
          <el-table-column prop="orderDate" label="Order Date" />
          <el-table-column prop="serviceTime" label="Service Time" />
          <el-table-column prop="status" label="Status">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>

        <!-- 总价 -->
        <div class="total-price">
          <h3>Discount Price: {{ totalPrice }}</h3>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import http from '../utils/request'
import { auth } from '../utils/request'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const services = ref([])
const orders = ref([])
const totalPrice = ref('£0.00')

const getIcon = (title) => {
  switch(title.toLowerCase()) {
    case "pricing":
      return ElementPlusIconsVue.Money;
    case "contract":
      return ElementPlusIconsVue.Document;
    case "training":
      return ElementPlusIconsVue.School;
    default:
      return ElementPlusIconsVue.Service;
  }
}

const initialization = async () => {
  try {
    const username = auth.getUsername()
    if (!username) {
      console.error('Username not found')
      return
    }

    const response = await http.post('/list', {
      username: username
    })

    if (response.code === 200) {
      console.log(response.data)
      services.value = response.data.map(service => ({
        id: service.id,
        title: service.title,
        description: service.description,
        icon: service.icon,
        count: service.count,
        lastUpdate: formatDate(service.lastUpdate),
        path: `/order/${service.path}`,
        username: service.username
      }))
    }
    
  } catch (error) {
    console.error('Failed to fetch services:', error)
  }
}

const navigateToService = (path) => {
  console.log(path)
  router.push(path)
}

const formatDate = (date) => {
  if (!date) return 'No update'
  return new Date(date).toLocaleDateString('zh-CN', {
    month: 'long',
    day: 'numeric'
  })
}

// 获取订单数据
const fetchOrders = async () => {
  try {
    const username = auth.getUsername()
    console.log('Username:', username)
    
    if (!username) {
      console.error('No username found')
      ElMessage.error('Please login first')
      return
    }

    const url = `/ordersfind`
    console.log('Making request to:', url)
    
    const response = await http.post(url, { username })
    console.log('Raw response:', response)
    
    if (response && response.code === 200) {
      orders.value = response.orders || []
      totalPrice.value = response.totalPrice || '£0.00'
      console.log('Updated orders:', orders.value)
      console.log('Updated total price:', totalPrice.value)
    } else {
      console.error('Unexpected response:', response)
      ElMessage.error('Failed to load orders: unexpected response')
    }
  } catch (error) {
    console.error('Failed to fetch orders:', error)
    console.error('Error details:', {
      message: error.message,
      response: error.response,
      config: error.config
    })
    ElMessage.error(`Failed to load orders: ${error.message}`)
  }
}

// 获取状态标签类型
const getStatusType = (status) => {
  switch (status.toLowerCase()) {
    case 'pending':
      return 'warning'
    case 'approved':
      return 'success'
    case 'rejected':
      return 'danger'
    default:
      return 'info'
  }
}

// 添加删除处理函数
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      'Are you sure you want to delete this order?',
      'Warning',
      {
        confirmButtonText: 'Yes',
        cancelButtonText: 'No',
        type: 'warning',
      }
    )
    
    console.log('Deleting order with ID:', row.id)
    const response = await http.delete(`/orders/${row.id}`)
    
    if (response && response.code === 200) {
      ElMessage.success('Order deleted successfully')
      await fetchOrders()
    } else {
      ElMessage.error(response?.message || 'Failed to delete order')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Error deleting order:', error)
      ElMessage.error(`Failed to delete order: ${error.message}`)
    }
  }
}

onMounted(() => {
  initialization()
  fetchOrders()
})

// 修改 icon 的设置
const icon = computed(() => {
  return services.value.map(item => getIcon(item.title))
})
</script>

<style scoped>
/* ... existing styles ... */

.service-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
  padding: 20px;
}

.service-card {
  background: #ffffff;
  border-radius: 8px;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  display: flex;
  align-items: flex-start;
}

.service-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.1);
}

.card-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: #ecf5ff;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
}

.card-icon i {
  font-size: 24px;
  color: #409eff;
}

.card-content {
  flex: 1;
}

.card-content h3 {
  margin: 0 0 8px 0;
  font-size: 16px;
  color: #303133;
}

.card-content p {
  margin: 0 0 16px 0;
  font-size: 14px;
  color: #606266;
  line-height: 1.5;
}

.card-stats {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #909399;
}

.card-stats span {
  display: flex;
  align-items: center;
}

.card-stats i {
  margin-right: 4px;
}

.orders-section {
  padding: 20px;
}

.total-price {
  margin-top: 20px;
  text-align: right;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 4px;
}

.total-price h3 {
  color: #1B365D;
  font-size: 1.2rem;
}

.el-button.is-circle {
  margin-right: 8px;
}
</style>