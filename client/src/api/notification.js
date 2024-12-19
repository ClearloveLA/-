import request from './index'














































export const notificationAPI = {
  // 获取用户的通知列表
  async getList(userId) {
    const response = await request.get('/notifications', { 
      params: { userId }
    })
    return response.map(notice => ({
      ...notice,
      isRead: notice.read // 确保字段名称匹配
    }))
  },

  // 创建通知
  create(data) {
    return request.post('/notifications', data)
  },

  // 标记通知为已读
  async markAsRead(id) {
    const response = await request.patch(`/notifications/${id}/read`)
    return response
  },

  // 标记所有通知为已读
  async markAllAsRead(userId) {
    const response = await request.patch(`/notifications/mark-all-read`, null, {
      params: { userId }
    })
    return response
  },

  // 获取未读通知数量
  async getUnreadCount(userId) {
    const response = await request.get('/notifications/unread-count', {
      params: { userId }
    })
    return response
  }
} 