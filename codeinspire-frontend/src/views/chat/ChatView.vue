<template>
  <div class="chat-container">
    <div class="chat-header">
      <div class="header-left">
        <h2>AI 学习顾问</h2>
        <el-tag size="small" :type="sceneType">{{ currentSceneLabel }}</el-tag>
      </div>

      <div class="header-right">
        <el-select
          v-model="selectedScene"
          placeholder="选择场景"
          size="small"
          style="width: 140px"
          @change="handleSceneChange"
        >
          <el-option label="通用咨询" value="general" />
          <el-option label="求职建议" value="career_advice" />
          <el-option label="技术学习" value="tech_learning" />
          <el-option label="面试准备" value="interview_prep" />
        </el-select>

        <el-button text @click="clearChat">
          <el-icon><RefreshRight /></el-icon>
          新对话
        </el-button>
      </div>
    </div>

    <div class="chat-messages" ref="messagesContainer">
      <div v-if="chatStore.messages.length === 0" class="empty-state">
        <div class="empty-icon">🤖</div>
        <h3>你好，我是 CodeInspire</h3>
        <p>你的AI个性化学习顾问</p>

        <div class="quick-actions">
          <div
            v-for="(action, index) in quickActions"
            :key="index"
            class="action-card"
            @click="sendQuickMessage(action.message)"
          >
            <span class="action-icon">{{ action.icon }}</span>
            <span class="action-text">{{ action.label }}</span>
          </div>
        </div>
      </div>

      <TransitionGroup name="message-list" tag="div" class="messages-wrapper">
        <div
          v-for="msg in chatStore.messages"
          :key="msg.id"
          class="message-item"
          :class="{ 'is-user': msg.role === 'user', 'is-assistant': msg.role === 'assistant' }"
        >
          <div class="message-avatar">
            {{ msg.role === 'user' ? '👤' : '⚡' }}
          </div>
          <div class="message-content">
            <div class="message-text" v-html="renderMarkdown(msg.content)"></div>
            <div class="message-meta">
              <span class="time">{{ formatTime(msg.timestamp) }}</span>
              <div
                v-if="msg.role === 'assistant'"
                class="message-actions"
              >
                <el-button text size="small" @click="copyMessage(msg.content)">
                  复制
                </el-button>
                <el-button text size="small" type="success" icon="CircleCheckFilled">
                  有帮助
                </el-button>
                <el-button text size="small" type="danger" icon="CircleCloseFilled">
                  无帮助
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </TransitionGroup>

      <div v-if="chatStore.isLoading" class="message-item is-assistant">
        <div class="message-avatar">⚡</div>
        <div class="message-content loading-content">
          <div class="typing-indicator">
            <span></span><span></span><span></span>
          </div>
        </div>
      </div>
    </div>

    <div class="chat-input-area">
      <div class="input-container">
        <el-input
          ref="inputRef"
          v-model="inputMessage"
          type="textarea"
          :rows="1"
          :autosize="{ minRows: 1, maxRows: 4 }"
          placeholder="输入你的问题..."
          resize="none"
          @keydown.enter.exact.prevent="sendMessage"
        />
        <el-button
          type="primary"
          circle
          :icon="Promotion"
          :loading="chatStore.isLoading"
          :disabled="!inputMessage.trim()"
          @click="sendMessage"
          class="send-btn"
        />
      </div>
      <p class="input-hint">按 Enter 发送，Shift + Enter 换行</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, nextTick, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Promotion, RefreshRight } from '@element-plus/icons-vue'
import MarkdownIt from 'markdown-it'
import { useChatStore, type Message } from '@/stores/chat'
import { chatApi } from '@/api/chat'

const md = new MarkdownIt({
  html: false,
  linkify: true,
  breaks: true
})

const chatStore = useChatStore()
const inputRef = ref<HTMLInputElement>()
const messagesContainer = ref<HTMLElement>()
const inputMessage = ref('')
const selectedScene = ref('general')

const quickActions = [
  { icon: '💼', label: '求职建议', message: '我想了解计算机专业的求职准备建议' },
  { icon: '📚', label: '学习路线', message: '帮我规划一下后端开发的学习路线' },
  { icon: '🎯', label: '面试准备', message: 'Java后端面试需要准备哪些内容？' },
  { icon: '📊', label: '技能分析', message: '帮我分析一下我的技术栈是否足够' }
]

const currentSceneLabel = computed(() => {
  const labels: Record<string, string> = {
    general: '通用咨询',
    career_advice: '求职建议',
    tech_learning: '技术学习',
    interview_prep: '面试准备'
  }
  return labels[selectedScene.value] || '通用'
})

const sceneType = computed(() => {
  const types: Record<string, string> = {
    general: '',
    career_advice: 'warning',
    tech_learning: 'success',
    interview_prep: 'danger'
  }
  return types[selectedScene.value] || ''
})

function renderMarkdown(content: string): string {
  return md.render(content)
}

function formatTime(date: Date): string {
  const d = new Date(date)
  return d.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

function generateId(): string {
  return `msg_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`
}

async function sendMessage() {
  if (!inputMessage.value.trim() || chatStore.isLoading) return

  const userMsg: Message = {
    id: generateId(),
    role: 'user',
    content: inputMessage.value.trim(),
    timestamp: new Date()
  }

  chatStore.addMessage(userMsg)

  const messageToSend = inputMessage.value.trim()
  inputMessage.value = ''

  await nextTick()
  scrollToBottom()

  chatStore.setLoading(true)

  try {
    const response = await chatApi.sendMessage({
      message: messageToSend,
      scene: selectedScene.value,
      sessionId: chatStore.currentSessionId || undefined
    })

    if (response.sessionId && !chatStore.currentSessionId) {
      chatStore.setSessionId(response.sessionId)
    }

    const assistantMsg: Message = {
      id: generateId(),
      role: 'assistant',
      content: response.content,
      timestamp: new Date()
    }

    chatStore.addMessage(assistantMsg)
  } catch (error) {
    ElMessage.error('发送失败，请重试')
  } finally {
    chatStore.setLoading(false)
    await nextTick()
    scrollToBottom()
  }
}

async function sendQuickMessage(message: string) {
  inputMessage.value = message
  await sendMessage()
}

function copyMessage(content: string) {
  navigator.clipboard.writeText(content)
  ElMessage.success('已复制到剪贴板')
}

function clearChat() {
  chatStore.clearMessages()
  selectedScene.value = 'general'
}

function handleSceneChange(scene: string) {
  selectedScene.value = scene
  chatStore.setScene(scene)
}

async function scrollToBottom() {
  if (messagesContainer.value) {
    messagesContainer.value.scrollTo({
      top: messagesContainer.value.scrollHeight,
      behavior: 'smooth'
    })
  }
}
</script>

<style lang="scss" scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: calc(100vh - var(--header-height) - 48px);
  background: var(--bg-primary);
  border-radius: 16px;
  overflow: hidden;
  border: 1px solid var(--border-color);
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid var(--border-color);
  background: var(--bg-secondary);

  h2 {
    font-size: 18px;
    font-weight: 600;
    margin-right: 12px;
  }

  .header-left {
    display: flex;
    align-items: center;
  }

  .header-right {
    display: flex;
    align-items: center;
    gap: 8px;
  }
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
  scroll-behavior: smooth;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  text-align: center;

  .empty-icon {
    font-size: 64px;
    margin-bottom: 16px;
    animation: bounce 2s ease-in-out infinite;
  }

  h3 {
    font-size: 22px;
    font-weight: 600;
    color: var(--text-primary);
    margin-bottom: 8px;
  }

  p {
    color: var(--text-secondary);
    margin-bottom: 32px;
  }
}

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  max-width: 480px;
  width: 100%;
}

.action-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 16px;
  background: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s ease;

  &:hover {
    border-color: var(--primary-color);
    background: rgba(99, 102, 241, 0.1);
    transform: translateY(-2px);
  }

  .action-icon {
    font-size: 28px;
  }

  .action-text {
    font-size: 13px;
    color: var(--text-secondary);
  }
}

.messages-wrapper {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.message-item {
  display: flex;
  gap: 14px;
  max-width: 85%;

  &.is-user {
    align-self: flex-end;
    flex-direction: row-reverse;

    .message-content {
      background: linear-gradient(135deg, var(--primary-color), #7c3aed);
      border-radius: 18px 18px 4px 18px;
      color: #fff;
    }
  }

  &.is-assistant {
    align-self: flex-start;

    .message-content {
      background: var(--card-bg);
      border: 1px solid var(--border-color);
      border-radius: 18px 18px 18px 4px;
    }
  }
}

.message-avatar {
  width: 38px;
  height: 38px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  flex-shrink: 0;
  background: var(--bg-tertiary);
}

.message-content {
  padding: 14px 18px;
  position: relative;
}

.message-text {
  line-height: 1.6;
  word-break: break-word;

  :deep(p) {
    margin-bottom: 8px;

    &:last-child {
      margin-bottom: 0;
    }
  }

  :deep(code) {
    background: rgba(99, 102, 241, 0.15);
    padding: 2px 6px;
    border-radius: 4px;
    font-family: 'Fira Code', monospace;
    font-size: 13px;
  }

  :deep(pre) {
    background: var(--bg-primary);
    padding: 12px;
    border-radius: 8px;
    overflow-x: auto;
    margin: 10px 0;

    code {
      background: none;
      padding: 0;
    }
  }

  :deep(ul), :deep(ol) {
    padding-left: 20px;
    margin: 8px 0;
  }

  :deep(li) {
    margin: 4px 0;
  }

  :deep(strong) {
    color: var(--primary-light);
  }
}

.loading-content {
  padding: 18px 22px;
}

.typing-indicator {
  display: flex;
  gap: 5px;

  span {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background: var(--primary-light);
    animation: typing 1.4s infinite ease-in-out both;

    &:nth-child(1) { animation-delay: -0.32s; }
    &:nth-child(2) { animation-delay: -0.16s; }
  }
}

@keyframes typing {
  0%, 80%, 100% {
    transform: scale(0.6);
    opacity: 0.5;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

.message-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 10px;
  padding-top: 8px;
  border-top: 1px solid rgba(255, 255, 255, 0.05);

  .time {
    font-size: 11px;
    color: var(--text-muted);
  }

  .message-actions {
    display: flex;
    gap: 4px;
  }
}

.chat-input-area {
  padding: 16px 24px;
  border-top: 1px solid var(--border-color);
  background: var(--bg-secondary);
}

.input-container {
  display: flex;
  align-items: flex-end;
  gap: 12px;
  background: var(--bg-tertiary);
  border-radius: 14px;
  padding: 8px 12px;
  border: 1px solid var(--border-color);

  &:focus-within {
    border-color: var(--primary-color);
    box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);
  }

  :deep(.el-textarea__inner) {
    background: transparent !important;
    box-shadow: none !important;
    padding: 8px 0;
    font-size: 14px;
  }
}

.send-btn {
  width: 40px;
  height: 40px;
  flex-shrink: 0;

  :deep(.el-icon) {
    font-size: 18px;
  }
}

.input-hint {
  text-align: right;
  font-size: 11px;
  color: var(--text-muted);
  margin-top: 8px;
}

.message-list-enter-active {
  transition: all 0.3s ease-out;
}

.message-list-enter-from {
  opacity: 0;
  transform: translateY(20px);
}
</style>
