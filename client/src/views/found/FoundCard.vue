<template>
  <div class="found-card-container">
    <!-- 页面标题区域 -->
    <div class="page-header">
      <div class="header-content">
        <h2><i class="fas fa-hand-holding-heart"></i> 校园卡招领</h2>
        <p class="text-muted">感谢您的善举，请填写以下信息完成招领登记</p>
      </div>
    </div>

    <div class="row g-4">
      <!-- 左侧表单区域 -->
      <div class="col-lg-8">
        <div class="form-card">
          <form @submit.prevent="handleSubmit" class="needs-validation" novalidate>
            <!-- 卡片信息区域 -->
            <div class="card-info-section mb-4">
              <h5 class="section-title">卡片信息</h5>
              <div class="form-content">
                <div class="form-floating mb-3">
                  <input type="text" class="form-control" id="cardNumber" 
                         v-model="formData.cardNumber" placeholder="请输入校园卡号">
                  <label for="cardNumber">校园卡号（如果卡面清晰可见）</label>
                </div>

                <div class="form-floating mb-3">
                  <input type="datetime-local" class="form-control" id="foundTime" 
                         v-model="formData.foundTime" required>
                  <label for="foundTime">拾获时间</label>
                </div>

                <div class="form-floating mb-3">
                  <input type="text" class="form-control" id="foundLocation" 
                         v-model="formData.foundLocation" placeholder="请输入拾获地点" required>
                  <label for="foundLocation">拾获地点</label>
                </div>

                <div class="form-floating mb-3">
                  <textarea class="form-control" id="description" 
                           v-model="formData.description" style="height: 100px" 
                           placeholder="请描述卡片外观特征"></textarea>
                  <label for="description">卡片外观描述</label>
                </div>
              </div>
            </div>

            <!-- 保管信息区域 -->
            <div class="keep-info-section mb-4">
              <h5 class="section-title">保管信息</h5>
              <div class="form-content">
                <div class="keep-location-options mb-3">
                  <label class="form-label">临时保管位置</label>
                  <div class="row g-3">
                    <div class="col-md-4">
                      <div class="keep-location-item" 
                           :class="{ active: formData.keepLocation === 'service' }"
                           @click="formData.keepLocation = 'service'">
                        <i class="fas fa-building"></i>
                        <span>已交到服务中心</span>
                      </div>
                    </div>
                    <div class="col-md-4">
                      <div class="keep-location-item"
                           :class="{ active: formData.keepLocation === 'security' }"
                           @click="formData.keepLocation = 'security'">
                        <i class="fas fa-shield-alt"></i>
                        <span>已交到保卫处</span>
                      </div>
                    </div>
                    <div class="col-md-4">
                      <div class="keep-location-item"
                           :class="{ active: formData.keepLocation === 'self' }"
                           @click="formData.keepLocation = 'self'">
                        <i class="fas fa-user"></i>
                        <span>暂时自行保管</span>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="form-floating mb-3">
                  <input type="tel" class="form-control" id="contactPhone" 
                         v-model="formData.contactPhone" placeholder="请输入联系电话" required>
                  <label for="contactPhone">联系电话</label>
                </div>

                <div class="form-floating mb-3">
                  <textarea class="form-control" id="remark" 
                           v-model="formData.remark" style="height: 100px" 
                           placeholder="其他需要说明的信息"></textarea>
                  <label for="remark">备注信息</label>
                </div>
              </div>
            </div>

            <!-- 提交区域 -->
            <div class="submit-section">
              <div class="form-check mb-3">
                <input type="checkbox" class="form-check-input" id="agreement" 
                       v-model="formData.agreement" required>
                <label class="form-check-label" for="agreement">
                  我承诺以上信息真实有效
                </label>
              </div>

              <button type="submit" class="btn btn-success btn-lg w-100" 
                      :disabled="!formData.agreement || loading">
                <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
                {{ loading ? '提交中...' : '提交招领信息' }}
              </button>
            </div>
          </form>
        </div>
      </div>

      <!-- 右侧提示区域 -->
      <div class="col-lg-4 ">
        <div class="info-card mb-4">
          <div class="card-header">
            <h5><i class="fas fa-info-circle"></i> 招领须知</h5>
          </div>
          <div class="card-body">
            <div class="info-item">
              <i class="fas fa-check-circle text-success"></i>
              <span>建议将拾获的校园卡交到就近服务中心</span>
            </div>
            <div class="info-item">
              <i class="fas fa-sync text-primary"></i>
              <span>系统会自动匹配失主信息</span>
            </div>
            <div class="info-item">
              <i class="fas fa-phone text-info"></i>
              <span>请保持电话畅通，方便失主联系</span>
            </div>
          </div>
        </div>

        <div class="process-card">
          <div class="card-header">
            <h5><i class="fas fa-tasks"></i> 招领流程</h5>
          </div>
          <div class="card-body">
            <div class="process-timeline">
              <div class="process-item">
                <div class="process-icon">
                  <i class="fas fa-edit"></i>
                </div>
                <div class="process-content">
                  <h6>登记信息</h6>
                  <p>填写完整的招领信息</p>
                </div>
              </div>
              <div class="process-item">
                <div class="process-icon">
                  <i class="fas fa-search"></i>
                </div>
                <div class="process-content">
                  <h6>等待匹配</h6>
                  <p>系统自动匹配失主信息</p>
                </div>
              </div>
              <div class="process-item">
                <div class="process-icon">
                  <i class="fas fa-handshake"></i>
                </div>
                <div class="process-content">
                  <h6>联系交接</h6>
                  <p>与失主确认后完成交接</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useCardStore } from '@/stores/cardStore'
import { foundAPI } from '@/api/found'
import { ElMessage } from 'element-plus'

const cardStore = useCardStore()
const loading = ref(false)

const formData = reactive({
  cardNumber: '',
  foundTime: '',
  foundLocation: '',
  description: '',
  keepLocation: 'service',
  contactPhone: '',
  remark: '',
  agreement: false
})

const handleSubmit = async () => {
  // 表单验证
  if (!formData.cardNumber || !formData.foundTime || !formData.foundLocation || !formData.contactPhone) {
    ElMessage.warning('请填写完整信息')
    return
  }

  if (!formData.agreement) {
    ElMessage.warning('请先同意招领须知')
    return
  }

  try {
    loading.value = true
    const userInfo = JSON.parse(localStorage.getItem('userInfo'))
    
    await foundAPI.create({
      cardId: formData.cardNumber,
      location: formData.foundLocation,
      time: formData.foundTime,
      contact: formData.contactPhone,
      description: formData.description,
      status: 'waiting',
      createTime: new Date().toISOString(),
      userId: userInfo.id,
      finder: userInfo.username
    })
    
    ElMessage.success('提交成功')
    // 重置表单
    Object.keys(formData).forEach(key => {
      formData[key] = ''
    })
    formData.agreement = false
    
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error(error.message || '提交失败，请重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.found-card-container {
  padding: 1.5rem;
}

.page-header {
  background: linear-gradient(135deg, #f6f9fc 0%, #ecf1f7 100%);
  padding: 2rem;
  border-radius: 12px;
  margin-bottom: 2rem;
}

.page-header h2 {
  color: #2c3e50;
  margin-bottom: 0.5rem;
}

.page-header i {
  margin-right: 0.5rem;
}

.form-card {
  background: #fff;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 0 20px rgba(0,0,0,0.03);
}

.section-title {
  color: #2c3e50;
  margin-bottom: 1.5rem;
  font-weight: 600;
}

.form-content {
  background: #f8f9fa;
  padding: 1.5rem;
  border-radius: 8px;
}

.keep-location-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 1.5rem;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.keep-location-item:hover {
  border-color: #198754;
  background: #f8f9fa;
}

.keep-location-item.active {
  border-color: #198754;
  background: #f0f9f6;
}

.keep-location-item i {
  font-size: 1.5rem;
  margin-bottom: 0.5rem;
  color: #6c757d;
}

.keep-location-item.active i {
  color: #198754;
}

.info-card, .process-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 0 20px rgba(0,0,0,0.03);
}

.info-card .card-header, .process-card .card-header {
  background: transparent;
  border-bottom: 1px solid #f0f0f0;
  padding: 1.25rem;
}

.info-card .card-header h5, .process-card .card-header h5 {
  margin: 0;
  color: #2c3e50;
  font-weight: 600;
}

.info-item {
  display: flex;
  align-items: center;
  padding: 0.75rem 0;
  border-bottom: 1px solid #f0f0f0;
}

.info-item:last-child {
  border-bottom: none;
}

.info-item i {
  margin-left: 20px;
  margin-right: 1rem;
  font-size: 1.25rem;
}

.process-timeline {
  padding: 1rem 0;
}

.process-item {
  display: flex;
  align-items: flex-start;
  padding: 1rem 0;
  position: relative;
}

.process-item:not(:last-child)::after {
  content: '';
  position: absolute;
  left: 1.5rem;
  top: 3rem;
  bottom: -1rem;
  width: 2px;
  background: #e9ecef;
}

.process-icon {
  width: 3rem;
  height: 3rem;
  background: #f8f9fa;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 1rem;
  position: relative;
  z-index: 1;
}

.process-icon i {
  margin-left: 10px;
  color: #198754;
  font-size: 1.25rem;
}

.process-content {
  flex: 1;
}

.process-content h6 {
  margin: 0 0 0.25rem;
  color: #2c3e50;
}

.process-content p {
  margin: 0;
  color: #6c757d;
  font-size: 0.875rem;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .form-card {
    padding: 1rem;
  }
  
  .keep-location-item {
    margin-bottom: 1rem;
  }
}
</style> 