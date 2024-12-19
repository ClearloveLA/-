// 挂失相关接口
import request from './index'
import { notificationAPI } from './notification'
import { foundAPI } from './found'
import { ElMessage } from 'element-plus'

export const lostAPI = {
  // 获取挂失列表
  getList() {
    return request.get('/lost_cards')
  },

  // 获取挂失详情
  getDetail(id) {
    return request.get(`/lost_cards/${id}`)
  },

  // 提交挂失
  async create(data) {
    const userInfo = JSON.parse(localStorage.getItem('userInfo'))
    const lostCard = await request.post('/lost_cards', {
      ...data,
      status: 'pending',
      time: new Date().toLocaleString(),
      userId: userInfo.id
    })

    // 查找是否有匹配的招领记录
    const foundCards = await foundAPI.getList()
    const matchedFound = foundCards.find(found => 
      found.cardId === data.cardId && 
      found.status === 'waiting'
    )

    if (matchedFound) {
      // 更新招领卡片状态为已认领
      await request.patch(`/found_cards/${matchedFound.id}`, {
        status: 'claimed'
      })

      // 通知失主
      await notificationAPI.create({
        userId: userInfo.id,
        type: 'found_match',
        title: '您的校园卡已被找到',
        content: `您挂失的校园卡(${data.cardId})已被拾获，请携带有效证件到${matchedFound.location}认领。`,
        relatedId: data.cardId,
        isRead: false,
        createTime: new Date().toISOString()
      })

      // 通知拾获者
      await notificationAPI.create({
        userId: matchedFound.userId,
        type: 'lost_match',
        title: '失主已提交挂失',
        content: `您拾获的校园卡(${data.cardId})的失主已提交挂失申请，请等待失主前来认领。`,
        relatedId: data.cardId,
        isRead: false,
        createTime: new Date().toISOString()
      })

      // 显示匹配成功提示
      ElMessage.success(`已找到您的校园卡！请查看消息通知了解详情。`)
      // 触发通知更新
      window.dispatchEvent(new CustomEvent('notification-update'))
    }

    return lostCard
  },

  // 更新挂失状态
  async updateStatus(id, status) {
    // 1. 更新状态
    const response = await request.patch(`/lost_cards/${id}`, { status })
    
    // 2. 获取挂失详情
    const lostCard = await this.getDetail(id)
    
    // 3. 创建状态更新通知
    await notificationAPI.create({
      userId: lostCard.userId,
      type: 'lost_status',
      title: '挂失状态更新',
      content: `您的校园卡(${lostCard.cardId})${this.getStatusText(status)}`,
      relatedId: lostCard.cardId,
      isRead: false,
      createTime: new Date().toISOString()
    })

    return response
  },

  // 获取状态文本
  getStatusText(status) {
    const statusMap = {
      'completed': '挂失已解除',
      'cancelled': '挂失已取消',
      'processing': '正在处理中'
    }
    return statusMap[status] || status
  },

  // 删除挂失记录
  delete(id) {
    return request.delete(`/lost_cards/${id}`)
  },

  // 更新挂失记录
  update(id, data) {
    return request.patch(`/lost_cards/${id}`, data)
  },

  // 取消挂失
  cancel(id) {
    return request.patch(`/lost_cards/${id}/cancel`)
  }
} 