<template>
  <div class="profile-container">
    <div class="page-header">
      <h2>用户画像</h2>
      <el-button type="primary" @click="showEditDialog = true">
        <el-icon><Edit /></el-icon>
        编辑画像
      </el-button>
    </div>

    <div v-if="loading" class="loading-state">
      <el-skeleton :rows="8" animated />
    </div>

    <div v-else-if="!profile" class="empty-profile">
      <el-empty description="尚未初始化画像">
        <el-button type="primary" @click="initProfile">开始初始化</el-button>
      </el-empty>
    </div>

    <div v-else class="profile-content">
      <el-row :gutter="24">
        <el-col :span="16">
          <div class="profile-cards">
            <el-card shadow="hover" class="info-card">
              <template #header>
                <div class="card-title">
                  <span class="icon">🎓</span> 教育背景
                </div>
              </template>
              <div class="info-grid">
                <div class="info-item">
                  <label>学校层次</label>
                  <span>{{ profile.education?.schoolLevel || '未填写' }}</span>
                </div>
                <div class="info-item">
                  <label>学历</label>
                  <span>{{ profile.education?.educationLevel || '未填写' }}</span>
                </div>
                <div class="info-item">
                  <label>专业</label>
                  <span>{{ profile.education?.major || '未填写' }}</span>
                </div>
                <div class="info-item">
                  <label>年级</label>
                  <span>{{ profile.education?.grade || '未填写' }}</span>
                </div>
              </div>
            </el-card>

            <el-card shadow="hover" class="info-card">
              <template #header>
                <div class="card-title">
                  <span class="icon">💼</span> 职业目标
                </div>
              </template>
              <div class="info-grid">
                <div class="info-item">
                  <label>目标岗位</label>
                  <span>{{ profile.career?.targetPosition || '未填写' }}</span>
                </div>
                <div class="info-item">
                  <label>目标城市</label>
                  <span>{{ profile.location?.targetCityLevel || '未填写' }}</span>
                </div>
                <div class="info-item">
                  <label>紧迫程度</label>
                  <el-tag :type="urgencyType">{{ profile.time?.urgencyLevel || '一般' }}</el-tag>
                </div>
                <div class="info-item">
                  <label>每周可用时间</label>
                  <span>{{ profile.time?.weeklyAvailableHours ? profile.time.weeklyAvailableHours + '小时' : '未填写' }}</span>
                </div>
              </div>
            </el-card>

            <el-card shadow="hover" class="info-card">
              <template #header>
                <div class="card-title">
                  <span class="icon">🛠️</span> 技术能力
                </div>
              </template>
              <div v-if="profile.tech?.skills?.length" class="skills-list">
                <el-tag
                  v-for="(skill, index) in profile.tech.skills"
                  :key="index"
                  :type="getSkillType(skill.level)"
                  effect="dark"
                  round
                  size="large"
                  style="margin: 4px"
                >
                  {{ skill.name }} ({{ skill.level }})
                </el-tag>
              </div>
              <el-empty v-else description="暂无技能记录" :image-size="60" />
            </el-card>
          </div>
        </el-col>

        <el-col :span="8">
          <div class="sidebar-cards">
            <el-card shadow="hover" class="completeness-card">
              <template #header>
                <div class="card-title">完整度</div>
              </template>
              <div class="completeness-content">
                <el-progress
                  type="circle"
                  :percentage="Math.round(profile.completenessScore * 100)"
                  :width="140"
                  :color="progressColor"
                />
                <p class="score-text">画像完整度</p>
              </div>
            </el-card>

            <el-card shadow="hover" class="suggestions-card">
              <template #header>
                <div class="card-title">完善建议</div>
              </template>
              <div v-if="suggestions.length" class="suggestions-list">
                <div
                  v-for="(item, index) in suggestions"
                  :key="index"
                  class="suggestion-item"
                  @click="handleSuggestion(item)"
                >
                  <span class="dot"></span>
                  {{ item }}
                </div>
              </div>
              <p v-else class="no-suggestion">🎉 画像已完善！</p>
            </el-card>
          </div>
        </el-col>
      </el-row>
    </div>

    <el-dialog
      v-model="showEditDialog"
      title="编辑用户画像"
      width="640px"
      destroy-on-close
    >
      <el-form :model="editForm" label-width="120px">
        <el-form-item label="学校层次">
          <el-select v-model="editForm.schoolLevel" placeholder="请选择">
            <el-option label="985/211" value="985/211" />
            <el-option label="一本" value="一本" />
            <el-option label="二本" value="二本" />
            <el-option label="民办本科" value="民办本科" />
            <el-option label="专科" value="专科" />
          </el-select>
        </el-form-item>
        <el-form-item label="年级">
          <el-select v-model="editForm.grade" placeholder="请选择">
            <el-option label="大一" value="大一" />
            <el-option label="大二" value="大二" />
            <el-option label="大三" value="大三" />
            <el-option label="大四" value="大四" />
            <el-option label="研究生" value="研究生" />
          </el-select>
        </el-form-item>
        <el-form-item label="专业方向">
          <el-input v-model="editForm.majorDirection" placeholder="如：Java后端开发" />
        </el-form-item>
        <el-form-item label="目标岗位">
          <el-input v-model="editForm.targetPosition" placeholder="如：后端工程师" />
        </el-form-item>
        <el-form-item label="紧迫程度">
          <el-select v-model="editForm.urgencyLevel" placeholder="请选择">
            <el-option label="紧急(6个月内)" value="紧急" />
            <el-option label="一般(1年内)" value="一般" />
            <el-option label="充裕(2年+)" value="充裕" />
          </el-select>
        </el-form-item>
        <el-form-item label="每周学习时间">
          <el-input-number v-model="editForm.weeklyAvailableHours" :min="1" :max="80" />
          <span style="margin-left: 8px; color: var(--text-secondary);">小时</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="saveProfile" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Edit } from '@element-plus/icons-vue'
import { profileApi } from '@/api/profile'

const loading = ref(false)
const saving = ref(false)
const showEditDialog = ref(false)
const profile = ref<any>(null)
const suggestions = ref<string[]>([])

const editForm = ref({
  schoolLevel: '',
  grade: '',
  majorDirection: '',
  targetPosition: '',
  urgencyLevel: '',
  weeklyAvailableHours: null as number | null
})

onMounted(async () => {
  await fetchProfile()
})

async function fetchProfile() {
  loading.value = true
  try {
    const data = await profileApi.getFullProfile()
    profile.value = data

    if (data) {
      editForm.value = {
        schoolLevel: data.education?.schoolLevel || '',
        grade: data.education?.grade || '',
        majorDirection: data.career?.majorDirection || '',
        targetPosition: data.career?.targetPosition || '',
        urgencyLevel: data.time?.urgencyLevel || '',
        weeklyAvailableHours: data.time?.weeklyAvailableHours || null
      }
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

async function initProfile() {
  ElMessage.info('请在编辑框中初始化你的基本信息')
  showEditDialog.value = true
}

async function saveProfile() {
  saving.value = true
  try {
    if (!profile.value) {
      await profileApi.initProfile(editForm.value)
      ElMessage.success('画像初始化成功')
    } else {
      await profileApi.updateFullProfile(editForm.value)
      ElMessage.success('画像更新成功')
    }
    showEditDialog.value = false
    await fetchProfile()
  } catch (error) {
    console.error(error)
  } finally {
    saving.value = false
  }
}

function handleSuggestion(item: string) {
  ElMessage.info(`建议：${item}`)
}

function getSkillType(level?: string): string {
  const levelMap: Record<string, string> = {
    expert: 'danger',
    advanced: 'warning',
    intermediate: 'success',
    beginner: 'info',
    learning: ''
  }
  return levelMap[level || ''] || ''
}

const urgencyType = computed(() => {
  const map: Record<string, string> = {
    '紧急': 'danger',
    '一般': 'warning',
    '充裕': 'success'
  }
  return map[profile.value?.time?.urgencyLevel] || 'info'
})

const progressColor = computed(() => {
  const score = profile.value?.completenessScore || 0
  if (score >= 0.8) return '#10b981'
  if (score >= 0.5) return '#f59e0b'
  return '#ef4444'
})
</script>

<style lang="scss" scoped>
.profile-container {
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

.profile-cards {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.info-card {
  border-radius: 14px;

  .card-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-weight: 600;
    font-size: 15px;

    .icon {
      font-size: 18px;
    }
  }

  :deep(.el-card__header) {
    padding: 16px 20px;
    border-bottom-color: var(--border-color);
  }

  :deep(.el-card__body) {
    padding: 20px;
  }
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 4px;

  label {
    font-size: 12px;
    color: var(--text-muted);
    text-transform: uppercase;
    letter-spacing: 0.5px;
  }

  span, .el-tag {
    font-size: 14px;
    font-weight: 500;
  }
}

.skills-list {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.sidebar-cards {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.completeness-card {
  border-radius: 14px;

  .card-title {
    font-weight: 600;
    font-size: 15px;
  }

  .completeness-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 16px 0;

    .score-text {
      margin-top: 12px;
      color: var(--text-secondary);
      font-size: 13px;
    }
  }
}

.suggestions-card {
  border-radius: 14px;

  .card-title {
    font-weight: 600;
    font-size: 15px;
  }

  .suggestions-list {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }

  .suggestion-item {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 10px 12px;
    background: var(--bg-tertiary);
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.2s;
    font-size: 13px;

    &:hover {
      background: rgba(99, 102, 241, 0.15);
      transform: translateX(4px);
    }

    .dot {
      width: 6px;
      height: 6px;
      border-radius: 50%;
      background: var(--primary-color);
      flex-shrink: 0;
    }
  }

  .no-suggestion {
    text-align: center;
    color: var(--text-secondary);
    font-size: 14px;
  }
}

.empty-profile {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}
</style>
