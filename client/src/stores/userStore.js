import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authAPI } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  // 安全地解析 localStorage 中的 userInfo
  const getUserInfoFromStorage = () => {
    try {
      const storedInfo = localStorage.getItem('userInfo')
      return storedInfo ? JSON.parse(storedInfo) : null
    } catch (error) {
      console.error('解析用户信息失败:', error)
      return null
    }
  }

  const userInfo = ref(getUserInfoFromStorage())
  const token = ref(localStorage.getItem('token') || '')
  
  const isAuthenticated = computed(() => !!token.value)

  // 登录
  const login = async (credentials) => {
    try {
      const response = await authAPI.login(credentials)
      const userData = response.data || response // 兼容不同的响应格式
      userInfo.value = userData
      token.value = btoa(JSON.stringify(credentials))
      
      localStorage.setItem('userInfo', JSON.stringify(userData))
      localStorage.setItem('token', token.value)
      
      return userData
    } catch (error) {
      console.error('登录失败:', error)
      if (error.response?.status === 401) {
        throw new Error('用户名或密码错误')
      }
      throw error
    }
  }

  // 注册
  // 注册
  const register = async (data) => {    try {
    const response = await authAPI.register(data)
    // 注册成功后不自动登录，只返回结果
    return response.data || response
  } catch (error) {
    console.error('注册失败:', error)
    if (error.response?.status === 400) {
      throw new Error(error.response.data.message || '注册信息不正确')
    }
    if (error.response?.status === 409) {
      throw new Error('用户名已存在')
    }
    throw new Error('注册失败，请重试')
  }
}

  // 登出
  const logout = () => {
    userInfo.value = null
    token.value = ''
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  return {
    userInfo,
    token,
    isAuthenticated,
    login,
    register,
    logout
  }
}) 