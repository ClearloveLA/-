<template>
  <div class="student-layout">
    <a-layout>
      <!-- 移动端顶部导航 -->
      <a-layout-header v-if="isMobile" class="mobile-header">
        <h3>{{ currentRoute.meta.title }}</h3>
      </a-layout-header>

      <a-layout class="main-layout">
        <!-- PC端侧边栏 -->
        <a-layout-sider
          v-if="!isMobile"
          class="pc-sider"
          :width="220"
          breakpoint="xl"
          :collapsed="collapsed"
          @collapse="setCollapsed"
        >
          <div class="logo">
            <icon-apps theme="filled" />
            <span v-show="!collapsed">校园卡管理系统</span>
          </div>
          <a-menu
            :selected-keys="[currentRoute.path]"
            @menu-item-click="handleMenuClick"
            :style="{ flex: 1 }"
          >
            <a-menu-item v-for="nav in navigation" :key="nav.path">
              <template #icon>
                <component :is="nav.icon" />
              </template>
              {{ nav.title }}
            </a-menu-item>
          </a-menu>
          <div class="sider-footer" v-show="!collapsed">
            <a-avatar :size="32">
              <icon-user />
            </a-avatar>
            <span class="username">张三</span>
          </div>
        </a-layout-sider>

        <!-- 内容区 -->
        <a-layout-content class="main-content">
          <div class="content-wrapper">
            <router-view v-slot="{ Component }">
              <transition name="fade" mode="out-in">
                <component :is="Component" />
              </transition>
            </router-view>
          </div>
        </a-layout-content>
      </a-layout>

      <!-- 移动端底部导航 -->
      <a-layout-footer v-if="isMobile" class="mobile-footer">
        <a-space fill>
          <a-button
            v-for="nav in navigation"
            :key="nav.path"
            :type="isActive(nav.path) ? 'primary' : 'text'"
            @click="router.push(nav.path)"
          >
            <template #icon>
              <component :is="nav.icon" />
            </template>
            {{ nav.title }}
          </a-button>
        </a-space>
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useDevice } from '@/composables/useDevice'
import {
  IconHome,
  IconNotification,
  IconHeart,
  IconSearch,
  IconApps,
  IconUser
} from '@arco-design/web-vue/es/icon'

const router = useRouter()
const route = useRoute()
const { isMobile } = useDevice()
const collapsed = ref(false)

const navigation = [
  { path: '/student/home', title: '首页', icon: IconHome },
  { path: '/student/report-loss', title: '挂失申请', icon: IconNotification },
  { path: '/student/found-card', title: '招领登记', icon: IconHeart },
  { path: '/student/search', title: '查询中心', icon: IconSearch }
]

const currentRoute = computed(() => route)

const isActive = (path) => route.path === path

const handleMenuClick = (key) => {
  router.push(key)
}

const setCollapsed = (value) => {
  collapsed.value = value
}
</script>

<style scoped>
.student-layout {
  min-height: 100vh;
  background: #f5f5f5;
}

.main-layout {
  min-height: 100vh;
}

.mobile-header {
  position: fixed;
  top: 0;
  width: 100%;
  z-index: 100;
  background: #fff;
  padding: 0 16px;
  height: 52px;
  line-height: 52px;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
}

.mobile-header h3 {
  margin: 0;
  font-size: 16px;
  text-align: center;
  color: #1d2129;
}

.pc-sider {
  height: 100vh;
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 8px 0 rgba(29,33,41,.05);
}

.logo {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: rgb(var(--primary-6));
  font-size: 18px;
  font-weight: 500;
  border-bottom: 1px solid var(--color-border);
}

.logo .icon {
  font-size: 24px;
}

.sider-footer {
  border-top: 1px solid var(--color-border);
  padding: 12px 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.username {
  color: var(--color-text-2);
  font-size: 14px;
}

.main-content {
  padding: 16px;
  margin-left: v-bind(isMobile ? '0' : collapsed ? '48px' : '220px');
  margin-top: v-bind(isMobile ? '52px' : '0');
  margin-bottom: v-bind(isMobile ? '60px' : '0');
  transition: margin-left 0.2s;
}

.content-wrapper {
  background: #fff;
  border-radius: 4px;
  padding: 24px;
  min-height: calc(100vh - 32px);
}

.mobile-footer {
  position: fixed;
  bottom: 0;
  width: 100%;
  padding: 8px;
  background: #fff;
  box-shadow: 0 -1px 4px rgba(0,21,41,.08);
}

:deep(.arco-layout-footer) {
  padding: 0;
}

:deep(.arco-space) {
  width: 100%;
  justify-content: space-around;
}

:deep(.arco-btn) {
  padding: 8px 0;
  height: auto;
  flex-direction: column;
}

:deep(.arco-btn-icon) {
  margin-right: 0;
  margin-bottom: 4px;
  font-size: 20px;
}

/* 页面切换动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style> 