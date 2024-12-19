<template>
  <div class="notification-container">
    <div class="notification-header">
      <h3>通知消息</h3>
      <span class="unread-count" v-if="unreadCount > 0">{{ unreadCount }}</span>
      <button v-if="unreadCount > 0" @click="markAllAsRead" class="mark-all-btn">
        全部标记为已读
      </button>
    </div>

    <div class="notification-list">
      <div 
        v-for="notice in notifications" 
        :key="notice.id"
        class="notification-item"
        :class="{ unread: !notice.isRead }"
        @click="markAsRead(notice)"
      >
        <div class="notice-icon">
          <i :class="getNoticeIcon(notice.type)"></i>
        </div>
        <div class="notice-content">
          <h4>{{ notice.title }}</h4>
          <p>{{ notice.content }}</p>
          <span class="notice-time">{{ formatTime(notice.createTime) }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { notificationAPI } from '@/api/notification'
import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/zh-cn'

dayjs.extend(relativeTime)
dayjs.locale('zh-cn')

const notifications = ref([])
const unreadCount = ref(0)

// 格式化时间
const formatTime = (time) => {
  return dayjs(time).fromNow()
}

// 更新未读消息数量
const updateUnreadCount = () => {
  unreadCount.value = notifications.value.filter(n => !n.isRead).length
}

// 获取通知列表
const getNotifications = async () => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo'))
    const data = await notificationAPI.getList(userInfo.id)
    notifications.value = data.map(notice => ({
      ...notice,
      isRead: notice.read
    }))
    updateUnreadCount()
  } catch (error) {
    console.error('获取通知失败:', error)
  }
}

// 监听通知更新事件
const handleNotificationUpdate = async () => {
  await getNotifications()
}

// 标记单条通知为已读
const markAsRead = async (notice) => {
  if (notice.isRead) return
  try {
    const response = await notificationAPI.markAsRead(notice.id)
    if (response) {
      notice.isRead = true
      // 重新获取通知列表以确保状态同步
      await getNotifications()
      // 更新未读数量
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      const count = await notificationAPI.getUnreadCount(userInfo.id)
      unreadCount.value = count
    }
  } catch (error) {
    console.error('标记已读失败:', error)
  }
}

// 标记所有通知为已读
const markAllAsRead = async () => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo'))
    await notificationAPI.markAllAsRead(userInfo.id)
    // 重新获取通知列表以确保状态同步
    await getNotifications()
    unreadCount.value = 0
  } catch (error) {
    console.error('标记全部已读失败:', error)
  }
}

// 获取通知图标
const getNoticeIcon = (type) => {
  const iconMap = {
    'found_match': 'fas fa-search',
    'lost_match': 'fas fa-bell',
    'status': 'fas fa-info-circle'
  }
  return iconMap[type] || 'fas fa-bell'
}

onMounted(async () => {
  await getNotifications()
  window.addEventListener('notification-update', handleNotificationUpdate)
})

onUnmounted(() => {
  window.removeEventListener('notification-update', handleNotificationUpdate)
})
</script>

<style scoped>
.notification-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.notification-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.mark-all-btn {
  padding: 4px 12px;
  border: none;
  background-color: #f0f0f0;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.mark-all-btn:hover {
  background-color: #e0e0e0;
}

.unread-count {
  background: #ff4444;
  color: white;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
  margin-left: 8px;
}

.notification-item {
  display: flex;
  padding: 15px;
  background: white;
  border-radius: 8px;
  margin-bottom: 10px;
  cursor: pointer;
  transition: all 0.3s;
}

.notification-item.unread {
  background: #f0f7ff;
}

.notification-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.notice-icon {
  width: 40px;
  height: 40px;
  background: #e3f2fd;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
}

.notice-icon i {
  color: #1976d2;
  font-size: 18px;
}

.notice-content h4 {
  margin: 0 0 5px;
  font-size: 16px;
}

.notice-content p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.notice-time {
  font-size: 12px;
  color: #999;
  display: block;
  margin-top: 5px;
}
</style> 