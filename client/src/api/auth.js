import request from './index'

export const authAPI = {
  // 登录
  login(data) {
    return request.get(`/users?username=${data.username}&password=${data.password}`)
  },

  // 注册
  register(data) {
    return request.post('/users', {
      ...data,
      role: 'user',
      avatar: `https://i.pravatar.cc/150?img=${Math.floor(Math.random() * 70)}`
    })
  }
} 