<template>
  <div class="login-container">
    <div class="login-box">
      <h1>Login</h1>
      <form class="login-form">
        <input type="text" placeholder="Username" v-model="loginForm.username" />
        <input type="password" placeholder="Password" v-model="loginForm.password" />
        <button type="submit" @click.prevent="handleLogin">Login</button>
      </form>
    </div>
  </div>
</template>

<script>
import { http, auth } from '@/utils/request'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

export default {
  name: 'Login',
  setup() {
    const router = useRouter()
    const loginForm = {
      username: '',
      password: ''
    }

    const handleLogin = async () => {
      try {
        const response = await http.post('/login', {
          username: loginForm.username,
          password: loginForm.password
        })
        
        if (response) {
          auth.setUsername(response.username)
          auth.setToken(response.token)
          
          ElMessage.success('Login successful')
          
          // 先导航到order页面

          window.location.reload()
          await router.push('/order')
          
          // 然后刷新页面
          
        }
      } catch (error) {
        console.error('Login failed:', error)
        ElMessage.error('Login failed: ' + error.message)
      }
    }

    return {
      loginForm,
      handleLogin
    }
  }
}
</script>

<style scoped>
.login-container {
  height: 90vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #ffffff 100%);
}

.login-box {
  background: white;
  padding: 2rem;
  border-radius: 10px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}

h1 {
  color: #333;
  text-align: center;
  margin-bottom: 2rem;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

input {
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 16px;
  outline: none;
  transition: border-color 0.3s;
}

input:focus {
  border-color: #4a90e2;
}

button {
  background: #4a90e2;
  color: white;
  padding: 12px;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
  transition: background 0.3s;
}

button:hover {
  background: #357abd;
}
</style>
