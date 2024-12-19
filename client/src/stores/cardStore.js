import { defineStore } from 'pinia'
import api from '@/api'

export const useCardStore = defineStore('card', {
  state: () => ({
    lostCards: [],
    foundCards: [],
    loading: false,
    error: null
  }),

  actions: {
    // 提交挂失
    async addLostCard(card) {
      this.loading = true
      try {
        const response = await api.post('/lost-cards', card)
        this.lostCards.push(response.data)
        return response.data
      } catch (error) {
        this.error = error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    // 提交招领
    async addFoundCard(card) {
      this.loading = true
      try {
        const response = await api.post('/found-cards', card)
        this.foundCards.push(response.data)
        return response.data
      } catch (error) {
        this.error = error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    // 获取挂失列表
    async fetchLostCards() {
      this.loading = true
      try {
        const response = await api.get('/lost-cards')
        this.lostCards = response.data
        return response.data
      } catch (error) {
        this.error = error.message
        throw error
      } finally {
        this.loading = false
      }
    },

    // 获取招领列表
    async fetchFoundCards() {
      this.loading = true
      try {
        const response = await api.get('/found-cards')
        this.foundCards = response.data
        return response.data
      } catch (error) {
        this.error = error.message
        throw error
      } finally {
        this.loading = false
      }
    }
  }
}) 