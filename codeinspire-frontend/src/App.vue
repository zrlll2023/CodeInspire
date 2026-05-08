<template>
  <router-view />
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user'
import { onMounted } from 'vue'

const userStore = useUserStore()

onMounted(() => {
  userStore.initFromStorage()
})
</script>

<style lang="scss">
#app {
  width: 100%;
  min-height: 100vh;
}
</style>
