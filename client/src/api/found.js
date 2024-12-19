// 招领相关接口
import request from './index'
import { notificationAPI } from './notification'
import { lostAPI } from './lost'
import { ElMessage } from 'element-plus'

export const foundAPI = {
  // 获取招领列表
  getList() {
    return request.get('/found_cards')
  },

  // 获取招领详情
  getDetail(id) {
    return request.get(`/found_cards/${id}`)
  },

  // 提交招领信息
  async create(data) {
    const userInfo = JSON.parse(localStorage.getItem('userInfo'))
    const response = await request.post('/found_cards', {
      ...data,
      userId: userInfo.id,
      finder: userInfo.username
    })
    
    if (response.matched) {
      ElMessage.success(response.message)
      // 触发通知更新
      window.dispatchEvent(new CustomEvent('notification-update'))
    }
    
    return response.foundCard
  }
} 