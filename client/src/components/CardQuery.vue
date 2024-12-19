<template>
  <div class="query-container">
    <div class="query-tabs">
      <div 
        class="tab-item" 
        :class="{ active: activeTab === 'lost' }"
        @click="activeTab = 'lost'"
      >
        挂失查询
      </div>
      <div 
        class="tab-item" 
        :class="{ active: activeTab === 'found' }"
        @click="activeTab = 'found'"
      >
        招领查询
      </div>
    </div>

    <div class="query-form">
      <div class="form-group">
        <input 
          type="text" 
          v-model="queryForm.cardId"
          :placeholder="activeTab === 'lost' ? '请输入挂失卡号' : '请输入拾获卡号'"
        >
        <button class="btn-query" @click="handleQuery">
          <i class="fas fa-search"></i> 查询
        </button>
      </div>
    </div>

    <!-- 查询结果展示 -->
    <div v-if="queryResult" class="query-result">
      <div class="result-card">
        <div class="card-header">
          <h4>查询结果</h4>
          <span :class="['status-badge', queryResult.status]">
            {{ getStatusText(queryResult.status) }}
          </span>
        </div>
        
        <div class="card-body">
          <div class="info-item">
            <label>卡号：</label>
            <span>{{ queryResult.cardId }}</span>
          </div>
          <div class="info-item">
            <label>状态：</label>
            <span>{{ getStatusDetail(queryResult) }}</span>
          </div>
          <div class="info-item">
            <label>时间：</label>
            <span>{{ queryResult.time }}</span>
          </div>
          <div v-if="queryResult.location" class="info-item">
            <label>地点：</label>
            <span>{{ queryResult.location }}</span>
          </div>
        </div>

        <div v-if="showAction" class="card-footer">
          <button class="btn-action" @click="handleAction">
            {{ getActionText() }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { lostAPI } from '@/api/lost'
import { foundAPI } from '@/api/found'
import { ElMessage } from 'element-plus'

const activeTab = ref('lost')
const queryResult = ref(null)
const queryForm = reactive({
  cardId: ''
})

// 处理查询
const handleQuery = async () => {
  if (!queryForm.cardId) {
    ElMessage.warning('请输入卡号')
    return
  }

  try {
    if (activeTab.value === 'lost') {
      // 查询挂失状态
      const result = await lostAPI.getDetail(queryForm.cardId)
      queryResult.value = result
    } else {
      // 查询招领状态
      const result = await foundAPI.getDetail(queryForm.cardId)
      queryResult.value = result
    }
  } catch (error) {
    console.error('查询失败:', error)
    ElMessage.error('查询失败，请重试')
  }
}

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    'pending': '处理中',
    'completed': '已完成',
    'cancelled': '已取消',
    'waiting': '待认领',
    'claimed': '已完成'
  }
  return statusMap[status] || status
}

// 获取进度条宽度
const getProgressWidth = (status) => {
  const progressMap = {
    'pending': '50%',
    'completed': '100%',
    'cancelled': '100%',
    'waiting': '75%',
    'claimed': '100%'
  }
  return progressMap[status] || '0%'
}

// 获取进度条颜色
const getProgressColor = (status) => {
  const colorMap = {
    'pending': 'bg-warning',
    'completed': 'bg-success',
    'cancelled': 'bg-secondary',
    'waiting': 'bg-info',
    'claimed': 'bg-success'
  }
  return colorMap[status] || 'bg-primary'
}

// 获取详细状���描述
const getStatusDetail = (result) => {
  if (activeTab.value === 'lost') {
    if (result.status === 'pending') {
      return '您的挂失申请正在处理中'
    }
    if (result.status === 'completed') {
      return '您的挂失已处理完成，请及时办理补卡'
    }
  } else {
    if (result.status === 'waiting') {
      return '拾获卡片等待失主认领中'
    }
    if (result.status === 'claimed') {
      return '卡片已成功归还失主'
    }
  }
  return ''
}

// 是否显示操作按钮
const showAction = computed(() => {
  if (!queryResult.value) return false
  return activeTab.value === 'lost' && queryResult.value.status === 'pending'
})

// 获取操作按钮文本
const getActionText = () => {
  return activeTab.value === 'lost' ? '取消挂失' : '确认认领'
}

// 处理操作
const handleAction = async () => {
  try {
    if (activeTab.value === 'lost') {
      await lostAPI.update(queryResult.value.id, {
        ...queryResult.value,
        status: 'cancelled'
      })
      ElMessage.success('已取消挂失')
    }
    handleQuery() // 刷新查询结果
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败，请重试')
  }
}
</script>

<style scoped>
.query-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.query-tabs {
  display: flex;
  margin-bottom: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  padding: 4px;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 12px;
  cursor: pointer;
  border-radius: 6px;
  transition: all 0.3s;
}

.tab-item.active {
  background: white;
  color: #4facfe;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.query-form {
  margin-bottom: 20px;
}

.form-group {
  display: flex;
  gap: 10px;
}

.form-group input {
  flex: 1;
  padding: 10px 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
}

.btn-query {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  cursor: pointer;
}

.query-result {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
  overflow: hidden;
}

.result-card {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.info-item {
  display: flex;
  margin-bottom: 12px;
}

.info-item label {
  width: 80px;
  color: #666;
}

.card-footer {
  margin-top: 20px;
  text-align: right;
}

.btn-action {
  background: #4facfe;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
}
</style> 