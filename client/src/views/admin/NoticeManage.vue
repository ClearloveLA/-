<template>
  <div class="notice-manage-page">
    <div class="page-header">
      <h2><i class="fas fa-bullhorn"></i> 公告管理</h2>
      <button class="btn-add" @click="showAddForm = true">
        <i class="fas fa-plus"></i> 发布公告
      </button>
    </div>

    <!-- 公告列表 -->
    <div class="notice-list">
      <div v-for="notice in notices" :key="notice.id" class="notice-item">
        <div class="notice-content">
          <div class="notice-header">
            <h3>{{ notice.title }}</h3>
          </div>
          <p>{{ notice.content }}</p>
          <div class="notice-footer">
            <span class="notice-time">
              <i class="fas fa-clock"></i> {{ notice.time }}
            </span>
          </div>
        </div>
        <div class="notice-actions">
          <button class="btn-edit" @click="editNotice(notice)">
            <i class="fas fa-edit"></i>
          </button>
          <button class="btn-delete" @click="deleteNotice(notice)">
            <i class="fas fa-trash"></i>
          </button>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="!notices.length" class="empty-state">
        <i class="fas fa-bullhorn"></i>
        <p>暂无公告</p>
      </div>
    </div>

    <!-- 添加/编辑公告弹窗 -->
    <el-dialog
      v-model="showAddForm"
      :title="editingNotice ? '编辑公告' : '发布公告'"
      width="500px"
    >
      <form @submit.prevent="submitNotice" class="notice-form">
        <div class="form-group">
          <label>标题</label>
          <input 
            type="text" 
            v-model="formData.title"
            required
            placeholder="请输入公告标题"
          >
        </div>

        <div class="form-group">
          <label>内容</label>
          <textarea 
            v-model="formData.content"
            rows="4"
            required
            placeholder="请输入公告内容"
          ></textarea>
        </div>

        <div class="form-footer">
          <button type="button" class="btn-cancel" @click="showAddForm = false">取消</button>
          <button type="submit" class="btn-submit">确定</button>
        </div>
      </form>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { noticeAPI } from '@/api/notice'

const notices = ref([])
const showAddForm = ref(false)
const editingNotice = ref(null)

const formData = reactive({
  title: '',
  content: ''
})

// 获取公告列表
const getNotices = async () => {
  try {
    const data = await noticeAPI.getList()
    notices.value = data.sort((a, b) => new Date(b.time) - new Date(a.time))
  } catch (error) {
    console.error('获取公告列表失败:', error)
    ElMessage.error('获取公告列表失败')
  }
}

// 编辑公告
const editNotice = (notice) => {
  editingNotice.value = notice
  formData.title = notice.title
  formData.content = notice.content
  showAddForm.value = true
}

// 删除公告
const deleteNotice = async (notice) => {
  try {
    await ElMessageBox.confirm('确定要删除这条公告吗？', '提示', {
      type: 'warning'
    })
    await noticeAPI.delete(notice.id)
    ElMessage.success('删除成功')
    getNotices()
    // 触发更新事件
    window.dispatchEvent(new CustomEvent('notice-update'))
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 提交公告
const submitNotice = async () => {
  try {
    if (editingNotice.value) {
      await noticeAPI.update(editingNotice.value.id, formData)
      ElMessage.success('更新成功')
    } else {
      await noticeAPI.create(formData)
      ElMessage.success('发布成功')
    }
    showAddForm.value = false
    getNotices()
    // 触发更新事件
    window.dispatchEvent(new CustomEvent('notice-update'))
    // 重置表单
    formData.title = ''
    formData.content = ''
    editingNotice.value = null
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('操作失败')
  }
}

onMounted(() => {
  getNotices()
})
</script>

<style scoped>
.notice-manage-page {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.btn-add {
  padding: 8px 16px;
  background: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
}

.notice-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.notice-item {
  background: white;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}

.notice-content {
  flex: 1;
}

.notice-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.notice-content h3 {
  margin: 0;
  font-size: 18px;
  color: #303133;
}

.notice-content p {
  margin: 12px 0;
  color: #606266;
  line-height: 1.6;
}

.notice-footer {
  color: #909399;
  font-size: 14px;
}

.notice-actions {
  display: flex;
  gap: 8px;
}

.notice-actions button {
  padding: 6px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-edit {
  background: #e6a23c;
  color: white;
}

.btn-delete {
  background: #f56c6c;
  color: white;
}

.empty-state {
  text-align: center;
  padding: 40px;
  color: #909399;
}

.empty-state i {
  font-size: 48px;
  margin-bottom: 16px;
}

/* 表单样式 */
.notice-form {
  padding: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #606266;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

.form-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 20px;
}

.btn-cancel {
  padding: 8px 20px;
  border: 1px solid #dcdfe6;
  background: white;
  border-radius: 4px;
  cursor: pointer;
}

.btn-submit {
  padding: 8px 20px;
  background: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style> 