<template>
  <div class="admin-container">
    <a-layout>
      <!-- 侧边栏 -->
      <a-layout-sider
        v-if="!isMobile"
        collapsible
        :collapsed="collapsed"
        @collapse="collapsed = !collapsed"
      >
        <div class="logo" />
        <a-menu
          :selected-keys="[currentPath]"
          @menu-item-click="handleMenuClick"
        >
          <a-menu-item key="dashboard">
            <template #icon><icon-dashboard /></template>
            控制台
          </a-menu-item>
          <a-menu-item key="cards">
            <template #icon><icon-credit-card /></template>
            卡片管理
          </a-menu-item>
        </a-menu>
      </a-layout-sider>

      <a-layout>
        <!-- 顶部栏 -->
        <a-layout-header>
          <!-- 移动端显示菜单按钮 -->
          <a-button
            v-if="isMobile"
            class="menu-btn"
            @click="drawerVisible = true"
          >
            <icon-menu />
          </a-button>
          
          <div class="header-right">
            <a-button @click="handleLogout">退出登录</a-button>
          </div>
        </a-layout-header>

        <!-- 内容区 -->
        <a-layout-content>
          <router-view></router-view>
        </a-layout-content>
      </a-layout>
    </a-layout>

    <!-- 移动端抽屉菜单 -->
    <a-drawer
      v-if="isMobile"
      :visible="drawerVisible"
      @cancel="drawerVisible = false"
      placement="left"
      :width="200"
    >
      <a-menu
        :selected-keys="[currentPath]"
        @menu-item-click="handleMenuClick"
      >
        <a-menu-item key="dashboard">
          <template #icon><icon-dashboard /></template>
          控制台
        </a-menu-item>
        <a-menu-item key="cards">
          <template #icon><icon-credit-card /></template>
          卡片管理
        </a-menu-item>
      </a-menu>
    </a-drawer>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../stores/user'
import { useDevice } from '../../composables/useDevice'

const router = useRouter()
const userStore = useUserStore()
const { isMobile } = useDevice()

const collapsed = ref(false)
const drawerVisible = ref(false)
const currentPath = ref('dashboard')

const handleMenuClick = (key) => {
  currentPath.value = key
  if (isMobile.value) {
    drawerVisible.value = false
  }
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.admin-container {
  height: 100vh;
}

.logo {
  height: 32px;
  margin: 16px;
  background: rgba(255, 255, 255, 0.2);
}

.header-right {
  float: right;
}

.menu-btn {
  margin-right: 16px;
}

:deep(.arco-layout-header) {
  background: #fff;
  padding: 0 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
}

:deep(.arco-layout-content) {
  padding: 24px;
  background: #f5f5f5;
}

@media screen and (max-width: 768px) {
  :deep(.arco-layout-content) {
    padding: 16px;
  }
}
</style> 