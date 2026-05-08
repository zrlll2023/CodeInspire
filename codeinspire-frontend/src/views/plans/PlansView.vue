<template>
  <div class="plans-container">
    <div class="page-header">
      <h2>学习规划</h2>
      <el-button type="primary" @click="showPlanDialog = true">
        <el-icon><Plus /></el-icon>
        新建规划
      </el-button>
    </div>

    <el-row :gutter="24" class="stats-row">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-value">{{ stats.total }}</div>
          <div class="stat-label">总规划数</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card active-count">
          <div class="stat-value">{{ stats.active }}</div>
          <div class="stat-label">进行中</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card completed-count">
          <div class="stat-value">{{ stats.completed }}</div>
          <div class="stat-label">已完成</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card task-count">
          <div class="stat-value">{{ stats.totalTasks }}</div>
          <div class="stat-label">总任务</div>
        </el-card>
      </el-col>
    </el-row>

    <div v-loading="loading" class="plans-list">
      <div v-if="plans.length === 0 && !loading" class="empty-state">
        <el-empty description="暂无规划，开始创建你的第一个学习计划吧！">
          <el-button type="primary" @click="showPlanDialog = true">创建规划</el-button>
        </el-empty>
      </div>

      <TransitionGroup name="list" tag="div" class="cards-grid">
        <el-card
          v-for="plan in plans"
          :key="plan.id"
          shadow="hover"
          class="plan-card"
        >
          <template #header>
            <div class="plan-header">
              <h3>{{ plan.title }}</h3>
              <el-tag :type="getStatusType(plan.status)" size="small">
                {{ getStatusLabel(plan.status) }}
              </el-tag>
            </div>
          </template>

          <p class="plan-desc">{{ plan.description || '暂无描述' }}</p>

          <div class="plan-meta">
            <div class="meta-item">
              <el-icon><Calendar /></el-icon>
              {{ formatDate(plan.startDate) }} - {{ formatDate(plan.endDate) }}
            </div>
            <div class="meta-item">
              <el-icon><Target /></el-icon>
              {{ plan.targetGoal || '未设定目标' }}
            </div>
          </div>

          <el-progress
            :percentage="getProgress(plan)"
            :stroke-width="8"
            :color="progressColors"
            style="margin: 16px 0;"
          />

          <div class="plan-footer">
            <span class="task-info">
              {{ plan.completedTasks || 0 }} / {{ plan.totalTasks || 0 }} 任务
            </span>
            <div class="actions">
              <el-button text type="primary" size="small" @click="viewTasks(plan)">
                查看任务
              </el-button>
              <el-button text size="small" @click="completePlan(plan)" v-if="plan.status === 'active'">
                完成
              </el-button>
            </div>
          </div>
        </el-card>
      </TransitionGroup>
    </div>

    <el-dialog
      v-model="showPlanDialog"
      title="新建学习规划"
      width="560px"
      destroy-on-close
    >
      <el-form :model="planForm" label-width="100px">
        <el-form-item label="规划标题" required>
          <el-input v-model="planForm.title" placeholder="如：Java后端学习计划" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="planForm.description" type="textarea" :rows="3" placeholder="规划详细说明" />
        </el-formItem>
        <el-form-item label="类型">
          <el-select v-model="planForm.type" placeholder="请选择">
            <el-option label="学习计划" value="learning" />
            <el-option label="求职准备" value="career" />
            <el-option label="面试准备" value="interview" />
            <el-option label="项目开发" value="project" />
          </el-select>
        </el-formItem>
        <el-form-item label="开始日期" required>
          <el-date-picker v-model="planForm.startDate" type="date" placeholder="选择日期" />
        </el-formItem>
        <el-form-item label="结束日期" required>
          <el-date-picker v-model="planForm.endDate" type="date" placeholder="选择日期" />
        </el-formItem>
        <el-form-item label="目标">
          <el-input v-model="planForm.targetGoal" placeholder="如：掌握Spring Boot并完成一个完整项目" />
        </el-formItem>
      </el-form>
      <template #footer>
        <el-button @click="showPlanDialog = false">取消</el-button>
        <el-button type="primary" @click="createPlan" :loading="creating">创建</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Calendar, Target } from '@element-plus/icons-vue'
import { planApi, taskApi } from '@/api/plan'

const loading = ref(false)
const creating = ref(false)
const showPlanDialog = ref(false)
const plans = ref<any[]>([])

const stats = reactive({
  total: 0,
  active: 0,
  completed: 0,
  totalTasks: 0
})

const planForm = reactive({
  title: '',
  description: '',
  type: 'learning',
  startDate: '',
  endDate: '',
  targetGoal: ''
})

onMounted(async () => {
  await fetchPlans()
})

async function fetchPlans() {
  loading.value = true
  try {
    const data = await planApi.getPlans()
    plans.value = data

    stats.total = data.length
    stats.active = data.filter((p: any) => p.status === 'active').length
    stats.completed = data.filter((p: any) => p.status === 'completed').length
    stats.totalTasks = data.reduce((sum: number, p: any) => sum + (p.totalTasks || 0), 0)
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

async function createPlan() {
  if (!planForm.title) {
    ElMessage.warning('请输入规划标题')
    return
  }

  creating.value = true
  try {
    await planApi.createPlan({
      ...planForm,
      startDate: new Date(planForm.startDate).toISOString().split('T')[0],
      endDate: new Date(planForm.endDate).toISOString().split('T')[0]
    })
    ElMessage.success('规划创建成功')
    showPlanDialog.value = false
    Object.assign(planForm, {
      title: '', description: '', type: 'learning',
      startDate: '', endDate: '', targetGoal: ''
    })
    await fetchPlans()
  } catch (error) {
    console.error(error)
  } finally {
    creating.value = false
  }
}

function getProgress(plan: any): number {
  if (!plan.totalTasks) return 0
  return Math.round((plan.completedTasks / plan.totalTasks) * 100)
}

function getStatusType(status: string): string {
  const map: Record<string, string> = {
    active: 'success',
    completed: 'info',
    paused: 'warning',
    archived: 'info'
  }
  return map[status] || 'info'
}

function getStatusLabel(status: string): string {
  const map: Record<string, string> = {
    active: '进行中',
    completed: '已完成',
    paused: '已暂停',
    archived: '已归档'
  }
  return map[status] || status
}

function formatDate(date: string): string {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('zh-CN', { month: 'short', day: 'numeric' })
}

async function completePlan(plan: any) {
  try {
    await ElMessageBox.confirm('确定完成此规划吗？', '确认', { type: 'warning' })
    await planApi.completePlan(plan.id)
    ElMessage.success('规划已完成')
    await fetchPlans()
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

function viewTasks(plan: any) {
  ElMessage.info(`查看规划「${plan.title}」的任务列表（功能开发中）`)
}

const progressColors = [
  { color: '#f56c6c', percentage: 20 },
  { color: '#e6a23c', percentage: 40 },
  { color: '#409eff', percentage: 60 },
  { color: '#67c23a', percentage: 80 },
  { color: '#10b981', percentage: 100 }
]
</script>

<style lang="scss" scoped>
.plans-container {
  max-width: 1200px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;

  h2 {
    font-size: 22px;
    font-weight: 600;
  }
}

.stats-row {
  margin-bottom: 28px;

  .stat-card {
    border-radius: 14px;
    text-align: center;
    padding: 20px;

    .stat-value {
      font-size: 32px;
      font-weight: 700;
      background: linear-gradient(135deg, var(--primary-light), #a78bfa);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }

    .stat-label {
      font-size: 13px;
      color: var(--text-secondary);
      margin-top: 4px;
    }

    &.active-count .stat-value {
      background: linear-gradient(135deg, #10b981, #34d399);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }

    &.completed-count .stat-value {
      background: linear-gradient(135deg, #f59e0b, #fbbf24);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }

    &.task-count .stat-value {
      background: linear-gradient(135deg, #8b5cf6, #a78bfa);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }
  }
}

.cards-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: 20px;
}

.plan-card {
  border-radius: 14px;
  transition: transform 0.2s ease;

  &:hover {
    transform: translateY(-4px);
  }

  .plan-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    h3 {
      font-size: 16px;
      font-weight: 600;
      margin: 0;
    }
  }

  .plan-desc {
    color: var(--text-secondary);
    font-size: 14px;
    line-height: 1.5;
    margin-bottom: 12px;
  }

  .plan-meta {
    display: flex;
    flex-direction: column;
    gap: 8px;

    .meta-item {
      display: flex;
      align-items: center;
      gap: 6px;
      font-size: 12px;
      color: var(--text-muted);

      .el-icon {
        font-size: 14px;
      }
    }
  }

  .plan-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-top: 12px;
    border-top: 1px solid var(--border-color);

    .task-info {
      font-size: 13px;
      color: var(--text-secondary);
    }

    .actions {
      display: flex;
      gap: 4px;
    }
  }
}

.empty-state {
  min-height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.list-enter-active,
.list-leave-active {
  transition: all 0.3s ease;
}

.list-enter-from,
.list-leave-to {
  opacity: 0;
  transform: translateY(20px);
}
</style>
