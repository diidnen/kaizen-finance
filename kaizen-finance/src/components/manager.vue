<template>
  <div class="manager-container">
    <div class="header">
      <h1>Manager Dashboard</h1>
      <div class="user-info">
        <span>Welcome, {{ username }}</span>
        <el-button type="danger" @click="logout">Logout</el-button>
      </div>
    </div>

    <div class="content-section">
      <div class="section-header">
        <h2>User Management</h2>
        <div class="actions">
          <el-input
            v-model="searchQuery"
            placeholder="Search users..."
            :prefix-icon="Search"
            clearable
            style="width: 300px; margin-right: 16px;"
          />
          <el-button type="success" @click="showCreateUserDialog = true">
            <el-icon><Plus /></el-icon>
            Add User
          </el-button>
          <el-button type="primary" @click="refreshUsers">
            <el-icon><Refresh /></el-icon>
            Refresh
          </el-button>
        </div>
      </div>

      <el-table :data="paginatedUsers" style="width: 100%" v-loading="loading" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="Username" width="150" />
        <el-table-column prop="password" label="Password" width="180">
          <template #default="scope">
            <div class="password-cell">
              <span>{{ scope.row.showPassword ? scope.row.password : '••••••••' }}</span>
              <el-button type="text" @click="togglePassword(scope.row)">
                <el-icon>
                  <View v-if="!scope.row.showPassword" />
                  <Hide v-else />
                </el-icon>
              </el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="email" label="Email" width="250" />
        <el-table-column prop="fullname" label="Full Name" width="200" />
        <el-table-column prop="createdAt" label="Created At" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="250" fixed="right">
          <template #default="scope">
            <el-button
              type="warning"
              size="small"
              @click="resetPassword(scope.row)"
              :disabled="scope.row.username === 'chuhan'"
            >
              Reset Password
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click="deleteUser(scope.row)"
              :disabled="scope.row.username === 'chuhan'"
            >
              <el-icon><Delete /></el-icon>
              Delete
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="filteredUsers.length"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <div class="content-section">
      <div class="section-header">
        <h2>Testimonials Management</h2>
        <div class="actions">
          <el-button type="success" @click="showCreateTestimonialDialog = true">
            <el-icon><Plus /></el-icon>
            Add Testimonial
          </el-button>
          <el-button type="primary" @click="refreshTestimonials">
            <el-icon><Refresh /></el-icon>
            Refresh
          </el-button>
        </div>
      </div>

      <el-table :data="testimonials" style="width: 100%" v-loading="testimonialsLoading" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="customerName" label="Customer" width="150" />
        <el-table-column prop="companyName" label="Company" width="150" />
        <el-table-column prop="testimonial" label="Testimonial" width="300">
          <template #default="scope">
            <div class="testimonial-preview">
              {{ scope.row.testimonial.length > 100 ? scope.row.testimonial.substring(0, 100) + '...' : scope.row.testimonial }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="displayOrder" label="Order" width="80" />
        <el-table-column prop="isActive" label="Status" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.isActive ? 'success' : 'danger'" size="small">
              {{ scope.row.isActive ? 'Active' : 'Inactive' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="editTestimonial(scope.row)">
              <el-icon><Edit /></el-icon>
              Edit
            </el-button>
            <el-button type="danger" size="small" @click="deleteTestimonial(scope.row)">
              <el-icon><Delete /></el-icon>
              Delete
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="content-section">
      <div class="section-header">
        <h2>Order Management</h2>
        <div class="actions">
          <el-input
            v-model="orderSearchQuery"
            placeholder="Search orders..."
            :prefix-icon="Search"
            clearable
            style="width: 300px; margin-right: 16px;"
          />
          <el-button type="primary" @click="refreshOrders">
            <el-icon><Refresh /></el-icon>
            Refresh
          </el-button>
        </div>
      </div>

      <el-table :data="paginatedOrders" style="width: 100%" v-loading="ordersLoading" border stripe>
        <el-table-column prop="id" label="Order ID" width="80" />
        <el-table-column prop="username" label="Username" width="120" />
        <el-table-column prop="serviceName" label="Service" width="200" />
        <el-table-column prop="price" label="Price" width="150">
          <template #default="scope">
            <div class="inline-edit-cell">
              <span v-if="!scope.row.editingPrice">{{ scope.row.price }}</span>
              <el-input
                v-else
                v-model="scope.row.newPrice"
                size="small"
                style="width: 100px;"
                @keyup.enter="savePrice(scope.row)"
                @blur="cancelEditPrice(scope.row)"
              />
              <el-button type="text" size="small" @click="editPrice(scope.row)" v-if="!scope.row.editingPrice">
                <el-icon><Edit /></el-icon>
              </el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="orderDate" label="Order Date" width="180" />
        <el-table-column prop="serviceTime" label="Service Time" width="180" />
        <el-table-column prop="status" label="Status" width="150">
          <template #default="scope">
            <div class="inline-edit-cell">
              <el-tag v-if="!scope.row.editingStatus" :type="getStatusType(scope.row.status)" size="small">
                {{ getStatusLabel(scope.row.status) }}
              </el-tag>
              <el-select
                v-else
                v-model="scope.row.newStatus"
                size="small"
                style="width: 120px;"
                @change="saveStatus(scope.row)"
                @blur="cancelEditStatus(scope.row)"
              >
                <el-option label="Pending" value="pending" />
                <el-option label="Approved" value="approved" />
                <el-option label="Rejected" value="rejected" />
                <el-option label="Completed" value="completed" />
              </el-select>
              <el-button type="text" size="small" @click="editStatus(scope.row)" v-if="!scope.row.editingStatus">
                <el-icon><Edit /></el-icon>
              </el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="isEnquiry" label="Type" width="100" fixed="right">
          <template #default="scope">
            <el-tag :type="scope.row.isEnquiry ? 'warning' : 'success'" size="small">
              {{ scope.row.isEnquiry ? 'Enquiry' : 'Service' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="orderCurrentPage"
          v-model:page-size="orderPageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="filteredOrders.length"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleOrderSizeChange"
          @current-change="handleOrderCurrentChange"
        />
      </div>
    </div>

    <el-dialog v-model="showCreateUserDialog" title="Create New User" width="500px" :before-close="handleCloseCreateDialog">
      <el-form :model="createUserForm" :rules="createUserRules" ref="createUserFormRef" label-width="100px">
        <el-form-item label="Username" prop="username">
          <el-input v-model="createUserForm.username" placeholder="Enter username" />
        </el-form-item>
        <el-form-item label="Password" prop="password">
          <el-input v-model="createUserForm.password" type="password" placeholder="Enter password" show-password />
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input v-model="createUserForm.email" placeholder="Enter email" />
        </el-form-item>
        <el-form-item label="Full Name" prop="fullname">
          <el-input v-model="createUserForm.fullname" placeholder="Enter full name" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleCloseCreateDialog">Cancel</el-button>
          <el-button type="primary" @click="createUser" :loading="creatingUser">Create User</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="showCreateTestimonialDialog" title="Add New Testimonial" width="600px" :before-close="handleCloseCreateTestimonialDialog">
      <el-form :model="createTestimonialForm" :rules="testimonialRules" ref="createTestimonialFormRef" label-width="120px">
        <el-form-item label="Customer Name" prop="customerName">
          <el-input v-model="createTestimonialForm.customerName" placeholder="Enter customer name" />
        </el-form-item>
        <el-form-item label="Company Name" prop="companyName">
          <el-input v-model="createTestimonialForm.companyName" placeholder="Enter company name (optional)" />
        </el-form-item>
        <el-form-item label="Testimonial" prop="testimonial">
          <el-input v-model="createTestimonialForm.testimonial" type="textarea" :rows="4" placeholder="Enter testimonial text" />
        </el-form-item>
        <el-form-item label="Display Order" prop="displayOrder">
          <el-input-number v-model="createTestimonialForm.displayOrder" :min="1" />
        </el-form-item>
        <el-form-item label="Status" prop="isActive">
          <el-switch v-model="createTestimonialForm.isActive" active-text="Active" inactive-text="Inactive" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleCloseCreateTestimonialDialog">Cancel</el-button>
          <el-button type="primary" @click="createTestimonial" :loading="creatingTestimonial">Create</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="showEditTestimonialDialog" title="Edit Testimonial" width="600px" :before-close="handleCloseEditTestimonialDialog">
      <el-form :model="editTestimonialForm" :rules="testimonialRules" ref="editTestimonialFormRef" label-width="120px">
        <el-form-item label="Customer Name" prop="customerName">
          <el-input v-model="editTestimonialForm.customerName" placeholder="Enter customer name" />
        </el-form-item>
        <el-form-item label="Company Name" prop="companyName">
          <el-input v-model="editTestimonialForm.companyName" placeholder="Enter company name (optional)" />
        </el-form-item>
        <el-form-item label="Testimonial" prop="testimonial">
          <el-input v-model="editTestimonialForm.testimonial" type="textarea" :rows="4" placeholder="Enter testimonial text" />
        </el-form-item>
        <el-form-item label="Display Order" prop="displayOrder">
          <el-input-number v-model="editTestimonialForm.displayOrder" :min="1" />
        </el-form-item>
        <el-form-item label="Status" prop="isActive">
          <el-switch v-model="editTestimonialForm.isActive" active-text="Active" inactive-text="Inactive" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleCloseEditTestimonialDialog">Cancel</el-button>
          <el-button type="primary" @click="updateTestimonial" :loading="editingTestimonial">Save Changes</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, View, Hide, Edit, Plus, Delete } from '@element-plus/icons-vue'
import { auth } from '@/utils/request'
import http from '@/utils/request'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = ref('')

// --- User Management State ---
const users = ref([])
const loading = ref(false)
const searchQuery = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const showCreateUserDialog = ref(false)
const creatingUser = ref(false)
const createUserFormRef = ref()
const createUserForm = ref({
  username: '',
  password: '',
  email: '',
  fullname: ''
})

// --- Testimonials Management State ---
const testimonials = ref([])
const testimonialsLoading = ref(false)
const showCreateTestimonialDialog = ref(false)
const showEditTestimonialDialog = ref(false)
const creatingTestimonial = ref(false)
const editingTestimonial = ref(false)
const createTestimonialFormRef = ref()
const editTestimonialFormRef = ref()
const createTestimonialForm = ref({
  customerName: '',
  companyName: '',
  testimonial: '',
  displayOrder: 100,
  isActive: true
})
const editTestimonialForm = ref({
  id: null,
  customerName: '',
  companyName: '',
  testimonial: '',
  displayOrder: 100,
  isActive: true
})

// --- Order Management State ---
const orders = ref([])
const ordersLoading = ref(false)
const orderSearchQuery = ref('')
const orderCurrentPage = ref(1)
const orderPageSize = ref(10)

// --- Permission & Auth ---
const checkPermission = async () => {
  try {
    const token = auth.getToken()
    if (!token) {
      ElMessage.error('Please login first')
      router.push('/login')
      return false
    }

    const response = await http.post('/api/verify-token', { token })
    if (response.code !== 200) {
      ElMessage.error('Authentication failed')
      router.push('/login')
      return false
    }

    if (response.username !== 'chuhan') {
      ElMessage.error('Access denied. Only managers can access this page.')
      router.push('/home')
      return false
    }

    username.value = response.username
    return true
  } catch (error) {
    console.error('Permission check failed:', error)
    ElMessage.error('Authentication failed')
    router.push('/login')
    return false
  }
}

const logout = () => {
  auth.removeToken()
  auth.removeUsername()
  router.push('/login')
}

// --- User Management Functions ---
const fetchUsers = async () => {
  try {
    loading.value = true
    const response = await http.get('/api/manager/users')
    if (response.code === 200) {
      users.value = response.users.map(user => ({ ...user, showPassword: false }))
    } else {
      ElMessage.error(response.message || 'Failed to fetch users')
    }
  } catch (error) {
    console.error('Failed to fetch users:', error)
    ElMessage.error('Failed to fetch users')
  } finally {
    loading.value = false
  }
}

const refreshUsers = () => fetchUsers()

const createUserRules = {
  username: [
    { required: true, message: 'Please enter username', trigger: 'blur' },
    { min: 3, max: 20, message: 'Username must be between 3 and 20 characters', trigger: 'blur' }
  ],
  password: [
    { required: true, message: 'Please enter password', trigger: 'blur' },
    { min: 6, max: 20, message: 'Password must be between 6 and 20 characters', trigger: 'blur' }
  ],
  email: [
    { required: true, message: 'Please enter email', trigger: 'blur' },
    { type: 'email', message: 'Please enter a valid email address', trigger: 'blur' }
  ],
  fullname: [{ required: true, message: 'Please enter full name', trigger: 'blur' }]
}

const handleCloseCreateDialog = () => {
  showCreateUserDialog.value = false
  createUserFormRef.value?.resetFields()
}

const createUser = async () => {
  try {
    const isValid = await createUserFormRef.value.validate()
    if (!isValid) return

    creatingUser.value = true
    const response = await http.post('/api/manager/users', createUserForm.value)
    if (response.code === 200) {
      ElMessage.success('User created successfully')
      handleCloseCreateDialog()
      await fetchUsers()
    } else {
      ElMessage.error(response.message || 'Failed to create user')
    }
  } catch (error) {
    // Validation error is caught here but not an API error
  } finally {
    creatingUser.value = false
  }
}

const togglePassword = (user) => {
  user.showPassword = !user.showPassword
}

const deleteUser = async (user) => {
  if (user.username === 'chuhan') {
    ElMessage.warning('Cannot delete manager account')
    return
  }
  try {
    await ElMessageBox.confirm(`Are you sure you want to delete user "${user.username}"?`, 'Warning', {
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
      type: 'warning',
    })

    const response = await http.delete(`/api/manager/users/${user.id}`)
    if (response.code === 200) {
      ElMessage.success('User deleted successfully')
      await fetchUsers()
    } else {
      ElMessage.error(response.message || 'Failed to delete user')
    }
  } catch (error) {
    if (error !== 'cancel') ElMessage.info('Delete canceled')
  }
}

const resetPassword = async (user) => {
  if (user.username === 'chuhan') {
    ElMessage.warning('Cannot reset manager password')
    return
  }
  try {
    await ElMessageBox.confirm(`Are you sure you want to reset password for user "${user.username}"?`, 'Warning', {
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
      type: 'warning',
    })

    const response = await http.post(`/api/manager/users/${user.id}/reset-password`)
    if (response.code === 200) {
      ElMessage.success('Password reset successfully')
      await fetchUsers()
    } else {
      ElMessage.error(response.message || 'Failed to reset password')
    }
  } catch (error) {
    if (error !== 'cancel') ElMessage.info('Reset password canceled')
  }
}

// --- Testimonials Management Functions ---
const fetchTestimonials = async () => {
  try {
    testimonialsLoading.value = true
    const response = await http.get('/api/manager/testimonials')
    if (response.code === 200) {
      testimonials.value = response.testimonials
    } else {
      ElMessage.error(response.message || 'Failed to fetch testimonials')
    }
  } catch (error) {
    console.error('Failed to fetch testimonials:', error)
    ElMessage.error('Failed to fetch testimonials')
  } finally {
    testimonialsLoading.value = false
  }
}

const refreshTestimonials = () => fetchTestimonials()

const testimonialRules = {
  customerName: [{ required: true, message: 'Please enter customer name', trigger: 'blur' }],
  testimonial: [{ required: true, message: 'Please enter testimonial text', trigger: 'blur' }],
  displayOrder: [{ required: true, message: 'Please enter display order', trigger: 'blur' }, { type: 'number', message: 'Display order must be a number' }]
}

const handleCloseCreateTestimonialDialog = () => {
  showCreateTestimonialDialog.value = false
  createTestimonialFormRef.value?.resetFields()
}

const createTestimonial = async () => {
  try {
    await createTestimonialFormRef.value.validate()
    creatingTestimonial.value = true
    const response = await http.post('/api/manager/testimonials', createTestimonialForm.value)
    if (response.code === 200) {
      ElMessage.success('Testimonial created successfully')
      handleCloseCreateTestimonialDialog()
      await fetchTestimonials()
    } else {
      ElMessage.error(response.message || 'Failed to create testimonial')
    }
  } catch (error) {
    // Validation error
  } finally {
    creatingTestimonial.value = false
  }
}

const editTestimonial = (testimonial) => {
  editTestimonialForm.value = { ...testimonial }
  showEditTestimonialDialog.value = true
}

const handleCloseEditTestimonialDialog = () => {
  showEditTestimonialDialog.value = false
}

const updateTestimonial = async () => {
  try {
    await editTestimonialFormRef.value.validate()
    editingTestimonial.value = true
    const { id, ...data } = editTestimonialForm.value
    const response = await http.put(`/api/manager/testimonials/${id}`, data)
    if (response.code === 200) {
      ElMessage.success('Testimonial updated successfully')
      handleCloseEditTestimonialDialog()
      await fetchTestimonials()
    } else {
      ElMessage.error(response.message || 'Failed to update testimonial')
    }
  } catch (error) {
    // Validation error
  } finally {
    editingTestimonial.value = false
  }
}

const deleteTestimonial = async (testimonial) => {
  try {
    await ElMessageBox.confirm(`Are you sure you want to delete the testimonial from "${testimonial.customerName}"?`, 'Warning', {
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
      type: 'warning',
    })
    const response = await http.delete(`/api/manager/testimonials/${testimonial.id}`)
    if (response.code === 200) {
      ElMessage.success('Testimonial deleted successfully')
      await fetchTestimonials()
    } else {
      ElMessage.error(response.message || 'Failed to delete testimonial')
    }
  } catch (error) {
    if (error !== 'cancel') ElMessage.info('Delete canceled')
  }
}

// --- Order Management Functions ---
const fetchOrders = async () => {
  try {
    ordersLoading.value = true
    const response = await http.get('/api/manager/orders')
    if (response.code === 200) {
      orders.value = response.orders.map(order => ({
        ...order,
        editingPrice: false,
        newPrice: order.price,
        editingStatus: false,
        newStatus: order.status
      }))
    } else {
      ElMessage.error(response.message || 'Failed to fetch orders')
    }
  } catch (error) {
    console.error('Failed to fetch orders:', error)
    ElMessage.error('Failed to fetch orders')
  } finally {
    ordersLoading.value = false
  }
}

const refreshOrders = () => fetchOrders()

const editPrice = (order) => {
  order.editingPrice = true
  order.newPrice = order.price
}

const savePrice = async (order) => {
  try {
    const response = await http.put(`/api/manager/orders/${order.id}/price`, { price: order.newPrice })
    if (response.code === 200) {
      order.price = order.newPrice
      order.editingPrice = false
      ElMessage.success('Price updated successfully')
    } else {
      ElMessage.error(response.message || 'Failed to update price')
    }
  } catch (error) {
    ElMessage.error('Failed to update price')
  }
}

const cancelEditPrice = (order) => {
  order.editingPrice = false
}

const editStatus = (order) => {
  order.editingStatus = true
  order.newStatus = order.status
}

const saveStatus = async (order) => {
  try {
    const response = await http.put(`/api/manager/orders/${order.id}/status`, { status: order.newStatus })
    if (response.code === 200) {
      order.status = order.newStatus
      order.editingStatus = false
      ElMessage.success('Order status updated successfully')
    } else {
      ElMessage.error(response.message || 'Failed to update order status')
    }
  } catch (error) {
    ElMessage.error('Failed to update order status')
  }
}

const cancelEditStatus = (order) => {
  order.editingStatus = false
}

// --- Utility & Computed ---
const formatDate = (dateString) => {
  if (!dateString) return 'N/A'
  try {
    return new Date(dateString).toLocaleString()
  } catch (error) {
    return dateString
  }
}

const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
}

const handleCurrentChange = (val) => {
  currentPage.value = val
}

const handleOrderSizeChange = (val) => {
  orderPageSize.value = val
  orderCurrentPage.value = 1
}

const handleOrderCurrentChange = (val) => {
  orderCurrentPage.value = val
}

const filteredUsers = computed(() => {
  if (!searchQuery.value) return users.value
  const query = searchQuery.value.toLowerCase()
  return users.value.filter(user =>
    user.username.toLowerCase().includes(query) ||
    user.email.toLowerCase().includes(query) ||
    (user.fullname && user.fullname.toLowerCase().includes(query))
  )
})

const paginatedUsers = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredUsers.value.slice(start, end)
})

const filteredOrders = computed(() => {
  if (!orderSearchQuery.value) return orders.value
  const query = orderSearchQuery.value.toLowerCase()
  return orders.value.filter(order =>
    order.username.toLowerCase().includes(query) ||
    order.serviceName.toLowerCase().includes(query) ||
    String(order.id).includes(query)
  )
})

const paginatedOrders = computed(() => {
  const start = (orderCurrentPage.value - 1) * orderPageSize.value
  const end = start + orderPageSize.value
  return filteredOrders.value.slice(start, end)
})

const getStatusType = (status) => {
  switch (status?.toLowerCase()) {
    case 'pending': return 'warning'
    case 'approved': return 'primary'
    case 'completed': return 'success'
    case 'rejected': return 'danger'
    default: return 'info'
  }
}

const getStatusLabel = (status) => {
  if (!status) return 'Unknown'
  return status.charAt(0).toUpperCase() + status.slice(1)
}

// --- Lifecycle Hook ---
onMounted(async () => {
  const hasPermission = await checkPermission()
  if (hasPermission) {
    await Promise.all([
      fetchUsers(),
      fetchTestimonials(),
      fetchOrders()
    ])
  }
})
</script>

<style scoped>
.manager-container {
  padding: 24px;
  background-color: #f5f7fa;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  background-color: #ffffff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.header h1 {
  font-size: 24px;
  color: #303133;
  margin: 0;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.content-section {
  background-color: #ffffff;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin-bottom: 24px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h2 {
  font-size: 20px;
  color: #303133;
  margin: 0;
}

.actions {
  display: flex;
  align-items: center;
}

.password-cell, .inline-edit-cell {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.testimonial-preview {
  white-space: normal;
  word-break: break-word;
}
</style>