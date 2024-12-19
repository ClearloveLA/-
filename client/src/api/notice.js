// 公告相关接口
import request from './index'

export const noticeAPI = {
  // 获取公告列表
  getList() {
    return request.get('/notices')
  },

  // 创建公告
  create(data) {
    return request.post('/notices', {
      ...data,
      time: new Date().toLocaleString(),
      isActive: true
    })
  },

  // 更新公告
  update(id, data) {
    return request.patch(`/notices/${id}`, data)
  },

  // 删除公告
  delete(id) {
    return request.delete(`/notices/${id}`)
  }
} 