<template>
  <div class="contract-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>{{ isManager ? 'Contracts Management' : 'My Contracts' }}</h1>
      <p>{{ isManager ? 'Upload and manage legal documents and contracts for all users' : 'Download your legal documents and contracts' }}</p>
    </div>

    <!-- 管理员上传区域 -->
    <div v-if="isManager" class="upload-section">
      <div class="section-header">
        <h2>Upload New Contract</h2>
      </div>
      
      <el-form :model="uploadForm" :rules="uploadRules" ref="uploadFormRef" label-width="120px">
        <el-form-item label="Title" prop="title">
          <el-input v-model="uploadForm.title" placeholder="Enter contract title" />
        </el-form-item>
        
        <el-form-item label="Description" prop="description">
          <el-input 
            v-model="uploadForm.description" 
            type="textarea" 
            :rows="3"
            placeholder="Enter contract description" 
          />
        </el-form-item>
        
        <el-form-item label="Category" prop="category">
          <el-select v-model="uploadForm.category" placeholder="Select category" style="width: 100%">
            <el-option label="Service Agreement" value="service" />
            <el-option label="Non-Disclosure Agreement" value="nda" />
            <el-option label="Employment Contract" value="employment" />
            <el-option label="Consulting Agreement" value="consulting" />
            <el-option label="Other" value="other" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="Target User" prop="username">
          <el-select 
            v-model="uploadForm.username" 
            placeholder="Select target user" 
            style="width: 100%"
            filterable
            remote
            :remote-method="searchUsers"
            :loading="usersLoading"
          >
            <el-option
              v-for="user in availableUsers"
              :key="user.id"
              :label="`${user.username} (${user.fullname || 'No Name'})`"
              :value="user.username"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="Contract File" prop="file">
          <el-upload
            ref="uploadRef"
            :auto-upload="false"
            :on-change="handleFileChange"
            :file-list="fileList"
            accept=".pdf,.doc,.docx"
            :limit="1"
          >
            <el-button type="primary">Select File</el-button>
            <template #tip>
              <div class="el-upload__tip">
                Only PDF, DOC, DOCX files are allowed. Max size: 10MB
              </div>
            </template>
          </el-upload>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="uploadContract" :loading="uploading">
            Upload Contract
          </el-button>
          <el-button @click="resetUploadForm">Reset</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 合同列表区域 -->
    <div class="contracts-section">
      <div class="section-header">
        <h2>{{ isManager ? 'All Contracts' : 'My Contracts' }}</h2>
        <div class="actions">
          <el-input
            v-model="searchQuery"
            :placeholder="isManager ? 'Search contracts...' : 'Search my contracts...'"
            prefix-icon="Search"
            clearable
            style="width: 300px; margin-right: 16px;"
          />
          <el-button type="primary" @click="refreshContracts">
            <el-icon><Refresh /></el-icon>
            Refresh
          </el-button>
        </div>
      </div>

      <el-table
        :data="filteredContracts"
        style="width: 100%"
        v-loading="loading"
        border
        stripe
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="Title" width="200" />
        <el-table-column prop="description" label="Description" width="300" />
        <el-table-column prop="username" label="Target User" width="120" />
        <el-table-column prop="category" label="Category" width="150">
          <template #default="scope">
            <el-tag :type="getCategoryType(scope.row.category)" size="small">
              {{ getCategoryLabel(scope.row.category) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="originalFileName" label="File Name" width="200" />
        <el-table-column prop="fileSize" label="Size" width="100">
          <template #default="scope">
            {{ formatFileSize(scope.row.fileSize) }}
          </template>
        </el-table-column>
        <el-table-column prop="uploadTime" label="Upload Time" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.uploadTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="isActive" label="Status" width="100">
          <template #default="scope">
            <el-tag
              :type="scope.row.isActive ? 'success' : 'danger'"
              size="small"
            >
              {{ scope.row.isActive ? 'Active' : 'Inactive' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="200" fixed="right">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              @click="downloadContract(scope.row)"
              :disabled="!scope.row.isActive"
            >
              <el-icon><Download /></el-icon>
              Download
            </el-button>
            
            <el-button
              v-if="isManager"
              type="warning"
              size="small"
              @click="toggleContractStatus(scope.row)"
            >
              <el-icon><Switch /></el-icon>
              {{ scope.row.isActive ? 'Deactivate' : 'Activate' }}
            </el-button>
            
            <el-button
              v-if="isManager"
              type="danger"
              size="small"
              @click="deleteContract(scope.row)"
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
          :total="filteredContracts.length"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Download, Switch, Delete } from '@element-plus/icons-vue'
import { auth } from '@/utils/request'
import http from '@/utils/request'
import { useRouter } from 'vue-router'

const router = useRouter()
const isManager = ref(false)
const contracts = ref([])
const loading = ref(false)
const uploading = ref(false)
const searchQuery = ref('')
const currentPage = ref(1)
const pageSize = ref(20)

// 上传表单
const uploadFormRef = ref()
const uploadRef = ref()
const fileList = ref([])

// 用户搜索相关
const availableUsers = ref([])
const usersLoading = ref(false)
const uploadForm = ref({
  title: '',
  description: '',
  category: '',
  username: '',
  file: null
})

// 上传验证规则
const uploadRules = {
  title: [
    { required: true, message: 'Please enter contract title', trigger: 'blur' }
  ],
  description: [
    { required: true, message: 'Please enter contract description', trigger: 'blur' }
  ],
  category: [
    { required: true, message: 'Please select category', trigger: 'change' }
  ],
  username: [
    { required: true, message: 'Please select target user', trigger: 'change' }
  ],
  file: [
    { required: true, message: 'Please select a file', trigger: 'change' }
  ]
}

// 检查用户权限
const checkPermission = async () => {
  try {
    const token = auth.getToken()
    if (!token) {
      ElMessage.error('Please login first')
      router.push('/login')
      return false
    }

    const response = await http.post('/api/verify-token', { token })
    if (response.code === 200) {
      isManager.value = response.username === 'chuhan'
      return true
    } else {
      ElMessage.error('Authentication failed')
      router.push('/login')
      return false
    }
  } catch (error) {
    console.error('Permission check failed:', error)
    ElMessage.error('Authentication failed')
    router.push('/login')
    return false
  }
}

// 获取合同列表
const fetchContracts = async () => {
  try {
    loading.value = true
    const endpoint = isManager.value ? '/api/contracts/admin' : '/api/contracts/user'
    const response = await http.get(endpoint)
    
    if (response.code === 200) {
      contracts.value = response.contracts
    } else {
      ElMessage.error(response.message || 'Failed to fetch contracts')
    }
  } catch (error) {
    console.error('Failed to fetch contracts:', error)
    ElMessage.error('Failed to fetch contracts')
  } finally {
    loading.value = false
  }
}

// 搜索用户
const searchUsers = async (query) => {
  if (!query || query.length < 2) {
    availableUsers.value = []
    return
  }
  
  try {
    usersLoading.value = true
    const response = await http.get('/api/manager/users')
    
    if (response.code === 200) {
      availableUsers.value = response.users.filter(user => 
        user.username.toLowerCase().includes(query.toLowerCase()) ||
        (user.fullname && user.fullname.toLowerCase().includes(query.toLowerCase()))
      )
    }
  } catch (error) {
    console.error('Failed to search users:', error)
  } finally {
    usersLoading.value = false
  }
}

// 刷新合同列表
const refreshContracts = () => {
  fetchContracts()
}

// 处理文件选择
const handleFileChange = (file) => {
  uploadForm.value.file = file.raw
}

// 上传合同
const uploadContract = async () => {
  try {
    await uploadFormRef.value.validate()
    
    if (!uploadForm.value.file) {
      ElMessage.error('Please select a file')
      return
    }
    
    uploading.value = true
    
    const formData = new FormData()
    formData.append('file', uploadForm.value.file)
    formData.append('title', uploadForm.value.title)
    formData.append('description', uploadForm.value.description)
    formData.append('category', uploadForm.value.category)
    formData.append('username', uploadForm.value.username)
    
    const response = await http.post('/api/contracts/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    
    if (response.code === 200) {
      ElMessage.success('Contract uploaded successfully')
      resetUploadForm()
      await fetchContracts()
    } else {
      ElMessage.error(response.message || 'Failed to upload contract')
    }
  } catch (error) {
    console.error('Failed to upload contract:', error)
    ElMessage.error('Failed to upload contract')
  } finally {
    uploading.value = false
  }
}

// 重置上传表单
const resetUploadForm = () => {
  uploadForm.value = {
    title: '',
    description: '',
    category: '',
    username: '',
    file: null
  }
  fileList.value = []
  uploadFormRef.value?.resetFields()
}

// 下载合同
const downloadContract = async (contract) => {
  try {
    const response = await fetch(`/api/contracts/download/${contract.id}`, {
      headers: {
        'Authorization': `Bearer ${auth.getToken()}`
      }
    })
    
    if (response.ok) {
      const blob = await response.blob()
      const url = window.URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = contract.originalFileName
      document.body.appendChild(a)
      a.click()
      window.URL.revokeObjectURL(url)
      document.body.removeChild(a)
      
      ElMessage.success('Download started')
    } else {
      ElMessage.error('Failed to download contract')
    }
  } catch (error) {
    console.error('Failed to download contract:', error)
    ElMessage.error('Failed to download contract')
  }
}

// 切换合同状态
const toggleContractStatus = async (contract) => {
  try {
    const newStatus = !contract.isActive
    const action = newStatus ? 'activate' : 'deactivate'
    
    await ElMessageBox.confirm(
      `Are you sure you want to ${action} this contract?`,
      'Warning',
      {
        confirmButtonText: 'Yes',
        cancelButtonText: 'No',
        type: 'warning',
      }
    )
    
    const response = await http.put(`/api/contracts/${contract.id}/status`, {
      isActive: newStatus
    })
    
    if (response.code === 200) {
      contract.isActive = newStatus
      ElMessage.success(`Contract ${action}d successfully`)
    } else {
      ElMessage.error(response.message || `Failed to ${action} contract`)
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to toggle contract status:', error)
      ElMessage.error('Failed to toggle contract status')
    }
  }
}

// 删除合同
const deleteContract = async (contract) => {
  try {
    await ElMessageBox.confirm(
      `Are you sure you want to delete this contract? This action cannot be undone.`,
      'Warning',
      {
        confirmButtonText: 'Yes',
        cancelButtonText: 'No',
        type: 'warning',
      }
    )
    
    const response = await http.delete(`/api/contracts/${contract.id}`)
    
    if (response.code === 200) {
      ElMessage.success('Contract deleted successfully')
      await fetchContracts()
    } else {
      ElMessage.error(response.message || 'Failed to delete contract')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to delete contract:', error)
      ElMessage.error('Failed to delete contract')
    }
  }
}

// 格式化文件大小
const formatFileSize = (bytes) => {
  if (!bytes) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return 'N/A'
  try {
    const date = new Date(dateString)
    return date.toLocaleString()
  } catch (error) {
    return dateString
  }
}

// 获取分类标签类型
const getCategoryType = (category) => {
  const types = {
    service: 'primary',
    nda: 'warning',
    employment: 'success',
    consulting: 'info',
    other: 'default'
  }
  return types[category] || 'default'
}

// 获取分类标签文本
const getCategoryLabel = (category) => {
  const labels = {
    service: 'Service Agreement',
    nda: 'Non-Disclosure',
    employment: 'Employment',
    consulting: 'Consulting',
    other: 'Other'
  }
  return labels[category] || category
}

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
}

const handleCurrentChange = (val) => {
  currentPage.value = val
}

// 计算属性
const filteredContracts = computed(() => {
  if (!searchQuery.value) return contracts.value
  
  const query = searchQuery.value.toLowerCase()
  return contracts.value.filter(contract => 
    contract.title.toLowerCase().includes(query) ||
    contract.description.toLowerCase().includes(query) ||
    contract.category.toLowerCase().includes(query) ||
    contract.username.toLowerCase().includes(query) ||
    contract.originalFileName.toLowerCase().includes(query)
  )
})

// 生命周期
onMounted(async () => {
  await checkPermission()
  await fetchContracts()
})
</script>

<style scoped>
.contract-container {
  min-height: 100vh;
  background: #f5f7fa;
  padding: 20px;
}

.page-header {
  background: white;
  padding: 24px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  text-align: center;
}

.page-header h1 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 28px;
}

.page-header p {
  margin: 0;
  color: #606266;
  font-size: 16px;
}

.upload-section {
  background: white;
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.contracts-section {
  background: white;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.section-header h2 {
  margin: 0;
  color: #303133;
  font-size: 20px;
}

.actions {
  display: flex;
  align-items: center;
}

.pagination-wrapper {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}

:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table th) {
  background-color: #fafafa;
  color: #606266;
  font-weight: 600;
}

:deep(.el-table td) {
  padding: 12px 0;
}

:deep(.el-tag) {
  border-radius: 4px;
}

:deep(.el-upload) {
  width: 100%;
}

:deep(.el-upload__tip) {
  margin-top: 8px;
  color: #909399;
  font-size: 12px;
}
</style> 