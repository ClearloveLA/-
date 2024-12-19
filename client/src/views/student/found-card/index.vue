<template>
  <div class="found-card">
    <a-card class="form-card">
      <template #title>
        <span class="form-title">
          <icon-heart /> 校园卡招领登记
        </span>
      </template>

      <a-form
        :model="formData"
        @submit="handleSubmit"
        layout="vertical"
      >
        <!-- 卡片信息 -->
        <a-form-item
          field="cardNumber"
          label="校园卡号"
          :rules="[{ required: true, message: '请输入校园卡号' }]"
        >
          <a-input
            v-model="formData.cardNumber"
            placeholder="请输入卡片上的卡号"
            allow-clear
          >
            <template #prefix>
              <icon-credit-card />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item
          field="studentName"
          label="持卡人姓名"
          :rules="[{ required: true, message: '请输入持卡人姓名' }]"
        >
          <a-input
            v-model="formData.studentName"
            placeholder="请输入卡片上的姓名"
            allow-clear
          >
            <template #prefix>
              <icon-user />
            </template>
          </a-input>
        </a-form-item>

        <!-- 拾获信息 -->
        <a-form-item
          field="foundTime"
          label="拾获时间"
          :rules="[{ required: true, message: '请选择拾获时间' }]"
        >
          <a-date-picker
            v-model="formData.foundTime"
            show-time
            style="width: 100%"
          />
        </a-form-item>

        <a-form-item
          field="foundLocation"
          label="拾获地点"
          :rules="[{ required: true, message: '请输入拾获地点' }]"
        >
          <a-input
            v-model="formData.foundLocation"
            placeholder="请详细描述拾获地点"
            allow-clear
          >
            <template #prefix>
              <icon-location />
            </template>
          </a-input>
        </a-form-item>

        <!-- 联系方式 -->
        <a-form-item
          field="contactType"
          label="联系方式"
        >
          <a-radio-group v-model="formData.contactType">
            <a-radio value="phone">手机号</a-radio>
            <a-radio value="wechat">微信号</a-radio>
            <a-radio value="qq">QQ号</a-radio>
          </a-radio-group>
        </a-form-item>

        <a-form-item
          :field="formData.contactType"
          :rules="[{ required: true, message: '请输入联系方式' }]"
        >
          <a-input
            v-model="formData[formData.contactType]"
            :placeholder="`请输入${contactTypeLabel}"`
            allow-clear
          >
            <template #prefix>
              <icon-phone v-if="formData.contactType === 'phone'" />
              <icon-wechat v-else-if="formData.contactType === 'wechat'" />
              <icon-qq v-else />
            </template>
          </a-input>
        </a-form-item>

        <!-- 备注信息 -->
        <a-form-item
          field="remark"
          label="备注说明"
        >
          <a-textarea
            v-model="formData.remark"
            placeholder="可补充说明其他相关信息"
            :max-length="200"
            show-word-limit
          />
        </a-form-item>

        <!-- 提交按钮 -->
        <a-form-item>
          <a-button type="primary" html-type="submit" long :loading="loading">
            提交招领信息
          </a-button>
        </a-form-item>
      </a-form>
    </a-card>

    <!-- 温馨提示 -->
    <a-card class="tips-card">
      <template #title>
        <span class="tips-title">
          <icon-info-circle /> 温馨提示
        </span>
      </template>
      <a-typography>
        <a-typography-paragraph>
          1. 请确保填写的信息准确无误，以便失主及时找到。
        </a-typography-paragraph>
        <a-typography-paragraph>
          2. 建议尽快将拾到的校园卡交至学校失物招领处。
        </a-typography-paragraph>
        <a-typography-paragraph>
          3. 系统会自动匹配挂失信息，如有匹配将通知双方。
        </a-typography-paragraph>
      </a-typography>
    </a-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Message } from '@arco-design/web-vue'
import {
  IconHeart,
  IconCreditCard,
  IconUser,
  IconLocation,
  IconPhone,
  IconInfoCircle
} from '@arco-design/web-vue/es/icon'

const loading = ref(false)

const formData = ref({
  cardNumber: '',
  studentName: '',
  foundTime: null,
  foundLocation: '',
  contactType: 'phone',
  phone: '',
  wechat: '',
  qq: '',
  remark: ''
})

const contactTypeLabel = computed(() => {
  const labels = {
    phone: '手机号',
    wechat: '微信号',
    qq: 'QQ号'
  }
  return labels[formData.value.contactType]
})

const handleSubmit = async () => {
  loading.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    Message.success('招领信息提交成功')
    // 重置表单
    formData.value = {
      cardNumber: '',
      studentName: '',
      foundTime: null,
      foundLocation: '',
      contactType: 'phone',
      phone: '',
      wechat: '',
      qq: '',
      remark: ''
    }
  } catch (error) {
    Message.error('提交失败，请重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.found-card {
  max-width: 800px;
  margin: 0 auto;
}

.form-card {
  margin-bottom: 16px;
}

.form-title,
.tips-title {
  display: flex;
  align-items: center;
  gap: 8px;
}

.tips-title {
  color: #165DFF;
}

.tips-card {
  background: #f9f9f9;
}

@media screen and (max-width: 768px) {
  .found-card {
    margin: 0 -16px;
  }
  
  .form-card,
  .tips-card {
    border-radius: 0;
  }
}
</style> 