import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import LostCard from '../views/lost/LostCard.vue'
import FoundCard from '../views/found/FoundCard.vue'
import CardQuery from '../views/CardQuery.vue'
import Notifications from '../views/Notifications.vue'
import Statistics from '../views/Statistics.vue'
import Auth from '../views/auth/Auth.vue'

const routes = [
  {
    path: '/auth',
    name: 'Auth',
    component: Auth,
    meta: { requiresGuest: true }
  },
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: { requiresAuth: true }
  },
  {
    path: '/lost',
    name: 'LostCard',
    component: LostCard,
    meta: { requiresAuth: true }
  },
  {
    path: '/found',
    name: 'FoundCard',
    component: FoundCard,
    meta: { requiresAuth: true }
  },
  {
    path: '/query',
    name: 'CardQuery',
    component: CardQuery,
    meta: { requiresAuth: true }
  },
  {
    path: '/notifications',
    name: 'Notifications',
    component: Notifications,
    meta: { requiresAuth: true }
  },
  {
    path: '/statistics',
    name: 'Statistics',
    component: Statistics,
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/data',
    component: () => import('@/views/admin/DataDisplay.vue'),
    meta: { requiresAdmin: true }
  },
  {
    path: '/admin/notice',
    component: () => import('@/views/admin/NoticeManage.vue'),
    meta: { requiresAdmin: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const userInfo = localStorage.getItem('userInfo')
  const isAuthenticated = token && userInfo

  // 已登录用户访问登录页，重定向到首页
  if (to.path === '/auth' && isAuthenticated) {
    next('/')
    return
  }

  // 未登录用户访问任何页面，重定向到登录页
  if (!isAuthenticated && to.path !== '/auth') {
    next('/auth')
    return
  }

  if (to.meta.requiresAdmin) {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    if (userInfo.role !== 'admin') {
      next('/')
      return
    }
  }

  next()
})

export default router 