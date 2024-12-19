<template>
  <div class="lost-card-page">
    <!-- 顶部信息区 -->
    <div class="info-section">
      <div class="process-card">
        <h3><i class="fas fa-clipboard-list"></i> 挂失流程</h3>
        <div class="process-steps">
          <div class="step">
            <div class="step-icon">1</div>
            <div class="step-content">
              <h4>填写挂失信息</h4>
              <p>提供详细的卡片信息</p>
            </div>
          </div>
          <div class="step-arrow"><i class="fas fa-chevron-right"></i></div>
          <div class="step">
            <div class="step-icon">2</div>
            <div class="step-content">
              <h4>后台审核确认</h4>
              <p>工作人员核实信息</p>
            </div>
          </div>
          <div class="step-arrow"><i class="fas fa-chevron-right"></i></div>
          <div class="step">
            <div class="step-icon">3</div>
            <div class="step-content">
              <h4>办理补办手续</h4>
              <p>前往服务大厅办理</p>
            </div>
          </div>
        </div>
      </div>

      <div class="notice-card">
        <h3><i class="fas fa-exclamation-circle"></i> 挂失须知</h3>
        <ul>
          <li><i class="fas fa-check"></i> 挂失后原卡将立即停止使用</li>
          <li><i class="fas fa-check"></i> 请携带有效证件到服务大厅办理补卡</li>
          <li><i class="fas fa-check"></i> 补办费用：20元/张</li>
          <li><i class="fas fa-check"></i> 原卡余额将自动转入新卡</li>
        </ul>
      </div>

      <div class="time-card">
        <h3><i class="fas fa-clock"></i> 办理时间</h3>
        <div class="time-info">
          <p><i class="fas fa-calendar-alt"></i> 工作日：08:30-17:30</p>
          <p><i class="fas fa-calendar-alt"></i> 周末：09:30-16:30</p>
          <p><i class="fas fa-phone"></i> 服务热线：123-4567</p>
        </div>
      </div>
    </div>

    <!-- 操作区域 -->
    <div class="action-section">
      <div class="search-box">
        <input 
          type="text" 
          v-model="searchQuery"
          placeholder="搜索卡号/姓名..."
        >
        <i class="fas fa-search"></i>
      </div>
      <button class="btn-report" @click="showReportForm = true">
        <i class="fas fa-plus"></i> 申请挂失
      </button>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <table class="lost-table">
        <thead>
          <tr>
            <th>卡号</th>
            <th>姓名</th>
            <th>挂失时间</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="card in currentPageCards" :key="card.id">
            <td>{{ card.cardId }}</td>
            <td>{{ card.name }}</td>
            <td>{{ card.time }}</td>
            <td>
              <span :class="['status-badge', card.status]">
                {{ getStatusText(card.status) }}
              </span>
            </td>
            <td>
              <button class="btn-view" @click="viewDetails(card)">
                <i class="fas fa-eye"></i>
              </button>
              <button 
                v-if="canCancel(card)"
                class="btn-cancel" 
                @click="cancelLost(card)"
              >
                <i class="fas fa-times"></i>
              </button>
              <button 
                v-if="isAdmin"
                class="btn-delete" 
                @click="deleteCard(card)"
              >
                <i class="fas fa-trash"></i>
              </button>
            </td>
          </tr>
          <tr v-if="currentPageCards.length === 0">
            <td colspan="5" class="empty-tip">暂无挂失记录</td>
          </tr>
        </tbody>
      </table>

      <div class="pagination">
        <button 
          :disabled="currentPage === 1"
          @click="currentPage--"
          class="page-btn"
        >
          <i class="fas fa-chevron-left"></i>
        </button>
        <span class="page-info">{{ currentPage }} / {{ totalPages }}</span>
        <button 
          :disabled="currentPage === totalPages"
          @click="currentPage++"
          class="page-btn"
        >
          <i class="fas fa-chevron-right"></i>
        </button>
      </div>
    </div>

    <!-- 申请挂失弹窗 -->
    <div class="modal-overlay" v-if="showReportForm" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3><i class="fas fa-exclamation-circle"></i> 校园卡挂失申请</h3>
          <button class="btn-close" @click="closeModal">
            <i class="fas fa-times"></i>
          </button>
        </div>
        
        <form @submit.prevent="submitLostCard" class="modal-body">
          <div class="form-group">
            <label>卡号 <span class="required">*</span></label>
            <input 
              type="text" 
              v-model="formData.cardId"
              required
              placeholder="请输入校园卡号"
            >
          </div>

          <div class="form-group">
            <label>姓名 <span class="required">*</span></label>
            <input 
              type="text" 
              v-model="formData.name"
              required
              placeholder="请输入持卡人姓名"
            >
          </div>

          <div class="form-group">
            <label>联系电话 <span class="required">*</span></label>
            <input 
              type="tel" 
              v-model="formData.phone"
              required
              placeholder="请输入联系电话"
            >
          </div>

          <div class="form-group">
            <label>遗失时间 <span class="required">*</span></label>
            <input 
              type="datetime-local" 
              v-model="formData.lostTime"
              required
            >
          </div>

          <div class="form-group">
            <label>遗失地点</label>
            <input 
              type="text" 
              v-model="formData.location"
              placeholder="请输入可能的遗失地点"
            >
          </div>

          <div class="form-group">
            <label>遗失原因</label>
            <textarea 
              v-model="formData.reason"
              rows="3"
              placeholder="请简要描述遗失原因"
            ></textarea>
          </div>

          <div class="form-footer">
            <button type="button" class="btn-cancel" @click="closeModal">取消</button>
            <button type="submit" class="btn-submit" :disabled="isSubmitting">
              <span v-if="isSubmitting" class="spinner"></span>
              {{ isSubmitting ? '提交中...' : '提交申请' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped>
.lost-card-page {
  padding: 20px;
  background: #f8f9fa;
}

/* 信息区样式 */
.info-section {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 30px;
}

.process-card, .notice-card, .time-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}

.process-card h3, .notice-card h3, .time-card h3 {
  font-size: 18px;
  color: #2c3e50;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 流程步骤样式 */
.process-steps {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.step {
  display: flex;
  align-items: center;
  gap: 12px;
}

.step-icon {
  width: 32px;
  height: 32px;
  background: #e3f2fd;
  color: #1976d2;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
}

.step-content h4 {
  font-size: 14px;
  margin: 0;
  color: #2c3e50;
}

.step-content p {
  font-size: 12px;
  color: #666;
  margin: 4px 0 0 0;
}

.step-arrow {
  color: #999;
}

/* 须知列表样式 */
.notice-card ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.notice-card li {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 0;
  color: #666;
  font-size: 14px;
}

.notice-card li i {
  color: #4caf50;
}

/* 时间信息样式 */
.time-info p {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 12px 0;
  color: #666;
  font-size: 14px;
}

/* 操作区样式 */
.action-section {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.search-box {
  position: relative;
  width: 300px;
}

.search-box input {
  width: 100%;
  padding: 10px 16px;
  padding-right: 40px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s;
}

.search-box input:focus {
  outline: none;
  border-color: #4facfe;
  box-shadow: 0 0 0 3px rgba(79,172,254,0.1);
}

.search-box i {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #666;
}

.btn-report {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-report:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(79,172,254,0.2);
}

/* 表格样式 */
.table-container {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}

.lost-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;
}

.lost-table th {
  background: #f8f9fa;
  padding: 12px;
  text-align: left;
  color: #2c3e50;
  font-weight: 600;
}

.lost-table td {
  padding: 12px;
  border-top: 1px solid #eee;
}

.lost-table tr:hover {
  background: #f8f9fa;
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

.btn-view, .btn-cancel {
  padding: 6px 10px;
  border: none;
  border-radius: 4px;
  margin-right: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-view {
  background: #e3f2fd;
  color: #1976d2;
}

.btn-cancel {
  background: #ffebee;
  color: #d32f2f;
}

.btn-view:hover, .btn-cancel:hover {
  transform: translateY(-2px);
}

.empty-tip {
  text-align: center;
  color: #999;
  padding: 30px 0;
}

/* 响应式调整 */
@media (max-width: 1024px) {
  .info-section {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .info-section {
    grid-template-columns: 1fr;
  }
  
  .action-section {
    flex-direction: column;
    gap: 15px;
  }
  
  .search-box {
    width: 100%;
  }
}

/* 添加弹窗相关样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 12px;
  width: 90%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  padding: 20px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  margin: 0;
  font-size: 18px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.btn-close {
  background: none;
  border: none;
  font-size: 20px;
  color: #666;
  cursor: pointer;
}

.modal-body {
  padding: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #2c3e50;
  font-weight: 500;
}

.required {
  color: #ff4444;
  margin-left: 4px;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
  transition: all 0.3s;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #4facfe;
  box-shadow: 0 0 0 3px rgba(79,172,254,0.1);
}

.form-footer {
  margin-top: 30px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.btn-cancel,
.btn-submit {
  padding: 8px 20px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.btn-cancel {
  background: #f8f9fa;
  border: 1px solid #ddd;
  color: #666;
}

.btn-submit {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  border: none;
  color: white;
}

.btn-submit:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.spinner {
  display: inline-block;
  width: 16px;
  height: 16px;
  border: 2px solid #fff;
  border-top-color: transparent;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
  margin-right: 8px;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.additional-features {
  margin-top: 30px;
}

.query-section,
.notification-section {
  background: white;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}

.query-section h3,
.notification-section h3 {
  margin-bottom: 20px;
  color: #2c3e50;
  font-size: 18px;
}

/* 添加分页样式 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  gap: 12px;
}

.page-btn {
  width: 32px;
  height: 32px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background: white;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s;
}

.page-btn:hover:not(:disabled) {
  background: #f0f0f0;
  border-color: #ccc;
}

.page-btn:disabled {
  background: #f5f5f5;
  cursor: not-allowed;
  opacity: 0.6;
}

.page-info {
  color: #666;
  font-size: 14px;
}

/* 删除按钮样式 */
.btn-delete {
  background: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 4px 8px;
  margin-left: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-delete:hover {
  background: #c82333;
}
</style>

<script setup>
import { ref, onMounted, reactive, computed } from 'vue'
import { lostAPI } from '@/api/lost'
import { ElMessage, ElMessageBox } from 'element-plus'
import CardQuery from '@/components/CardQuery.vue'
import NotificationList from '@/components/NotificationList.vue'

const lostCards = ref([])
const searchQuery = ref('')
const showReportForm = ref(false)

// 表单数据
const formData = reactive({
  cardId: '',
  name: '',
  phone: '',
  lostTime: '',
  location: '',
  reason: ''
})

const isSubmitting = ref(false)

const currentPage = ref(1)
const pageSize = 10 // 每页显示数量

// 判断是否为管理员
const isAdmin = computed(() => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  return userInfo.role === 'admin'
})

// 计算当��页的数据
const currentPageCards = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  const end = start + pageSize
  return lostCards.value.slice(start, end)
})

// 计算总页数
const totalPages = computed(() => {
  return Math.ceil(lostCards.value.length / pageSize)
})

// 获取挂失列表
const getLostCards = async () => {
  try {
    const data = await lostAPI.getList()
    console.log('获取到的数据:', data) // 调试用
    lostCards.value = data
  } catch (error) {
    console.error('获取挂失列表失败:', error)
  }
}

// 状态文本映射
const getStatusText = (status) => {
  const statusMap = {
    'pending': '处理中',
    'completed': '已完成',
    'cancelled': '已取消'
  }
  return statusMap[status] || status
}

// 状态样式映射
const getStatusClass = (status) => {
  const classMap = {
    'pending': 'badge bg-warning',
    'completed': 'badge bg-success',
    'cancelled': 'badge bg-secondary'
  }
  return classMap[status] || 'badge bg-primary'
}

// 查看详情
const viewDetails = (card) => {
  console.log('查看详情:', card)
  // 实现查看详情的逻辑
}

// 关闭弹窗
const closeModal = () => {
  showReportForm.value = false
  // 重置表单
  Object.keys(formData).forEach(key => {
    formData[key] = ''
  })
}

// 提交挂失申请
const submitLostCard = async () => {
  try {
    isSubmitting.value = true
    
    // 构造提交数据
    const submitData = {
      ...formData,
      time: new Date().toLocaleString(),
      status: 'pending'
    }

    // 调用API提交数据
    await lostAPI.create(submitData)
    
    // 提示成功
    ElMessage.success('挂失申请提交成功')
    
    // 刷新列表
    getLostCards()
    
    // 关闭弹窗
    closeModal()
  } catch (error) {
    console.error('提交挂失申请失败:', error)
    ElMessage.error('提交失败，请重试')
  } finally {
    isSubmitting.value = false
  }
}

// 判断是否可以取消挂失
const canCancel = (card) => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  // 管理员可以取消所有挂失
  if (userInfo.role === 'admin') return true
  // 普通用户只能取消自己的挂失，且状态为 pending
  return card.userId === userInfo.id && card.status === 'pending'
}

// 取消挂失
const cancelLost = async (card) => {
  try {
    await ElMessageBox.confirm('确定要取消这条挂失申请吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await lostAPI.cancel(card.id)
    ElMessage.success('已取消挂失申请')
    getLostCards() // 刷新列表
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消挂失失败:', error)
      ElMessage.error('操作失败，请重试')
    }
  }
}

// 删除卡片
const deleteCard = async (card) => {
  try {
    await ElMessageBox.confirm('确定要删除这条挂失记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await lostAPI.delete(card.id)
    ElMessage.success('删除成功')
    getLostCards() // 刷新列表
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败，请重试')
    }
  }
}

onMounted(() => {
  getLostCards()
})
</script> 