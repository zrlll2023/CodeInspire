<template>
  <div class="settings-container">
    <div class="page-header">
      <h2>设置</h2>
    </div>

    <el-row :gutter="24">
      <el-col :span="16">
        <el-card shadow="hover" class="section-card">
          <template #header>
            <div class="card-title">
              <el-icon><Bell /></el-icon> 通知设置
            </div>
          </template>

          <div class="setting-items">
            <div class="setting-item">
              <div class="setting-info">
                <h4>任务提醒</h4>
                <p>任务到期前发送提醒通知</p>
              </div>
              <el-switch v-model="notificationSettings.taskReminderEnabled" />
            </div>

            <div class="setting-item">
              <div class="setting-info">
                <h4>时间节点提醒</h4>
                <p>关键时间节点（如秋招季）自动推送</p>
              </div>
              <el-switch v-model="notificationSettings.timeNodeEnabled" />
            </div>

            <div class="setting-item">
              <div class="setting-info">
                <h4>进度预警</h4>
                <p>规划进度落后时发送预警</p>
              </div>
              <el-switch v-model="notificationSettings.progressWarningEnabled" />
            </div>

            <div class="setting-item">
              <div class="setting-info">
                <h4>AI回复通知</h4>
                <p>AI顾问回复时推送通知</p>
              </div>
              <el-switch v-model="notificationSettings.aiReplyEnabled" />
            </div>

            <el-divider />

            <div class="setting-item">
              <div class="setting-info">
                <h4>免打扰时段</h4>
                <p>{{ notificationSettings.quietStartHour }} - {{ notificationSettings.quietEndHour }} 不接收通知</p>
              </div>
              <el-time-picker
                v-model="quietHours"
                is-range
                range-separator="至"
                start-placeholder="开始"
                end-placeholder="结束"
                size="default"
                @change="handleQuietHoursChange"
              />
            </div>
          </div>
        </el-card>

        <el-card shadow="hover" class="section-card">
          <template #header>
            <div class="card-title">
              <el-icon><User /></el-icon> 账户信息
            </div>
          </template>

          <div class="user-info-section">
            <div class="avatar-section">
              <el-avatar :size="80" :src="null" style="background: var(--primary-color);">
                {{ userStore.username?.charAt(0)?.toUpperCase() }}
              </el-avatar>
              <el-button text type="primary">更换头像</el-button>
            </div>

            <el-descriptions :column="2" border>
              <el-descriptions-item label="用户名">{{ userStore.username }}</el-descriptions-item>
              <el-descriptions-item label="邮箱">{{ userStore.user?.email || '未绑定' }}</el-descriptions-item>
              <el-descriptions-item label="注册时间">{{ formatDate(userStore.user?.createdAt) }}</el-descriptions-item>
              <el-descriptions-item label="账号状态">
                <el-tag type="success" size="small">正常</el-tag>
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card shadow="hover" class="quick-actions-card">
          <template #header>
            <div class="card-title">快捷操作</div>
          </template>

          <div class="action-buttons">
            <el-button @click="$router.push('/profile')">
              <el-icon><EditPen /></el-icon>
              编辑画像
            </el-button>
            <el-button @click="handleExportData">
              <el-icon><Download /></el-icon>
              导出数据
            </el-button>
            <el-button type="danger" plain @click="handleLogout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-button>
          </div>
        </el-card>

        <el-card shadow="hover" class="about-card">
          <template #header>
            <div class="card-title">关于 CodeInspire</div>
          </template>

          <div class="about-content">
            <div class="version">版本 1.0.0</div>
            <p class="description">
              AI驱动的计算机专业学生个性化顾问系统，帮助你更好地规划学习和职业发展。
            </p>
            <div class="links">
              <a href="#">使用文档</a>
              <a href="#">反馈建议</a>
              <a href="#">隐私政策</a>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Bell, User, EditPen, Download, SwitchButton
} from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const quietHours = ref<[Date, Date] | null>(null)

const notificationSettings = reactive({
  taskReminderEnabled: true,
  timeNodeEnabled: true,
  progressWarningEnabled: true,
  aiReplyEnabled: true,
  quietStartHour: '22:00',
  quietEndHour: '08:00'
})

onMounted(() => {
  quietHours.value = [
    new Date(`2024-01-01T${notificationSettings.quietStartHour}`),
    new Date(`2024-01-01T${notificationSettings.quietEndHour}`)
  ]
})

function handleQuietHoursChange(val: [Date, Date] | null) {
  if (val && val.length === 2) {
    const format = (d: Date) => d.toTimeString().slice(0, 5)
    notificationSettings.quietStartHour = format(val[0])
    notificationSettings.quietEndHour = format(val[1])
  }
}

function formatDate(date?: string): string {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('zh-CN')
}

function handleExportData() {
  ElMessage.success('数据导出功能开发中...')
}

async function handleLogout() {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', { type: 'warning' })
    userStore.logout()
    router.push('/login')
    ElMessage.success('已退出登录')
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}
</script>

<style lang="scss" scoped>
.settings-container {
  max-width: 1200px;
}

.page-header {
  margin-bottom: 24px;

  h2 {
    font-size: 22px;
    font-weight: 600;
  }
}

.section-card {
  border-radius: 14px;
  margin-bottom: 20px;

  .card-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-weight: 600;
    font-size: 15px;
  }

  :deep(.el-card__header) {
    padding: 16px 20px;
    border-bottom-color: var(--border-color);
  }
}

.setting-items {
  display: flex;
  flex-direction: column;
}

.setting-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
  border-bottom: 1px solid var(--border-color);

  &:last-child {
    border-bottom: none;
  }

  .setting-info {
    h4 {
      font-size: 14px;
      font-weight: 500;
      color: var(--text-primary);
      margin-bottom: 4px;
    }

    p {
      font-size: 12px;
      color: var(--text-muted);
      margin: 0;
    }
  }
}

.user-info-section {
  .avatar-section {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 12px;
    margin-bottom: 24px;
  }
}

.quick-actions-card, .about-card {
  border-radius: 14px;
  margin-bottom: 20px;

  .card-title {
    font-weight: 600;
    font-size: 15px;
  }

  :deep(.el-card__header) {
    padding: 16px 20px;
    border-bottom-color: var(--border-color);
  }
}

.action-buttons {
  display: flex;
  flex-direction: column;
  gap: 10px;

  .el-button {
    justify-content: flex-start;
    height: 44px;
    border-radius: 10px;

    .el-icon {
      margin-right: 8px;
    }
  }
}

.about-content {
  .version {
    font-size: 18px;
    font-weight: 600;
    color: var(--primary-light);
    margin-bottom: 12px;
  }

  .description {
    font-size: 13px;
    line-height: 1.6;
    color: var(--text-secondary);
    margin-bottom: 16px;
  }

  .links {
    display: flex;
    gap: 16px;

    a {
      font-size: 13px;
      color: var(--primary-light);

      &:hover {
        text-decoration: underline;
      }
    }
  }
}
</style>
