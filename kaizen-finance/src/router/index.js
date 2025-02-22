import { createRouter, createWebHistory } from 'vue-router'
import Home from '../components/home.vue'
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
    {path:'/contract',meta:{requiresAuth:true},component:()=>import('../components/contract.vue')},
    {path:'/profile',meta:{requiresAuth:true},component:()=>import('../components/profile.vue')},
    { path: '/career', component: () => import('../components/career.vue') },
    { path: '/services', component: () => import('../components/services.vue') },
    { path: '/contact', component: () => import('../components/contact.vue') },
    { path: '/training', component: () => import('../components/training.vue') },
    { path: '/login', component: () => import('../components/login.vue') },
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
      const response = await http.post('/verify-token', { token })
      if (response.code === 200) {
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
      auth.removeToken()
      ElMessage.error('Authentication failed')
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      })
    }
  } 
  // 处理登录页面
  else if (to.path === '/login' && token) {
    try {
      const response = await http.post('/verify-token', { token })
      if (response.code === 200) {
        next('/order')
      } else {
        auth.removeToken()
        next()
      }
    } catch (error) {
      auth.removeToken()
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