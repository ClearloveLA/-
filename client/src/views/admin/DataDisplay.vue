<template>
  <div class="data-display-page">
    <div class="page-header">
      <h2><i class="fas fa-chart-bar"></i> 数据统计</h2>
      <p class="sub-title">校园卡挂失与招领数据分析</p>
    </div>

    <!-- 数据卡片 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-exclamation-circle"></i>
        </div>
        <div class="stat-info">
          <h3>{{ stats.totalLost }}</h3>
          <p>总挂失数</p>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-search"></i>
        </div>
        <div class="stat-info">
          <h3>{{ stats.totalFound }}</h3>
          <p>总招领数</p>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-check-circle"></i>
        </div>
        <div class="stat-info">
          <h3>{{ stats.matchedCount }}</h3>
          <p>成功配对</p>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-icon">
          <i class="fas fa-clock"></i>
        </div>
        <div class="stat-info">
          <h3>{{ stats.pendingCount }}</h3>
          <p>待处理</p>
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="charts-container">
      <div class="chart-card">
        <h3>每月挂失/招领统计</h3>
        <div class="chart" ref="monthlyChart"></div>
      </div>

      <div class="chart-card">
        <h3>处理状态分布</h3>
        <div class="chart" ref="statusChart"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import * as echarts from 'echarts'
import { lostAPI } from '@/api/lost'
import { foundAPI } from '@/api/found'

const stats = reactive({
  totalLost: 0,
  totalFound: 0,
  matchedCount: 0,
  pendingCount: 0
})

const monthlyChart = ref(null)
const statusChart = ref(null)

// 获取统计数据
const fetchStats = async () => {
  try {
    const [lostCards, foundCards] = await Promise.all([
      lostAPI.getList(),
      foundAPI.getList()
    ])
    
    stats.totalLost = lostCards.length
    stats.totalFound = foundCards.length
    stats.matchedCount = foundCards.filter(card => card.status === 'claimed').length
    stats.pendingCount = lostCards.filter(card => card.status === 'pending').length
    
    initCharts(lostCards, foundCards)
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

// 初始化图表
const initCharts = (lostCards, foundCards) => {
  // 月度统计图表
  const monthlyChartInstance = echarts.init(monthlyChart.value)
  monthlyChartInstance.setOption({
    // ... 图表配置
  })

  // 状态分布图表
  const statusChartInstance = echarts.init(statusChart.value)
  statusChartInstance.setOption({
    // ... 图表配置
  })
}

onMounted(() => {
  fetchStats()
})
</script>

<style scoped>
.data-display-page {
  padding: 20px;
}

.page-header {
  margin-bottom: 30px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 15px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}

.charts-container {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.chart-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}

.chart {
  height: 400px;
}
</style> 