<template>
  <div id="app">
    <header class="header">
      <div class="header-container">
        <!-- 品牌区域 -->
        <div class="brand">
          <img src="@/assets/final.png" alt="logo" class="logo">
        </div>
        
        <!-- 导航栏 -->
        <nav class="nav">
          <div class="nav-links" :class="{ 'nav-links-active': isMenuOpen }">
            <router-link 
              v-for="item in filteredNavigation" 
              :key="item.name"
              :to="item.href"
              class="nav-link"
              :class="{ 'active': isActive(item.href) }"
              @click="toggleMenu"
            >
              {{ item.name }}
            </router-link>
          </div>
          <!-- 汉堡菜单按钮 -->
          <button class="hamburger" :class="{ 'active': isMenuOpen }" @click="toggleMenu">
            <span></span>
            <span></span>
            <span></span>
          </button>
        </nav>
      </div>
    </header>
    <main class="main-content">
      <router-view></router-view>
    </main>
  </div>
</template>

<script>
import { auth } from '@/utils/request'

export default {
  name: 'App',
  data() {
    return {
      isMenuOpen: false,
      navigation: [
        { name: 'Home', href: '/home', requiresAuth: false },
        { name: 'Services', href: '/services', requiresAuth: false },
        { name: 'Contact', href: '/contact', requiresAuth: false },
        { name: 'Training', href: '/training', requiresAuth: false },
        { name: 'Order', href: '/order', requiresAuth: true },
        { name: 'Login', href: '/login', hideWhenAuth: true },
        { name: 'Profile', href: '/profile', requiresAuth: true, hidden: true },
        { name: 'Contract', href: '/contract', requiresAuth: true },
        { name: 'Career', href: '/career', requiresAuth: false },
      ]
    }
  },
  computed: {
    filteredNavigation() {
      const isLoggedIn = auth.getToken()
      return this.navigation.filter(item => {
        if (item.hidden) return false
        if (item.hideWhenAuth && isLoggedIn) return false
        if (item.requiresAuth && !isLoggedIn) return false
        return true
      })
    }
  },
  mounted() {
    // 初始化时设置当前活动项
    this.updateActiveItem();
    // 监听路由变化
    window.addEventListener('popstate', this.updateActiveItem);
  },
  beforeDestroy() {
    // 清理监听器
    window.removeEventListener('popstate', this.updateActiveItem);
  },
  methods: {
    toggleMenu() {
      this.isMenuOpen = !this.isMenuOpen;
    },
    isActive(path) {
      // 处理首页重定向
      if (this.$route.path === '/' && path === '/home') {
        return true
      }
      
      // 处理登录重定向
      if (this.$route.path === '/login' && this.$route.query.redirect) {
        return this.$route.query.redirect === path
      }
      
      // 普通路由匹配
      return this.$route.path === path
    },
    updateActiveItem() {
      // 从当前路径获取活动项
      const path = window.location.pathname;
      const currentPage = path.split('/')[1] || 'home';
      // 找到对应的导航项
      const matchedItem = this.navigation.find(
        item => item.href.toLowerCase() === `/${currentPage}`
      );
      if (matchedItem) {
        this.activeItem = matchedItem.name;
      }
    }
  }
}
</script>

<style>
:root {
  --header-height: 4rem;
  --color-primary: #000000;
}

.main-content {
  margin-top: var(--header-height);
} 
/* 重置样式 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

/* 头部样式 */
.header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background: white;
  z-index: 1000;
}

.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: var(--header-height);
  padding: 0 2rem;
  max-width: 100%;
  margin: 0 auto;
}

/* 品牌区域 */
.brand {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.logo {
  height: 2.5rem;
  width: auto;
}


/* 导航区域 */
.nav {
  display: flex;
  align-items: center;
}

.nav-links {
  display: flex;
  gap: 2rem;
}

.nav-link {
  color: var(--color-primary);
  text-decoration: none;
  font-size: 1rem;
  font-weight: 500;
  padding: 0.5rem 1rem;
  position: relative;
}

.nav-link::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 2px;
  background-color: var(--color-primary);
  transition: all 0.3s ease;
  transform: translateX(-50%);
}

.nav-link:hover::after,
.nav-link.active::after {
  width: 80%;
}

.nav-link.active {
  font-weight: 600;
  color: var(--color-primary);
}

/* 汉堡菜单 */
.hamburger {
  display: none;
  flex-direction: column;
  justify-content: space-between;
  width: 30px;
  height: 20px;
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 0;
}

.hamburger span {
  width: 100%;
  height: 2px;
  background-color: var(--color-primary);
  transition: all 0.3s ease;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hamburger {
    display: flex;
  }

  .nav-links {
    position: fixed;
    top: var(--header-height);
    right: 0;
    width: 200px;
    flex-direction: column;
    background: white;
    padding: 1rem;
    gap: 1rem;
    transform: translateX(100%);
    transition: transform 0.3s ease;
    box-shadow: -2px 0 5px rgba(0,0,0,0.1);
  }

  .nav-links-active {
    transform: translateX(0);
  }
}
</style>
