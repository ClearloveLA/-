import axios from 'axios'

const api = axios.create({
  // baseURL: 'http://localhost:3000/', // mockAPI地址
  baseURL: 'http://localhost:8080/api', // 修改为 Spring Boot 后端地址
  timeout: 5000
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    // 可以在这里添加token等认证信息
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    return Promise.reject(error)
  }
)

export default api 