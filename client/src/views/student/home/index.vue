<template>
  <div class="student-home">
    <!-- 卡片信息概览 -->
    <a-card class="card-info">
      <template #title>
        <span class="card-title">
          <icon-credit-card /> 我的校园卡
        </span>
      </template>
      
      <a-descriptions :column="isMobile ? 1 : 2" layout="vertical">
        <a-descriptions-item label="卡号">2023010101</a-descriptions-item>
        <a-descriptions-item label="状态">
          <a-tag color="green">正常</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="余额">￥99.99</a-descriptions-item>
        <a-descriptions-item label="最后交易">2024-01-20 12:30</a-descriptions-item>
      </a-descriptions>
    </a-card>

    <!-- 快捷操作 -->
    <a-card class="quick-actions">
      <template #title>
        <span class="card-title">
          <icon-apps /> 快捷操作
        </span>
      </template>
      
      <a-grid :cols="isMobile ? 2 : 4" :colGap="12" :rowGap="16">
        <a-grid-item>
          <div class="action-item" @click="router.push('/student/report-loss')">
            <icon-notification class="action-icon" />
            <span>卡片挂失</span>
          </div>
        </a-grid-item>
        <a-grid-item>
          <div class="action-item" @click="router.push('/student/found-card')">
            <icon-heart class="action-icon" />
            <span>招领登记</span>
          </div>
        </a-grid-item>
        <a-grid-item>
          <div class="action-item" @click="router.push('/student/search')">
            <icon-search class="action-icon" />
            <span>查询中心</span>
          </div>
        </a-grid-item>
        <a-grid-item>
          <div class="action-item">
            <icon-question-circle class="action-icon" />
            <span>帮助中心</span>
          </div>
        </a-grid-item>
      </a-grid>
    </a-card>

    <!-- 最近通知 -->
    <a-card class="notice-list">
      <template #title>
        <span class="card-title">
          <icon-notification /> 最近通知
        </span>
      </template>
      
      <a-list>
        <a-list-item>
          <a-space direction="vertical" style="width: 100%">
            <div class="notice-header">
              <span class="notice-title">系统维护通知</span>
              <span class="notice-time">2024-01-20</span>
            </div>
            <div class="notice-content">
              系统将于本周六凌晨2:00-4:00进行例行维护，期间可能影响部分功能使用。
            </div>
          </a-space>
        </a-list-item>
        <a-list-item>
          <a-space direction="vertical" style="width: 100%">
            <div class="notice-header">
              <span class="notice-title">新功能上线公告</span>
              <span class="notice-time">2024-01-18</span>
            </div>
            <div class="notice-content">
              校园卡挂失系统新增短信通知功能，挂失状态变更时将实时通知到您的手机。
            </div>
          </a-space>
        </a-list-item>
      </a-list>
    </a-card>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useDevice } from '../../../composables/useDevice'
import {
  IconCreditCard,
  IconApps,
  IconNotification,
  IconHeart,
  IconSearch,
  IconQuestionCircle
} from '@arco-design/web-vue/es/icon'

const router = useRouter()
const { isMobile } = useDevice()
</script>

<style scoped>
.student-home {
  max-width: 1200px;
  margin: 0 auto;
}

.card-info,
.quick-actions,
.notice-list {
  margin-bottom: 16px;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 8px;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 16px;
  background: #f5f5f5;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.action-item:hover {
  background: #e5e6eb;
}

.action-icon {
  font-size: 24px;
  color: #165DFF;
}

.notice-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.notice-title {
  font-weight: 500;
}

.notice-time {
  color: #86909c;
  font-size: 12px;
}

.notice-content {
  color: #4e5969;
  font-size: 14px;
}

@media screen and (max-width: 768px) {
  .student-home {
    margin: 0 -16px;
  }

  .card-info,
  .quick-actions,
  .notice-list {
    border-radius: 0;
  }

  .action-item {
    padding: 12px;
  }
}
</style> 