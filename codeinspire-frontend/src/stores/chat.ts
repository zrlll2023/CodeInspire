import { defineStore } from 'pinia'
import { ref } from 'vue'

export interface Message {
  id: string
  role: 'user' | 'assistant'
  content: string
  timestamp: Date
  loading?: boolean
}

export const useChatStore = defineStore('chat', () => {
  const messages = ref<Message[]>([])
  const isLoading = ref(false)
  const currentSessionId = ref<string | null>(null)
  const currentScene = ref('general')

  function addMessage(message: Message) {
    messages.value.push(message)
  }

  function clearMessages() {
    messages.value = []
    currentSessionId.value = null
  }

  function setLoading(loading: boolean) {
    isLoading.value = loading
  }

  function setSessionId(sessionId: string) {
    currentSessionId.value = sessionId
  }

  function setScene(scene: string) {
    currentScene.value = scene
  }

  return {
    messages,
    isLoading,
    currentSessionId,
    currentScene,
    addMessage,
    clearMessages,
    setLoading,
    setSessionId,
    setScene
  }
})
