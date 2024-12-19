<template>
  <div class="auth-content">
    <a-tabs v-model:activeKey="activeTab">
      <a-tab-pane key="account" title="账号登录">
        <a-form
          :model="formData"
          @submit="handleSubmit"
          layout="vertical"
        >
          <a-form-item
            field="username"
            :rules="[{ required: true, message: '请输入用户名' }]"
          >
            <a-input
              v-model="formData.username"
              placeholder="请输入用户名"
              allow-clear
            >
              <template #prefix>
                <icon-user />
              </template>
            </a-input>
          </a-form-item>

          <a-form-item
            field="password"
            :rules="[{ required: true, message: '请输入密码' }]"
          >
            <a-input-password
              v-model="formData.password"
              placeholder="请输入密码"
              allow-clear
            >
              <template #prefix>
                <icon-lock />
              </template>
            </a-input-password>
          </a-form-item>

          <a-form-item>
            <a-button type="primary" html-type="submit" long :loading="loading">
              登录
            </a-button>
          </a-form-item>
        </a-form>
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Message } from '@arco-design/web-vue'
import { IconUser, IconLock } from '@arco-design/web-vue/es/icon'

const emit = defineEmits(['login-success'])
const activeTab = ref('account')
const loading = ref(false)

const formData = ref({
  username: '',
  password: ''
})

const handleSubmit = async () => {
  loading.value = true
  try {
    // 模拟登录请求
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    // 根据用户名判断用户类型（实际应该由后端返回）
    const userType = formData.value.username.startsWith('admin') ? 'admin' : 'student'
    
    Message.success('登录成功')
    emit('login-success', userType)
  } catch (error) {
    Message.error('登录失败，请重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-content {
  width: 100%;
  padding: 20px 0;
}

:deep(.arco-tabs-nav) {
  margin-bottom: 24px;
}
</style> 