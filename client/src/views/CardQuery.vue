<template>
  <div class="card-query-page">
    <div class="page-header">
      <h2><i class="fas fa-search"></i> 卡片查询</h2>
      <p class="sub-title">可查询挂失/招领卡片的处理状态</p>
    </div>

    <div class="query-container">
      <div class="query-tabs">
        <div 
          class="tab-item" 
          :class="{ active: activeTab === 'lost' }"
          @click="switchTab('lost')"
        >
          <i class="fas fa-exclamation-circle"></i>
          挂失查询
        </div>
        <div 
          class="tab-item" 
          :class="{ active: activeTab === 'found' }"
          @click="switchTab('found')"
        >
          <i class="fas fa-search"></i>
          招领查询
        </div>
      </div>

      <div class="query-form">
        <div class="form-group">
          <input 
            type="text" 
            v-model="queryForm.cardId"
            :placeholder="activeTab === 'lost' ? '请输入挂失卡号查询进度' : '请输入拾获卡号查询认领状态'"
            @keyup.enter="handleQuery"
          >
          <button class="btn-query" @click="handleQuery" :disabled="isQuerying">
            <i class="fas fa-search"></i> 
            {{ isQuerying ? '查询中...' : '查询' }}
          </button>
        </div>
      </div>

      <!-- 查询提示 -->
      <div v-if="!hasSearched" class="empty-state">
        <i class="fas fa-search"></i>
        <p>请输入卡号进行查询</p>
        <p class="hint">支持查询最近30天内的记录</p>
      </div>

      <!-- 无结果提示 -->
      <div v-else-if="hasSearched && !queryResult" class="empty-state">
        <i class="fas fa-exclamation-circle"></i>
        <p>未找到相关记录</p>
        <p class="hint">请确认卡号是否正确</p>
      </div>

      <!-- 查询结果 -->
      <div v-else-if="queryResult" class="query-result">
        <div class="result-card">
          <div class="card-header">
            <div class="header-left">
              <h4>查询结果</h4>
              <span class="time">{{ queryResult.time }}</span>
            </div>
            <span :class="['status-badge', queryResult.status]">
              {{ getStatusText(queryResult.status) }}
            </span>
          </div>
          
          <div class="card-body">
            <div class="info-grid">
              <div class="info-item">
                <label>卡号</label>
                <span>{{ queryResult.cardId }}</span>
              </div>
              <div class="info-item">
                <label>状态</label>
                <span>{{ getStatusDetail(queryResult) }}</span>
              </div>
              <div class="info-item">
                <label>地点</label>
                <span>{{ queryResult.location || '未知' }}</span>
              </div>
              <div class="info-item">
                <label>处理进度</label>
                <div class="progress-bar">
                  <div 
                    class="progress" 
                    :style="{ width: getProgressWidth(queryResult.status) }"
                    :class="queryResult.status"
                  ></div>
                </div>
              </div>
            </div>
          </div>

          <div v-if="showActionGuide(queryResult)" class="card-footer">
            <div class="action-guide">
              <i class="fas fa-info-circle"></i>
              <span>{{ getActionGuide(queryResult) }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { lostAPI } from '@/api/lost'
import { foundAPI } from '@/api/found'

const activeTab = ref('lost')
const queryResult = ref(null)
const hasSearched = ref(false)
const isQuerying = ref(false)
const queryForm = reactive({
  cardId: ''
})

// 切换标签
const switchTab = (tab) => {
  activeTab.value = tab
  queryResult.value = null
  hasSearched.value = false
  queryForm.cardId = ''
}

// 处理查询
const handleQuery = async () => {
  if (!queryForm.cardId.trim()) {
    ElMessage.warning('请输入卡号')
    return
  }

  isQuerying.value = true
  hasSearched.value = true

  try {
    let result = null
    if (activeTab.value === 'lost') {
      // 查询挂失状态
      const response = await lostAPI.getList()
      result = response.find(item => item.cardId === queryForm.cardId)
    } else {
      // 查询招领状态
      const response = await foundAPI.getList()
      result = response.find(item => item.cardId === queryForm.cardId)
    }
    
    queryResult.value = result
    
    if (!result) {
      ElMessage.warning('未找到相关记录')
    }
  } catch (error) {
    console.error('查询失败:', error)
    ElMessage.error('查询失败，请重试')
  } finally {
    isQuerying.value = false
  }
}

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    'pending': '处理中',
    'completed': '已完成',
    'cancelled': '已取消',
    'waiting': '待认领'
  }
  return statusMap[status] || status
}

// 获取进度条宽度
const getProgressWidth = (status) => {
  const progressMap = {
    'pending': '50%',
    'completed': '100%',
    'cancelled': '100%',
    'waiting': '75%'
  }
  return progressMap[status] || '0%'
}

// 获取详细状态说明
const getStatusDetail = (result) => {
  if (activeTab.value === 'lost') {
    if (result.status === 'completed') return '已找到，请前往服务中心认领'
    if (result.status === 'pending') return '正在处理中，请耐心等待'
    return '已取消处理'
  }
  return result.status === 'waiting' ? '等待失主认领中' : '已被失主认领'
}

// 是否显示操作指引
const showActionGuide = (result) => {
  return result.status === 'completed' || result.status === 'waiting'
}

// 获取操作指引文本
const getActionGuide = (result) => {
  if (activeTab.value === 'lost' && result.status === 'completed') {
    return '请携带有效证件到服务中心认领您的校园卡'
  }
  if (activeTab.value === 'found' && result.status === 'waiting') {
    return '感谢您的拾获登记，我们会及时通知失主前来认领'
  }
  return ''
}
</script>

<style scoped>
.card-query-page {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 24px;
  text-align: center;
}

.page-header h2 {
  color: #2c3e50;
  margin-bottom: 8px;
}

.sub-title {
  color: #666;
  font-size: 14px;
}

.query-container {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
  padding: 24px;
}

.query-tabs {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  color: #666;
}

.tab-item i {
  margin-right: 8px;
}

.tab-item.active {
  background: #4facfe;
  color: white;
}

.query-form {
  margin-bottom: 24px;
}

.form-group {
  display: flex;
  gap: 12px;
}

.form-group input {
  flex: 1;
  padding: 12px 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s;
}

.form-group input:focus {
  border-color: #4facfe;
  box-shadow: 0 0 0 3px rgba(79,172,254,0.1);
  outline: none;
}

.btn-query {
  background: #4facfe;
  color: white;
  border: none;
  padding: 0 24px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-query:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.empty-state {
  text-align: center;
  padding: 40px 0;
  color: #666;
}

.empty-state i {
  font-size: 48px;
  color: #ddd;
  margin-bottom: 16px;
}

.empty-state .hint {
  font-size: 14px;
  color: #999;
  margin-top: 8px;
}

.query-result {
  margin-top: 24px;
}

.result-card {
  border: 1px solid #eee;
  border-radius: 8px;
}

.card-header {
  padding: 16px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-left h4 {
  margin: 0;
  color: #2c3e50;
}

.time {
  color: #999;
  font-size: 14px;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  padding: 20px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-item label {
  color: #666;
  font-size: 14px;
}

.progress-bar {
  height: 6px;
  background: #f0f0f0;
  border-radius: 3px;
  overflow: hidden;
}

.progress {
  height: 100%;
  transition: width 0.3s ease;
}

.progress.pending {
  background: #ffc107;
}

.progress.completed {
  background: #28a745;
}

.progress.waiting {
  background: #17a2b8;
}

.progress.cancelled {
  background: #dc3545;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
}

.status-badge.pending {
  background: #fff3cd;
  color: #856404;
}

.status-badge.completed {
  background: #d4edda;
  color: #155724;
}

.status-badge.cancelled {
  background: #f8f9fa;
  color: #6c757d;
}

.status-badge.waiting {
  background: #cce5ff;
  color: #004085;
}

.card-footer {
  padding: 16px;
  border-top: 1px solid #eee;
  background: #f8f9fa;
}

.action-guide {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #666;
}

.action-guide i {
  color: #4facfe;
}

@media (max-width: 768px) {
  .info-grid {
    grid-template-columns: 1fr;
  }
}
</style> 