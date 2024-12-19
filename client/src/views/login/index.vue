<template>
  <div :class="['login-container', { 'mobile': isMobile }]">
    <div v-if="isMobile" class="mobile-header">
      <div class="header-content">
        <h1>校园卡管理系统</h1>
        <p class="subtitle">Card Management System</p>
      </div>
    </div>

    <div class="auth-content">
      <template v-if="!isMobile">
        <a-card class="auth-card">
          <div class="auth-body">
            <AuthContent @login-success="handleLoginSuccess" />
          </div>
        </a-card>
      </template>

      <template v-else>
        <div class="mobile-auth-content">
          <AuthContent @login-success="handleLoginSuccess" />
        </div>
      </template>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useDevice } from '@/composables/useDevice'
import AuthContent from './components/AuthContent.vue'

const router = useRouter()
const { isMobile } = useDevice()

const handleLoginSuccess = (userType) => {
  if (userType === 'student') {
    router.push('/student/home')
  } else if (userType === 'admin') {
    router.push('/admin/dashboard')
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

/* PC端样式 */
.auth-card {
  width: 90%;
  max-width: 480px;
  margin: auto;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  padding: 24px;
}

/* 移动端样式 */
.login-container.mobile {
  background: #fff;
}

.mobile-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 48px 20px 40px;
  text-align: center;
  border-radius: 0 0 24px 24px;
}

.header-content {
  padding: 20px 0;
}

.mobile-header h1 {
  color: #fff;
  font-size: 24px;
  margin: 0;
  margin-bottom: 8px;
}

.mobile-header .subtitle {
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
  margin: 0;
}

.mobile-auth-content {
  flex: 1;
  padding: 24px 16px;
  background: #fff;
}

@media screen and (max-width: 768px) {
  .mobile-auth-content :deep(.arco-tabs-nav) {
    padding: 0 20px;
    margin-bottom: 24px;
  }

  .mobile-auth-content :deep(.arco-form-item) {
    margin-bottom: 20px;
  }

  .mobile-auth-content :deep(.arco-input-wrapper) {
    height: 48px;
    border-radius: 24px;
  }

  .mobile-auth-content :deep(.arco-btn) {
    height: 48px;
    font-size: 16px;
    border-radius: 24px;
  }
}
</style> 