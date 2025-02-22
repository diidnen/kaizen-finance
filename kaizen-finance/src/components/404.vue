<template>
  <div class="not-found">
    <div class="content">
      <h1>404</h1>
      <h2>The page you are looking for does not exist</h2>
      <p>Sorry, the page you are looking for does not exist or has been removed.</p>
      <div class="actions">
        <el-button type="primary" @click="goHome">go home</el-button>
        <el-button @click="goBack">go back</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'NotFound',
  methods: {
    goHome() {
      this.$router.push('/')
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.not-found {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f8f9fa;
}

.content {
  text-align: center;
  padding: 2rem;
}

h1 {
  font-size: 8rem;
  margin: 0;
  background: linear-gradient(45deg, #409eff, #67c23a);
  
  -webkit-text-fill-color: transparent;
  line-height: 1;
  color: white;
}

h2 {
  font-size: 2rem;
  color: #303133;
  margin: 1rem 0;
}

p {
  color: #606266;
  margin-bottom: 2rem;
}

.actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
}

/* 添加一些动画效果 */
@keyframes float {
  0% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-20px);
  }
  100% {
    transform: translateY(0px);
  }
}

h1 {
  animation: float 4s ease-in-out infinite;
}

/* 响应式设计 */
@media (max-width: 768px) {
  h1 {
    font-size: 6rem;
  }
  
  h2 {
    font-size: 1.5rem;
  }
  
  .actions {
    flex-direction: column;
  }
  
  .el-button {
    width: 100%;
  }
}
</style>
