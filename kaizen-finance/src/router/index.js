import { createRouter, createWebHistory } from 'vue-router'
import Home from '../components/home.vue'
import PrivacyPolicyFull from '../components/PrivacyPolicyFull.vue'
import { auth } from '../utils/request'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { ElMessage } from 'element-plus'
import http from '../utils/request'

NProgress.configure({
  showSpinner: false,
  easing: 'ease',
  speed: 500,
  minimum: 0.3
})

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/home'
    },
    {
      path: '/home',
      name: 'Home',
      component: Home
    },
    { 
      path: '/order', 
      component: () => import('../components/order.vue'),
      meta: { requiresAuth: true },
      children: [
        { path: 'pricing', component: () => import('../components/pricing.vue') },
        
      ]

    },
    { path: '/career', component: () => import('../components/career.vue') },
    { path: '/services', component: () => import('../components/services.vue') },
    { path: '/contact', component: () => import('../components/contact.vue') },
    { path: '/training', component: () => import('../components/training.vue') },
    { path: '/privacy-policy', component: PrivacyPolicyFull },
    { path: '/cookies-policy', component: () => import('../components/CookiesPolicy.vue') },
    { path: '/contracts', component: () => import('../components/contract.vue'), meta: { requiresAuth: true } },
    { path: '/login', component: () => import('../components/login.vue') },
    { 
      path: '/manager', 
      component: () => import('../components/manager.vue'),
      meta: { requiresAuth: true, requiresManager: true }
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: () => import('../components/404.vue')
    }
  ]
})

// 全局前置守卫
router.beforeEach(async (to, from, next) => {
  NProgress.start()
  const token = auth.getToken()

  // 处理需要认证的路由
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!token) {
      ElMessage.error('Please login first')
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      })
      NProgress.done()
      return
    }

    try {
      const response = await http.post('/api/verify-token', { token })
      if (response.code === 200) {
        // 检查是否需要manager权限
        if (to.matched.some(record => record.meta.requiresManager)) {
          if (response.username !== 'chuhan') {
            ElMessage.error('Access denied. Only managers can access this page.')
            next('/home')
            NProgress.done()
            return
          }
        }
        next()
      } else {
        auth.removeToken()
        ElMessage.error('Session expired, please login again')
        next({
          path: '/login',
          query: { redirect: to.fullPath }
        })
      }
    } catch (error) {
      console.error('Token verification failed:', error)
      // 检查是否是401错误响应（后端返回的JSON格式）
      if (error && error.code === 401) {
        auth.removeToken()
        ElMessage.error('Session expired, please login again')
      } else {
        auth.removeToken()
        ElMessage.error('Authentication failed')
      }
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      })
    }
  } 
  // 处理登录页面
  else if (to.path === '/login' && token) {
    try {
      const response = await http.post('/api/verify-token', { token })
      if (response.code === 200) {
        next('/order')
      } else {
        auth.removeToken()
        next()
      }
    } catch (error) {
      // 如果是401错误，清除token
      if (error && error.code === 401) {
        auth.removeToken()
      }
      next()
    }
  }
  // 其他路由
  else {
    next()
  }
  
  NProgress.done()
})

router.afterEach(() => {
  NProgress.done()
})

export default router