<template>
  <div class="dashboard">
    <!-- 欢迎区域 -->
    <div class="welcome-banner mb-4">
      <h1>欢迎使用校园卡管理系统</h1>
      <p>今天是 {{ currentDate }}，{{ weekDay }}，祝您使用愉快</p>
      <div class="time-display">
        {{ currentTime }}
        <span class="weather">晴朗 3°C</span>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid mb-4">
      <div class="stat-card purple">
        <i class="fas fa-search"></i>
        <div class="stat-info">
          <h3>90</h3>
          <p>待认领卡片</p>
        </div>
      </div>
      
      <div class="stat-card green">
        <i class="fas fa-check-circle"></i>
        <div class="stat-info">
          <h3>20</h3>
          <p>今日已处理</p>
        </div>
      </div>
      
      <div class="stat-card orange">
        <i class="fas fa-bell"></i>
        <div class="stat-info">
          <h3>30</h3>
          <p>系统公告</p>
        </div>
      </div>
      
      <div class="stat-card blue">
        <i class="fas fa-users"></i>
        <div class="stat-info">
          <h3>0.5k</h3>
          <p>总用户数</p>
        </div>
      </div>
    </div>

    <div class="content-grid">
      <!-- 左侧内容 -->
      <div class="main-content">
        <!-- 系统公告 -->
        <div class="content-card">
          <div class="card-header">
            <h2><i class="fas fa-bullhorn"></i> 系统公告</h2>
            <a href="#" class="view-all">查看全部</a>
          </div>
          <div class="notice-list">
            <div v-if="notices.length === 0" class="empty-notice">
              暂无公告
            </div>
            <div v-else v-for="notice in notices" :key="notice.id" class="notice-item">
              <div class="notice-time">{{ notice.time }}</div>
              <div class="notice-content">
                <h3>{{ notice.title }}</h3>
                <p>{{ notice.content }}</p>
              </div>
            </div>
          </div>
        </div>

        <!-- 快捷操作区域 -->
        <div class="quick-actions-grid mb-4">
          <div class="action-card red">
            <router-link to="/lost">
              <i class="fas fa-exclamation-circle"></i>
              <span>卡片挂失</span>
            </router-link>
          </div>
          <div class="action-card blue">
            <router-link to="/found">
              <i class="fas fa-search"></i>
              <span>卡片招领</span>
            </router-link>
          </div>
          <div class="action-card green">
            <a href="#">
              <i class="fas fa-credit-card"></i>
              <span>余额查询</span>
            </a>
          </div>
          <div class="action-card purple">
            <a href="#">
              <i class="fas fa-history"></i>
              <span>消费记录</span>
            </a>
          </div>
        </div>

        <!-- 最新活动区域 -->
        <div class="recent-activities mb-4">
          <div class="section-header">
            <h2><i class="fas fa-history"></i> 最近活动</h2>
            <a href="#" class="view-all">查看全部</a>
          </div>
          <div class="activities-list">
            <div class="activity-item" v-for="activity in activities" :key="activity.id">
              <div class="activity-time">
                <span class="time">{{ activity.time }}</span>
              </div>
              <div class="activity-content">
                <div class="activity-title">{{ activity.title }}</div>
                <div class="activity-desc">{{ activity.content }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧内容 -->
      <div class="side-content">
        <!-- 温馨提示 -->
        <div class="content-card mt-4">
          <div class="card-header">
            <h2><i class="fas fa-info-circle"></i> 温馨提示</h2>
          </div>
          <div class="tips-list">
            <div class="tip-item">
              <i class="fas fa-check-circle"></i>
              <span>请妥善保管好您的校园卡</span>
            </div>
            <div class="tip-item">
              <i class="fas fa-exclamation-triangle"></i>
              <span>如遇卡片遗失请及时挂失</span>
            </div>
            <div class="tip-item">
              <i class="fas fa-info-circle"></i>
              <span>拾获他人卡片请及时登记</span>
            </div>
            <div class="tip-item">
              <i class="fas fa-shield-alt"></i>
              <span>定期修改密码保障安全</span>
            </div>
          </div>
        </div>

        <!-- 服务时间 -->
        <div class="content-card mt-4">
          <div class="card-header">
            <h2><i class="fas fa-clock"></i> 服务时间</h2>
          </div>
          <div class="service-hours">
            <div class="service-item">
              <div class="service-day">工作日</div>
              <div class="service-time">08:30 - 17:30</div>
            </div>
            <div class="service-item">
              <div class="service-day">周末及节假日</div>
              <div class="service-time">09:30 - 16:30</div>
            </div>
            <div class="contact-info">
              <p><i class="fas fa-phone"></i> 123-4567</p>
              <p><i class="fas fa-envelope"></i> support@school.edu</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { statsAPI } from '@/api/stats'
import { noticeAPI } from '@/api/notice'

const stats = ref({})
const notices = ref([])
const activities = ref([])

onMounted(async () => {
  try {
    // 获取统计数据
    stats.value = await statsAPI.getDashboard()
    
    // 获取公告列表
    notices.value = await noticeAPI.getList()
    
    // 获取最近活动
    activities.value = await statsAPI.getActivities()
  } catch (error) {
    console.error('获取数据失败:', error)
  }
})

// 添加时间日期相关
const currentTime = ref('')
const currentDate = ref('')
const weekDay = ref('')

const weekDays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']

const updateDateTime = () => {
  const now = new Date()
  
  // 更新时间
  currentTime.value = now.toLocaleTimeString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit'
  })
  
  // 更新日期
  currentDate.value = now.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
  
  // 更新星期
  weekDay.value = weekDays[now.getDay()]
}

// 定时器
let timer

onMounted(() => {
  updateDateTime()
  timer = setInterval(updateDateTime, 1000)
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})

// 获取公告列表
const getNotices = async () => {
  try {
    const data = await noticeAPI.getList()
    // 只显示已发布（isActive 为 true）的公告，并按时间倒序排序
    notices.value = data
      .filter(notice => notice.isActive === true)
      .sort((a, b) => new Date(b.time) - new Date(a.time))
  } catch (error) {
    console.error('获取公告列表失败:', error)
  }
}

onMounted(() => {
  getNotices()
  // 监听公告更新事件
  window.addEventListener('notice-update', getNotices)
})

onUnmounted(() => {
  // 移除事件监听
  window.removeEventListener('notice-update', getNotices)
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
}

/* 欢迎横幅 */
.welcome-banner {
  background: linear-gradient(135deg, #00b4db, #0083b0);
  padding: 30px;
  border-radius: 15px;
  color: white;
  position: relative;
  overflow: hidden;
}

.welcome-banner h1 {
  font-size: 24px;
  margin: 0;
  font-weight: 600;
}

.welcome-banner p {
  margin: 10px 0 0;
  opacity: 0.9;
}

.time-display {
  position: absolute;
  right: 30px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 28px;
  font-weight: 600;
}

.weather {
  display: block;
  font-size: 14px;
  text-align: right;
  opacity: 0.9;
}

/* 统计卡片网格 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.stat-card {
  background: white;
  padding: 20px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  gap: 15px;
  transition: transform 0.3s;
  cursor: pointer;
}

.stat-card:hover {
  transform: translateY(-5px);
}

.stat-card i {
  font-size: 24px;
  padding: 15px;
  border-radius: 10px;
}

.stat-info h3 {
  font-size: 24px;
  margin: 0;
  font-weight: 600;
}

.stat-info p {
  margin: 5px 0 0;
  color: #666;
  font-size: 14px;
}

/* 卡片颜色 */
.purple {
  background: linear-gradient(135deg, #a18cd1, #fbc2eb);
  color: white;
}

.green {
  background: linear-gradient(135deg, #84fab0, #8fd3f4);
  color: white;
}

.orange {
  background: linear-gradient(135deg, #fad961, #f76b1c);
  color: white;
}

.blue {
  background: linear-gradient(135deg, #2193b0, #6dd5ed);
  color: white;
}

/* 内容网格 */
.content-grid {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 20px;
  margin-top: 20px;
}

.content-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
}

.card-header {
  padding: 20px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h2 {
  font-size: 18px;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.view-all {
  color: #666;
  text-decoration: none;
  font-size: 14px;
}

/* 公告列表 */
.notice-list {
  padding: 20px;
}

.notice-item {
  padding: 15px;
  border-radius: 8px;
  background: #f8f9fa;
  margin-bottom: 15px;
}

.notice-time {
  font-size: 12px;
  color: #666;
  margin-bottom: 5px;
}

.notice-content h3 {
  font-size: 16px;
  margin: 0 0 5px;
}

.notice-content p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

/* 快捷操作区域 */
.quick-actions-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 15px;
  padding: 0;
}

.action-card {
  border-radius: 10px;
  overflow: hidden;
}

.action-card a {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
  color: white;
  text-decoration: none;
  height: 100%;
  transition: all 0.3s ease;
}

.action-card:hover a {
  transform: translateY(-3px);
}

.action-card i {
  font-size: 24px;
  margin-bottom: 8px;
}

.action-card span {
  font-size: 14px;
}

.action-card.red a { background: linear-gradient(135deg, #ff6b6b, #ee5253); }
.action-card.blue a { background: linear-gradient(135deg, #4facfe, #00f2fe); }
.action-card.green a { background: linear-gradient(135deg, #48c6ef, #6f86d6); }
.action-card.purple a { background: linear-gradient(135deg, #a18cd1, #fbc2eb); }

/* 最新活动样式 */
.recent-activities {
  background: white;
  border-radius: 10px;
  overflow: hidden;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #eef2f7;
}

.section-header h2 {
  font-size: 16px;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 8px;
}

.view-all {
  color: #6c757d;
  text-decoration: none;
  font-size: 14px;
}

.activities-list {
  padding: 0;
}

.activity-item {
  display: flex;
  padding: 15px 20px;
  border-bottom: 1px solid #eef2f7;
  transition: background-color 0.3s;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-item:hover {
  background-color: #f8f9fa;
}

.activity-time {
  min-width: 100px;
  padding-right: 20px;
}

.activity-time .time {
  font-size: 13px;
  color: #6c757d;
  background: #f8f9fa;
  padding: 2px 8px;
  border-radius: 4px;
}

.activity-content {
  flex: 1;
}

.activity-title {
  font-weight: 500;
  margin-bottom: 4px;
  color: #2c3e50;
}

.activity-desc {
  font-size: 13px;
  color: #6c757d;
}

/* 温馨提示 */
.tips-list {
  padding: 20px;
}

.tip-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 0;
  color: #666;
}

.tip-item i {
  font-size: 16px;
}

/* 服务时间 */
.service-hours {
  padding: 20px;
}

.service-item {
  margin-bottom: 15px;
}

.service-day {
  font-weight: 600;
  margin-bottom: 5px;
}

.service-time {
  color: #666;
}

.contact-info {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.contact-info p {
  margin: 10px 0;
  color: #666;
  display: flex;
  align-items: center;
  gap: 10px;
}

/* 响应式调整 */
@media (max-width: 1024px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .content-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .time-display {
    position: static;
    margin-top: 15px;
    transform: none;
  }
  
  .quick-actions-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .activity-item {
    flex-direction: column;
  }
  
  .activity-time {
    margin-bottom: 8px;
  }
}

.empty-notice {
  text-align: center;
  padding: 20px;
  color: #909399;
}
</style> 