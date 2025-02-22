<template>
    <div class="profile-container">
      <div class="profile-card">
        <div class="profile-header">
          <h1>Profile Information</h1>
        </div>
        
        <div class="profile-content">
          <div class="info-section">
            <div class="info-item">
              <label>Username:</label>
              <span>{{ username }}</span>
            </div>
            
            <div class="info-item">
              <label>Email:</label>
              <span>{{ email }}</span>
              <el-button v-if="!emailVerified" type="primary" size="small" @click="emailVerification">
                Verify Email
              </el-button>
            </div>
            
            <div class="info-item">
              <label>Phone:</label>
              <span>{{ phone }}</span>
            </div>
          </div>
  
          <div class="upload-section">
            <h2>ID Verification</h2>
            <p class="upload-description">Please upload a valid ID document.</p>
            
            <vue-dropzone ref="dropzone" id="dropzone" :options="dropzoneOptions" 
              @vdropzone-success="uploadSuccess" 
              @vdropzone-error="uploadError" 
            />
  
            <el-table v-if="documents.length" :data="documents" style="width: 100%">
              <el-table-column prop="fileName" label="File Name" />
              <el-table-column label="Upload Date">
                <template #default="{ row }">{{ formatDate(row.uploadTime) }}</template>
              </el-table-column>
              <el-table-column prop="status" label="Status">
                <template #default="{ row }">
                  <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import { auth, http } from '@/utils/request';
  import { ElMessage } from 'element-plus';
  import vue2Dropzone from 'vue2-dropzone';
  import 'vue2-dropzone/dist/vue2Dropzone.min.css';
  
  export default {
    components: { vueDropzone: vue2Dropzone },
    data() {
      return {
        username: auth.getUsername(),
        email: '',
        phone: '',
        documents: [],
        dropzoneOptions: {
          url: 'http://localhost:8080/upload',
          thumbnailWidth: 150,
          maxFilesize: 5,
          headers: { "Authorization": `Bearer ${auth.getToken()}` },
          acceptedFiles: 'image/*,.pdf',
        },
      };
    },
    computed: {
      emailVerified() {
        return this.email && this.email.includes('@verified.com');
      }
    },
    mounted() {
      this.fetchUserProfile();
      this.fetchDocuments();
    },
    methods: {
      async fetchUserProfile() {
        try {
          const { data } = await http.get('/user/profile');
          this.email = data.email;
          this.phone = data.phone;
        } catch (error) {
          ElMessage.error('Failed to fetch profile data');
        }
      },
      async emailVerification() {
        try {
          await http.post('/email/verify', { email: this.email });
          ElMessage.success('Verification email sent');
        } catch (error) {
          ElMessage.error('Failed to send verification email');
        }
      },
      async fetchDocuments() {
        try {
          const { data } = await http.get(`/documents/${this.username}`);
          this.documents = data.documents || [];
        } catch (error) {
          ElMessage.error('Failed to fetch documents');
        }
      },
      uploadSuccess(file, response) {
        ElMessage.success('Document uploaded successfully');
        this.fetchDocuments();
      },
      uploadError(file, message) {
        ElMessage.error(`Upload failed: ${message}`);
      },
      getStatusType(status) {
        return status === 'approved' ? 'success' : status === 'rejected' ? 'danger' : 'warning';
      },
      formatDate(date) {
        return date ? new Date(date).toLocaleString() : '';
      }
    }
  };
  </script>
  
  <style scoped>
  .profile-container {
    padding: 2rem;
    max-width: 1200px;
    margin: auto;
  }
  .profile-card {
    background: white;
    border-radius: 10px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  }
  .profile-header {
    background: #1B365D;
    color: white;
    padding: 1.5rem;
  }
  .profile-content {
    padding: 2rem;
  }
  .info-item {
    display: flex;
    align-items: center;
    margin-bottom: 1rem;
    border-bottom: 1px solid #eee;
  }
  .upload-section {
    background: #f8f9fa;
    padding: 2rem;
    border-radius: 8px;
  }
  </style>
  