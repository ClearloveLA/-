<template>
  <div class="search-center">
    <!-- 查询类型切换 -->
    <a-tabs v-model:activeKey="activeTab">
      <a-tab-pane key="loss" title="挂失记录">
        <!-- 挂失记录查询 -->
        <div class="search-section">
          <a-form :model="lossSearchForm" layout="inline">
            <a-form-item field="status">
              <a-select
                v-model="lossSearchForm.status"
                placeholder="处理状态"
                allow-clear
                style="width: 120px"
              >
                <a-option value="pending">处理中</a-option>
                <a-option value="found">已找到</a-option>
                <a-option value="completed">已完成</a-option>
              </a-select>
            </a-form-item>
            <a-form-item field="dateRange">
              <a-range-picker
                v-model="lossSearchForm.dateRange"
                style="width: 240px"
              />
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="searchLossRecords">
                查询
              </a-button>
            </a-form-item>
          </a-form>
        </div>

        <!-- 挂失记录列表 -->
        <a-table
          :data="lossRecords"
          :loading="loading"
          :pagination="pagination"
          @page-change="onPageChange"
        >
          <template #columns>
            <a-table-column title="挂失时间" data-index="reportTime">
              {{ ({ record }) => formatDate(record.reportTime) }}
            </a-table-column>
            <a-table-column title="卡号" data-index="cardNumber" />
            <a-table-column title="状态" data-index="status">
              <template #cell="{ record }">
                <a-tag :color="getStatusColor(record.status)">
                  {{ getStatusText(record.status) }}
                </a-tag>
              </template>
            </a-table-column>
            <a-table-column title="操作">
              <template #cell="{ record }">
                <a-space>
                  <a-button type="text" @click="viewDetail(record)">
                    详情
                  </a-button>
                  <a-button 
                    v-if="record.status === 'pending'"
                    type="text" 
                    status="danger"
                    @click="cancelLoss(record)"
                  >
                    取消挂失
                  </a-button>
                </a-space>
              </template>
            </a-table-column>
          </template>
        </a-table>
      </a-tab-pane>

      <a-tab-pane key="found" title="招领记录">
        <!-- 招领记录查询 -->
        <div class="search-section">
          <a-form :model="foundSearchForm" layout="inline">
            <a-form-item field="status">
              <a-select
                v-model="foundSearchForm.status"
                placeholder="处理状态"
                allow-clear
                style="width: 120px"
              >
                <a-option value="pending">待认领</a-option>
                <a-option value="claimed">已认领</a-option>
                <a-option value="completed">已完成</a-option>
              </a-select>
            </a-form-item>
            <a-form-item field="dateRange">
              <a-range-picker
                v-model="foundSearchForm.dateRange"
                style="width: 240px"
              />
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="searchFoundRecords">
                查询
              </a-button>
            </a-form-item>
          </a-form>
        </div>

        <!-- 招领记录列表 -->
        <a-table
          :data="foundRecords"
          :loading="loading"
          :pagination="pagination"
          @page-change="onPageChange"
        >
          <template #columns>
            <a-table-column title="登记时间" data-index="foundTime">
              {{ ({ record }) => formatDate(record.foundTime) }}
            </a-table-column>
            <a-table-column title="卡号" data-index="cardNumber" />
            <a-table-column title="拾获地点" data-index="foundLocation" />
            <a-table-column title="状态" data-index="status">
              <template #cell="{ record }">
                <a-tag :color="getStatusColor(record.status)">
                  {{ getStatusText(record.status) }}
                </a-tag>
              </template>
            </a-table-column>
            <a-table-column title="操作">
              <template #cell="{ record }">
                <a-button type="text" @click="viewDetail(record)">
                  详情
                </a-button>
              </template>
            </a-table-column>
          </template>
        </a-table>
      </a-tab-pane>
    </a-tabs>

    <!-- 详情抽屉 -->
    <a-drawer
      v-model:visible="drawerVisible"
      :width="drawerWidth"
      :title="drawerTitle"
    >
      <a-descriptions
        :data="detailData"
        :column="1"
        layout="horizontal"
      />
      <template #footer>
        <div style="text-align: right">
          <a-button @click="drawerVisible = false">关闭</a-button>
        </div>
      </template>
    </a-drawer>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useDevice } from '../../../composables/useDevice'
import { Message } from '@arco-design/web-vue'

const { isMobile } = useDevice()
const activeTab = ref('loss')
const loading = ref(false)
const drawerVisible = ref(false)
const currentDetail = ref(null)

// 查询表单数据
const lossSearchForm = ref({
  status: '',
  dateRange: []
})

const foundSearchForm = ref({
  status: '',
  dateRange: []
})

// 模拟数据
const lossRecords = ref([
  {
    id: 1,
    reportTime: '2024-01-20 14:30:00',
    cardNumber: '2023010101',
    status: 'pending'
  },
  {
    id: 2,
    reportTime: '2024-01-19 09:15:00',
    cardNumber: '2023010102',
    status: 'found'
  }
])

const foundRecords = ref([
  {
    id: 1,
    foundTime: '2024-01-20 15:00:00',
    cardNumber: '2023010103',
    foundLocation: '图书馆一楼',
    status: 'pending'
  }
])

// 分页配置
const pagination = {
  total: 50,
  current: 1,
  pageSize: 10
}

// 计算属性
const drawerWidth = computed(() => isMobile.value ? '100%' : '500px')
const drawerTitle = computed(() => 
  activeTab.value === 'loss' ? '挂失详情' : '招领详情'
)

const detailData = computed(() => {
  if (!currentDetail.value) return []
  
  return Object.entries(currentDetail.value).map(([key, value]) => ({
    label: getFieldLabel(key),
    value: value
  }))
})

// 方法
const formatDate = (date) => {
  return new Date(date).toLocaleString()
}

const getStatusColor = (status) => {
  const colors = {
    pending: 'orange',
    found: 'green',
    claimed: 'blue',
    completed: 'green'
  }
  return colors[status] || 'gray'
}

const getStatusText = (status) => {
  const texts = {
    pending: '处理中',
    found: '已找到',
    claimed: '已认领',
    completed: '已完成'
  }
  return texts[status] || status
}

const getFieldLabel = (field) => {
  const labels = {
    reportTime: '挂失时间',
    foundTime: '拾获时间',
    cardNumber: '卡号',
    foundLocation: '拾获地点',
    status: '状态'
  }
  return labels[field] || field
}

const searchLossRecords = () => {
  loading.value = true
  // 模拟API调用
  setTimeout(() => {
    loading.value = false
  }, 1000)
}

const searchFoundRecords = () => {
  loading.value = true
  // 模拟API调用
  setTimeout(() => {
    loading.value = false
  }, 1000)
}

const viewDetail = (record) => {
  currentDetail.value = record
  drawerVisible.value = true
}

const cancelLoss = async (record) => {
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    Message.success('取消挂失成功')
    // 更新列表
    const index = lossRecords.value.findIndex(item => item.id === record.id)
    if (index !== -1) {
      lossRecords.value[index].status = 'completed'
    }
  } catch (error) {
    Message.error('操作失败，请重试')
  }
}

const onPageChange = (page) => {
  pagination.current = page
  if (activeTab.value === 'loss') {
    searchLossRecords()
  } else {
    searchFoundRecords()
  }
}
</script>

<style scoped>
.search-center {
  max-width: 1200px;
  margin: 0 auto;
}

.search-section {
  margin-bottom: 16px;
}

@media screen and (max-width: 768px) {
  .search-center {
    margin: 0 -16px;
  }

  :deep(.arco-card) {
    border-radius: 0;
  }

  :deep(.arco-form-inline) {
    display: grid;
    grid-template-columns: 1fr;
    gap: 16px;
  }

  :deep(.arco-form-item) {
    margin-right: 0;
    margin-bottom: 0;
  }

  :deep(.arco-select),
  :deep(.arco-picker),
  :deep(.arco-btn) {
    width: 100%;
  }
}
</style> 