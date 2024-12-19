<template>
  <div class="notifications-page">
    <div class="page-header">
      <h2><i class="fas fa-bell"></i> 消息通知</h2>
      <div class="header-actions">
        <button class="btn-read-all" @click="markAllAsRead" v-if="hasUnread">
          <i class="fas fa-check-double"></i> 全部已读
        </button>
      </div>
    </div>

    <div class="notifications-container">
      <!-- 加载状态 -->
      <div v-if="loading" class="loading-state">
        <i class="fas fa-spinner fa-spin"></i>
        <span>加载中...</span>
      </div>

      <!-- 空状态 -->
      <div v-else-if="!notifications.length" class="empty-state">
        <i class="fas fa-bell-slash"></i>
        <p>暂无通知消息</p>
      </div>

      <!-- 通知列表 -->
      <div v-else class="notification-list">
        <div 
          v-for="notice in notifications" 
          :key="notice.id"
          class="notification-item"
          :class="{ unread: !notice.isRead }"
          @click="markAsRead(notice)"
        >
          <div class="notice-icon" :class="notice.type">
            <i :class="getNoticeIcon(notice.type)"></i>
          </div>
          <div class="notice-content">
            <div class="notice-header">
              <h4>{{ notice.title }}</h4>
              <span class="notice-time">{{ formatTime(notice.createTime) }}</span>
            </div>
            <p class="notice-message">{{ notice.content }}</p>
            <div v-if="!notice.isRead" class="unread-badge">
              <span>未读</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { notificationAPI } from '@/api/notification'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/zh-cn'

dayjs.extend(relativeTime)
dayjs.locale('zh-cn')

const notifications = ref([])
const loading = ref(true)
const unreadCount = ref(0)

// 是否有未读消息
const hasUnread = computed(() => {
  return notifications.value.some(notice => !notice.isRead)
})

// 获取通知列表
const getNotifications = async () => {
  try {
    loading.value = true
    const userInfo = JSON.parse(localStorage.getItem('userInfo'))
    const data = await notificationAPI.getList(userInfo.id)
    notifications.value = data
  } catch (error) {
    console.error('获取通知列表失败:', error)
    ElMessage.error('获取通知列表失败')
  } finally {
    loading.value = false
  }
}

// 标记单条通知为已读
const markAsRead = async (notice) => {
  if (notice.isRead) return
  try {
    await notificationAPI.markAsRead(notice.id)
    notice.isRead = true
    ElMessage.success('已标记为已读')
    // 更新未读数量
    unreadCount.value = notifications.value.filter(n => !n.isRead).length
  } catch (error) {
    console.error('标记已读失败:', error)
    ElMessage.error('标记已读失败')
  }
}

// 标记所有通知为已读
const markAllAsRead = async () => {
  try {
    const promises = notifications.value
      .filter(n => !n.isRead)
      .map(n => notificationAPI.markAsRead(n.id))
    
    await Promise.all(promises)
    notifications.value.forEach(notice => {
      notice.isRead = true
    })
    ElMessage.success('已全部标记为已读')
    unreadCount.value = 0
  } catch (error) {
    console.error('标记全部已读失败:', error)
    ElMessage.error('标记全部已读失败')
  }
}

// 获取通知图标
const getNoticeIcon = (type) => {
  const iconMap = {
    'found': 'fas fa-search',
    'status': 'fas fa-info-circle',
    'system': 'fas fa-cog'
  }
  return iconMap[type] || 'fas fa-bell'
}

// 格式化时间
const formatTime = (time) => {
  return dayjs(time).fromNow()
}

onMounted(() => {
  getNotifications()
})
</script>

<style scoped>
.notifications-page {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-header h2 {
  margin: 0;
  color: #2c3e50;
}

.page-header h2 i {
  margin-right: 8px;
  color: #4facfe;
}

.btn-read-all {
  background: none;
  border: 1px solid #4facfe;
  color: #4facfe;
  padding: 6px 12px;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-read-all:hover {
  background: #4facfe;
  color: white;
}

.loading-state,
.empty-state {
  text-align: center;
  padding: 40px 0;
  color: #666;
}

.loading-state i,
.empty-state i {
  font-size: 48px;
  color: #ddd;
  margin-bottom: 16px;
}

.notification-item {
  display: flex;
  padding: 20px;
  background: white;
  border-radius: 8px;
  margin-bottom: 12px;
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
  border: 1px solid #eee;
}

.notification-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.notification-item.unread {
  background: #f0f7ff;
  border-color: #4facfe;
}

.notice-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  flex-shrink: 0;
}

.notice-icon.found {
  background: #e3f2fd;
  color: #1976d2;
}

.notice-icon.status {
  background: #e8f5e9;
  color: #2e7d32;
}

.notice-icon.system {
  background: #fff3e0;
  color: #f57c00;
}

.notice-content {
  flex: 1;
}

.notice-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 8px;
}

.notice-header h4 {
  margin: 0;
  color: #2c3e50;
  font-size: 16px;
}

.notice-time {
  color: #999;
  font-size: 12px;
}

.notice-message {
  margin: 0;
  color: #666;
  font-size: 14px;
  line-height: 1.5;
}

.unread-badge {
  position: absolute;
  top: 12px;
  right: 12px;
}

.unread-badge span {
  background: #ff4444;
  color: white;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
}

@media (max-width: 768px) {
  .notifications-page {
    padding: 12px;
  }

  .notification-item {
    padding: 16px;
  }
}
</style> 