import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref<string | null>(null)
  const user = ref<any>(null)
  const profile = ref<any>(null)

  const isLoggedIn = computed(() => !!token.value)
  const username = computed(() => user.value?.username || '')
  const userId = computed(() => user.value?.id || null)

  function setToken(newToken: string) {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  function setUser(newUser: any) {
    user.value = newUser
    if (newUser) {
      localStorage.setItem('user', JSON.stringify(newUser))
    }
  }

  function setProfile(newProfile: any) {
    profile.value = newProfile
  }

  function logout() {
    token.value = null
    user.value = null
    profile.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  function initFromStorage() {
    const storedToken = localStorage.getItem('token')
    const storedUser = localStorage.getItem('user')

    if (storedToken) {
      token.value = storedToken
    }
    if (storedUser) {
      try {
        user.value = JSON.parse(storedUser)
      } catch (e) {
        localStorage.removeItem('user')
      }
    }
  }

  return {
    token,
    user,
    profile,
    isLoggedIn,
    username,
    userId,
    setToken,
    setUser,
    setProfile,
    logout,
    initFromStorage
  }
})
