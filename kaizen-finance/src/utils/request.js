import axios from 'axios'
import router from '@/router'
import {ElMessage} from 'element-plus'


export const TokenKey='access_Token'

// 从环境变量读取 API 基础 URL，如果没有则使用相对路径（开发环境使用 Vite proxy）
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api'

const service = axios.create({
  baseURL: API_BASE_URL,  // 支持环境变量配置，生产环境使用完整 URL，开发环境使用相对路径
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const auth= {
  setToken(token) {
    console.log('Setting token:', token)  // 添加日志
    localStorage.setItem(TokenKey, token)
  },
  
  getToken() {
    const token = localStorage.getItem(TokenKey)
    console.log('Getting token:', token)  // 添加日志
    return token
  },
  
  removeToken() {
    localStorage.removeItem(TokenKey)
  },

  getUsername() {
    return localStorage.getItem('username')
  },

  setUsername(username) {
    localStorage.setItem('username', username)
  },

  removeUsername() {
    localStorage.removeItem('username')
  }
}
service.interceptors.request.use(config => {
    const token=auth.getToken()
    if(token){
        config.headers['Authorization']=`Bearer ${token}`
    }
    return config
},
error=>{
    ElMessage.error('Request error')
    return Promise.reject(error)//中断promise请求
}
)

service.interceptors.response.use(
  response => {
    // 直接返回数据部分
    return response.data
  },
  error => {
    console.error('Request error:', error)
    // 如果后端返回了响应数据（包括401等错误响应），返回它以便调用者可以处理
    if (error.response && error.response.data) {
      // 如果响应数据是对象（JSON），直接返回
      if (typeof error.response.data === 'object') {
        return Promise.reject(error.response.data)
      }
      // 如果是字符串，尝试解析
      try {
        const parsed = JSON.parse(error.response.data)
        return Promise.reject(parsed)
      } catch (e) {
        return Promise.reject(error.response.data)
      }
    }
    ElMessage.error(error.message || 'Request failed')
    return Promise.reject(error)
  }
)

export const http={
    get(url,params){
        return service.get(url,{params})
    },
    post(url,data){
        return service.post(url,data)
    },
    put(url,data){
        return service.put(url,data)
    },
    delete(url){
        return service.delete(url)
    }
}   

export default service;
