<template>
  <div class="sidebar">
    <div class="logo">
      <i class="fas fa-id-card"></i>
      <span>校园卡管理系统</span>
    </div>
    
    <nav class="menu">
      <router-link to="/" class="menu-item" active-class="active">
        <i class="fas fa-home"></i>
        <span>首页</span>
      </router-link>
      
      <router-link to="/lost" class="menu-item" active-class="active">
        <i class="fas fa-exclamation-circle"></i>
        <span>卡片挂失</span>
      </router-link>
      
      <router-link to="/found" class="menu-item" active-class="active">
        <i class="fas fa-search"></i>
        <span>卡片招领</span>
      </router-link>

      <router-link to="/query" class="menu-item" active-class="active">
        <i class="fas fa-search-location"></i>
        <span>卡片查询</span>
      </router-link>

      <router-link to="/notifications" class="menu-item" active-class="active">
        <i class="fas fa-bell"></i>
        <span>消息通知</span>
        <span v-if="unreadCount > 0" class="badge">{{ unreadCount }}</span>
      </router-link>
      
      <router-link to="/statistics" class="menu-item" active-class="active">
        <i class="fas fa-chart-bar"></i>
        <span>数据统计</span>
      </router-link>
    </nav>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { notificationAPI } from '@/api/notification'

const unreadCount = ref(0)

const getUnreadCount = async () => {
  try {
    const count = await notificationAPI.getUnreadCount()
    unreadCount.value = count
  } catch (error) {
    console.error('获取未读消息数量失败:', error)
  }
}

onMounted(() => {
  getUnreadCount()
})
</script>

<style scoped>
.sidebar {
  width: 240px;
  height: 100vh;
  background: #2c3e50;
  color: white;
  padding: 20px 0;
  position: fixed;
  left: 0;
  top: 0;
}

.logo {
  padding: 0 20px;
  margin-bottom: 30px;
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 18px;
  font-weight: bold;
}

.logo i {
  font-size: 24px;
  color: #4facfe;
}

.menu {
  display: flex;
  flex-direction: column;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  color: #a4b0be;
  text-decoration: none;
  transition: all 0.3s;
  position: relative;
}

.menu-item i {
  width: 24px;
  margin-right: 10px;
}

.menu-item:hover {
  color: white;
  background: rgba(255,255,255,0.1);
}

.menu-item.active {
  color: white;
  background: linear-gradient(90deg, #4facfe 0%, #00f2fe 100%);
}

.badge {
  position: absolute;
  right: 20px;
  background: #ff4444;
  color: white;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 10px;
  min-width: 18px;
  text-align: center;
}
</style> 