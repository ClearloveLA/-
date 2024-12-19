<template>
  <div class="auth-container">
    <div class="auth-card">
      <!-- 登录表单 -->
      <div class="auth-form" v-show="!isRegister">
        <h2>欢迎回来</h2>
        <p class="subtitle">请登录您的账号继续访问</p>
        
        <form @submit.prevent="handleLogin">
          <div class="form-item">
            <input 
              type="text" 
              v-model="loginForm.username" 
              placeholder="账号"
              required
            >
          </div>

          <div class="form-item">
            <input 
              :type="showLoginPassword ? 'text' : 'password'"
              v-model="loginForm.password" 
              placeholder="密码"
              required
            >
            <span class="password-toggle" @click="showLoginPassword = !showLoginPassword">
              <i :class="showLoginPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
            </span>
          </div>

          <div class="form-options">
            <label class="remember-me">
              <input type="checkbox" v-model="loginForm.remember">
              <span>记住我</span>
            </label>
            <a href="#" class="forgot-link">忘记密码？</a>
          </div>

          <button type="submit" class="submit-btn" :disabled="loginLoading">
            {{ loginLoading ? '登录中...' : '登录' }}
          </button>
        </form>

        <div class="switch-form">
          <span>还没有账号？</span>
          <a href="#" @click.prevent="toggleForm">立即注册</a>
        </div>
      </div>

      <!-- 注册表单 -->
      <div class="auth-form" v-show="isRegister">
        <h2>创建账号</h2>
        <p class="subtitle">请填写以下信息完成注册</p>
        
        <form @submit.prevent="handleRegister">
          <div class="form-item">
            <input 
              type="text" 
              v-model="registerForm.username" 
              placeholder="账号"
              required
            >
          </div>

          <div class="form-item">
            <input 
              :type="showRegisterPassword ? 'text' : 'password'"
              v-model="registerForm.password" 
              placeholder="密码"
              required
            >
            <span class="password-toggle" @click="showRegisterPassword = !showRegisterPassword">
              <i :class="showRegisterPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
            </span>
          </div>

          <div class="form-item">
            <input 
              :type="showRegisterPassword ? 'text' : 'password'"
              v-model="registerForm.confirmPassword" 
              placeholder="确认密码"
              required
            >
          </div>

          <button type="submit" class="submit-btn" :disabled="registerLoading">
            {{ registerLoading ? '注册中...' : '注册' }}
          </button>
        </form>

        <div class="switch-form">
          <span>已有账号？</span>
          <a href="#" @click.prevent="toggleForm">立即登录</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const isRegister = ref(false)
const showLoginPassword = ref(false)
const showRegisterPassword = ref(false)
const loginLoading = ref(false)
const registerLoading = ref(false)

const loginForm = reactive({
  username: '',
  password: '',
  remember: false
})

const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: ''
})

const toggleForm = () => {
  isRegister.value = !isRegister.value
}

const handleLogin = async () => {
  try {
    loginLoading.value = true
    if (!loginForm.username || !loginForm.password) {
      ElMessage.warning('请输入用户名和密码')
      return
    }
    
    await userStore.login({
      username: loginForm.username,
      password: loginForm.password,
      remember: loginForm.remember
    })
    ElMessage.success('登录成功')
    router.push('/')
  } catch (error) {
    ElMessage.error(error.message || '登录失败，请重试')
  } finally {
    loginLoading.value = false
  }
}

const handleRegister = async () => {
  try {
    if (registerForm.password !== registerForm.confirmPassword) {
      ElMessage.warning('两次输入的密码不一致')
      return
    }
    
    registerLoading.value = true
    await userStore.register({
      username: registerForm.username,
      password: registerForm.password
    })
    ElMessage.success('注册成功')
    router.push('/')
  } catch (error) {
    ElMessage.error(error.message || '注册失败，请重试')
  } finally {
    registerLoading.value = false
  }
}
</script>

<style scoped>
.auth-container {
  min-height: 50vh;
  display: flex;
  justify-content: center;
  background-color: #f5f7fa;
  padding: 1rem;
  margin-top: 13vh;
}

.auth-card {
  background: hsl(0, 0%, 100%);
  border-radius: 8px;
  width: 100%;
  max-width: 400px;
  padding: 2rem;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.auth-form h2 {
  text-align: center;
  font-size: 1.5rem;
  margin-bottom: 0.5rem;
  color: #333;
}

.subtitle {
  text-align: center;
  color: #666;
  font-size: 0.875rem;
  margin-bottom: 1.5rem;
}

.form-item {
  position: relative;
  margin-bottom: 1rem;
}

.form-item input {
  width: 100%;
  padding: 0.75rem 1rem;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 0.875rem;
  transition: all 0.3s;
}

.form-item input:focus {
  outline: none;
  border-color: #1a73e8;
}

.password-toggle {
  position: absolute;
  right: 1rem;
  top: 50%;
  transform: translateY(-50%);
  color: #666;
  cursor: pointer;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  font-size: 0.875rem;
}

.remember-me {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #666;
}

.forgot-link {
  color: #1a73e8;
  text-decoration: none;
}

.submit-btn {
  width: 100%;
  padding: 0.75rem;
  background: #1a73e8;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 0.875rem;
  cursor: pointer;
  transition: background 0.3s;
}

.submit-btn:hover {
  background: #1557b0;
}

.submit-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.switch-form {
  margin-top: 1rem;
  text-align: center;
  font-size: 0.875rem;
  color: #666;
}

.switch-form a {
  color: #1a73e8;
  text-decoration: none;
  margin-left: 0.5rem;
}

/* 添加简单的切换动画 */
.auth-form {
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style> 