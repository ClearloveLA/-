// 统计相关接口
import request from './index'

export const statsAPI = {
  // 获取统计数据
  getDashboard() {
    return request.get('/stats')
  },

  // 获取最近活动
  getActivities() {
    return request.get('/activities')
  }
} 