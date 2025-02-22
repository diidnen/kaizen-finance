import axios from 'axios'
import router from '@/router'
import {ElMessage} from 'element-plus'


export const TokenKey='access_Token'


const service = axios.create({
    baseURL: "http://localhost:8080",
    timeout: 10000,
    headers:{
        'Content-Type':'application/json'
    }
})

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
