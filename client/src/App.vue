<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useUserStore } from '@/stores/userStore'
import { useRouter } from 'vue-router'
import { storeToRefs } from 'pinia'
import { notificationAPI } from '@/api/notification'

const sidebarOpen = ref(true)
const userStore = useUserStore()
const router = useRouter()
const { isAuthenticated, userInfo } = storeToRefs(userStore)
const unreadCount = ref(0)

// 判断是否为管理员
const isAdmin = computed(() => {
  return userInfo.value?.role === 'admin'
})

// 获取未读消息数量
const getUnreadCount = async () => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo'))
    if (!userInfo) return
    const count = await notificationAPI.getUnreadCount(userInfo.id)
    unreadCount.value = count
  } catch (error) {
    console.error('获取未读消息数量失败:', error)
  }
}

// 监听登录状态变化
watch(isAuthenticated, (newValue) => {
  if (newValue) {
    getUnreadCount()
  } else {
    unreadCount.value = 0
  }
})

onMounted(() => {
  if (isAuthenticated.value) {
    getUnreadCount()
  }
})

const toggleSidebar = () => {
  sidebarOpen.value = !sidebarOpen.value
}

const handleLogout = () => {
  userStore.logout()
  router.push('/auth')
}
</script>

<template>
  <div class="wrapper">
    <!-- 侧边栏 -->
    <nav id="sidebar" :class="{ active: !sidebarOpen }">
      <div class="sidebar-header">
        <h3><i class="fas fa-id-card"></i> 校园卡管理系统</h3>
      </div>

      <ul class="list-unstyled components">
        <li>
          <router-link to="/" class="nav-link">
            <i class="fas fa-home"></i> 首页
          </router-link>
        </li>
        <li>
          <router-link to="/lost" class="nav-link">
            <i class="fas fa-exclamation-circle"></i> 卡片挂失
          </router-link>
        </li>
        <li>
          <router-link to="/found" class="nav-link">
            <i class="fas fa-search"></i> 卡片招领
          </router-link>
        </li>
        <li>
          <router-link to="/query" class="nav-link">
            <i class="fas fa-search-location"></i> 卡片查询
          </router-link>
        </li>
        <li>
          <router-link to="/notifications" class="nav-link">
            <i class="fas fa-bell"></i> 消息通知
            <span v-if="unreadCount > 0" class="badge bg-danger">{{ unreadCount }}</span>
          </router-link>
        </li>
        <!-- 管理员专属菜单 -->
        <template v-if="isAdmin">
          <li class="nav-divider">
            <span class="divider-text">管理功能</span>
          </li>
          <li>
            <router-link to="/admin/notice" class="nav-link">
              <i class="fas fa-bullhorn"></i> 公告管理
            </router-link>
          </li>
        </template>
      </ul>
    </nav>

    <!-- 主要内容区域 -->
    <div id="content" :class="{ active: !sidebarOpen }">
      <!-- 顶部导航栏 -->
      <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container-fluid">
          <button type="button" @click="toggleSidebar" class="btn btn-info">
            <i class="fas fa-bars"></i>
          </button>
          <div class="ms-auto">
            <div class="dropdown" v-if="isAuthenticated">
              <button class="btn btn-light dropdown-toggle" type="button" id="userDropdown" data-bs-toggle="dropdown">
                <i class="fas fa-user"></i> {{ userInfo?.username || '用户' }}
              </button>
              <ul class="dropdown-menu dropdown-menu-end">
                <li><a class="dropdown-item" href="#"><i class="fas fa-user-cog"></i> 个人设置</a></li>
                <li><hr class="dropdown-divider"></li>
                <li><a class="dropdown-item" href="#" @click.prevent="handleLogout">
                  <i class="fas fa-sign-out-alt"></i> 退出登录
                </a></li>
              </ul>
            </div>
          </div>
        </div>
      </nav>

      <div class="content-body">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<style>
/* 引入字体图标 */
@import url('https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css');

.wrapper {
  display: flex;
  width: 100%;
  min-height: 100vh;
}

#sidebar {
  width: 250px;
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh;
  z-index: 999;
  background: #1e1e2d;
  transition: all 0.3s ease;
}

#sidebar.active {
  margin-left: -250px;
}

#sidebar .sidebar-header {
  padding: 20px;
  background: #1a1a27;
  border-bottom: 1px solid #2d2d3f;
}

#sidebar .sidebar-header h3 {
  color: #fff;
  margin: 0;
}

#sidebar ul.components {
  padding: 20px 0;
}

#sidebar ul li a {
  padding: 12px 20px;
  font-size: 1.1em;
  display: flex;
  align-items: center;
  color: #a4b0be;
  text-decoration: none;
  transition: all 0.3s;
  position: relative;
}

#sidebar ul li a i {
  width: 24px;
  margin-right: 10px;
}

#sidebar ul li a:hover {
  color: white;
  background: rgba(255,255,255,0.1);
}

#sidebar ul li a.router-link-active {
  color: white;
  background: linear-gradient(90deg, #4facfe 0%, #00f2fe 100%);
}

.badge {
  position: absolute;
  right: 20px;
  padding: 2px 6px;
  font-size: 12px;
  border-radius: 10px;
  min-width: 18px;
  text-align: center;
}

#content {
  position: relative;
  width: calc(100% - 250px);
  margin-left: 250px;
  transition: all 0.3s ease;
}

#content.active {
  width: 100%;
  margin-left: 0;
}

.content-body {
  padding: 20px;
  background: #f8f9fa;
  min-height: calc(100vh - 60px);
}

.navbar {
  padding: 15px 10px;
  border-bottom: 1px solid #ddd;
  margin-bottom: 0;
}

.navbar button {
  box-shadow: none;
}

/* 响应式设计 */
@media (max-width: 768px) {
  #sidebar {
    margin-left: -250px;
  }
  #sidebar.active {
    margin-left: 0;
  }
  #content {
    width: 100%;
    margin-left: 0;
  }
  #content.active {
    width: calc(100% - 250px);
    margin-left: 250px;
  }
}

/* 添加管理员菜单分隔线样式 */
.nav-divider {
  padding: 10px 20px;
  color: #6c757d;
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 1px;
  border-top: 1px solid rgba(255,255,255,0.1);
  margin-top: 10px;
}

.nav-divider .divider-text {
  color: #6c757d;
  font-weight: 600;
}

/* 确保管理员菜单项样式与其他菜单项一致 */
#sidebar ul li.nav-divider {
  padding: 10px 20px;
  color: #6c757d;
}

#sidebar ul li.nav-divider .divider-text {
  display: block;
  padding: 5px 0;
}
</style>

<style scoped>
.wrapper {
  display: flex;
  width: 100%;
  min-height: 100vh;
}

#sidebar {
  width: 250px;
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh;
  z-index: 999;
  background: #1e1e2d;
  transition: all 0.3s ease;
}

#sidebar.active {
  margin-left: -250px;
}

#content {
  position: relative;
  width: calc(100% - 250px);
  margin-left: 250px;
  transition: all 0.3s ease;
}

#content.active {
  width: 100%;
  margin-left: 0;
}

.sidebar-toggle {
  padding: 0.5rem;
  background: transparent;
  border: none;
  color: #333;
  cursor: pointer;
}

.sidebar-toggle:hover {
  color: #0d6efd;
}

.sidebar-toggle:focus {
  box-shadow: none;
}

/* 响应式布局 */
@media (max-width: 768px) {
  #sidebar {
    margin-left: -250px;
  }
  
  #sidebar.active {
    margin-left: 0;
  }
  
  #content {
    width: 100%;
    margin-left: 0;
  }
  
  #content.active {
    width: calc(100% - 250px);
    margin-left: 250px;
  }
}
</style>
