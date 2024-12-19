<template>
  <div class="report-loss">
    <a-card class="form-card">
      <template #title>
        校园卡挂失申请
      </template>
      
      <a-form 
        :model="formData" 
        @submit="handleSubmit"
        layout="vertical"
      >
        <!-- 身份验证部分 -->
        <a-form-item
          field="verifyType"
          label="验证方式"
          :rules="[{ required: true, message: '请选择验证方式' }]"
        >
          <a-radio-group v-model="formData.verifyType">
            <a-radio value="idCard">身份证号</a-radio>
            <a-radio value="phone">预留手机号</a-radio>
            <a-radio value="studentId">学号</a-radio>
          </a-radio-group>
        </a-form-item>

        <!-- 动态验证字段 -->
        <a-form-item
          :field="formData.verifyType"
          label="验证信息"
          :rules="[{ required: true, message: '请输入验证信息' }]"
        >
          <a-input
            v-model="formData[formData.verifyType]"
            :placeholder="getPlaceholder"
            allow-clear
          />
        </a-form-item>

        <!-- 卡号信息 -->
        <a-form-item
          field="cardNumber"
          label="校园卡号（选填）"
        >
          <a-input
            v-model="formData.cardNumber"
            placeholder="如果记得卡号请填写"
            allow-clear
          />
        </a-form-item>

        <!-- 丢失信息 -->
        <a-form-item
          field="lossTime"
          label="大致丢失时间"
          :rules="[{ required: true, message: '请选择丢失时间' }]"
        >
          <a-date-picker
            v-model="formData.lossTime"
            show-time
            style="width: 100%"
          />
        </a-form-item>

        <a-form-item
          field="lossLocation"
          label="可能丢失地点"
          :rules="[{ required: true, message: '请输入丢失地点' }]"
        >
          <a-input
            v-model="formData.lossLocation"
            placeholder="请尽可能详细描述丢失地点"
            allow-clear
          />
        </a-form-item>

        <a-form-item
          field="description"
          label="补充说明"
        >
          <a-textarea
            v-model="formData.description"
            placeholder="可补充描述其他相关信息"
            :max-length="200"
            show-word-limit
          />
        </a-form-item>

        <!-- 提交按钮 -->
        <a-form-item>
          <a-button type="primary" html-type="submit" long :loading="loading">
            提交挂失申请
          </a-button>
        </a-form-item>
      </a-form>
    </a-card>

    <!-- 挂失须知 -->
    <a-card class="notice-card">
      <template #title>
        <span class="notice-title">
          <icon-info-circle /> 挂失须知
        </span>
      </template>
      <a-typography>
        <a-typography-paragraph>
          1. 挂失后校园卡将立即失效，无法进行任何消费和门禁使用。
        </a-typography-paragraph>
        <a-typography-paragraph>
          2. 如找回原卡，可在查询中心申请解除挂失。
        </a-typography-paragraph>
        <a-typography-paragraph>
          3. 挂失成功后可立即前往服务大厅办理补办手续。
        </a-typography-paragraph>
      </a-typography>
    </a-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Message } from '@arco-design/web-vue'
import { IconInfoCircle } from '@arco-design/web-vue/es/icon'

const loading = ref(false)

const formData = ref({
  verifyType: 'studentId',
  studentId: '',
  idCard: '',
  phone: '',
  cardNumber: '',
  lossTime: null,
  lossLocation: '',
  description: ''
})

const getPlaceholder = computed(() => {
  const placeholders = {
    studentId: '请输入学号',
    idCard: '请输入身份证号',
    phone: '请输入预留手机号'
  }
  return placeholders[formData.value.verifyType]
})

const handleSubmit = async () => {
  loading.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    Message.success('挂失申请提交成功')
    // 重置表单
    formData.value = {
      verifyType: 'studentId',
      studentId: '',
      idCard: '',
      phone: '',
      cardNumber: '',
      lossTime: null,
      lossLocation: '',
      description: ''
    }
  } catch (error) {
    Message.error('提交失败，请重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.report-loss {
  max-width: 800px;
  margin: 0 auto;
}

.form-card {
  margin-bottom: 16px;
}

.notice-card {
  background: #f9f9f9;
}

.notice-title {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #165DFF;
}

:deep(.arco-form-item-label-col) {
  padding-bottom: 8px;
}

@media screen and (max-width: 768px) {
  .report-loss {
    margin: 0 -16px;
  }
  
  .form-card,
  .notice-card {
    border-radius: 0;
  }
}
</style> 