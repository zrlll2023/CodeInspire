<template>
  <div class="dashboard-container">
    <div class="page-header">
      <h2>数据看板</h2>
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        size="default"
        @change="refreshData"
      />
    </div>

    <el-row :gutter="24" class="stats-row">
      <el-col :span="6">
        <div class="stat-card gradient-1">
          <div class="stat-icon">💬</div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalChats }}</div>
            <div class="stat-label">AI对话次数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card gradient-2">
          <div class="stat-icon">📋</div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalTasks }}</div>
            <div class="stat-label">完成任务数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card gradient-3">
          <div class="stat-icon">⏱️</div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.studyHours }}</div>
            <div class="stat-label">学习时长(h)</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card gradient-4">
          <div class="stat-icon">🎯</div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.completeness }}%</div>
            <div class="stat-label">画像完整度</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="24" class="charts-row">
      <el-col :span="16">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-title">能力雷达图</div>
          </template>
          <div ref="radarChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-title">技能分布</div>
          </template>
          <div ref="pieChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="24" class="charts-row">
      <el-col :span="16">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-title">学习进度趋势</div>
          </template>
          <div ref="lineChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-title">任务完成情况</div>
          </template>
          <div ref="barChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import * as echarts from 'echarts'

const dateRange = ref<[Date, Date] | null>(null)
const radarChartRef = ref<HTMLElement>()
const pieChartRef = ref<HTMLElement>()
const lineChartRef = ref<HTMLElement>()
const barChartRef = ref<HTMLElement>()

let radarChart: echarts.ECharts | null = null
let pieChart: echarts.ECharts | null = null
let lineChart: echarts.ECharts | null = null
let barChart: echarts.ECharts | null = null

const stats = ref({
  totalChats: 0,
  totalTasks: 0,
  studyHours: 0,
  completeness: 0
})

onMounted(async () => {
  await nextTick()
  initCharts()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  disposeCharts()
})

function handleResize() {
  radarChart?.resize()
  pieChart?.resize()
  lineChart?.resize()
  barChart?.resize()
}

function disposeCharts() {
  radarChart?.dispose()
  pieChart?.dispose()
  lineChart?.dispose()
  barChart?.dispose()
}

function initCharts() {
  if (radarChartRef.value) {
    radarChart = echarts.init(radarChartRef.value)
    radarChart.setOption({
      radar: {
        indicator: [
          { name: '编程语言', max: 100 },
          { name: '数据库', max: 100 },
          { name: '框架', max: 100 },
          { name: '系统设计', max: 100 },
          { name: '算法', max: 100 },
          { name: '项目经验', max: 100 }
        ],
        shape: 'circle',
        splitNumber: 5,
        axisName: { color: '#94a3b8' },
        splitLine: { lineStyle: { color: 'rgba(148, 163, 184, 0.2)' } },
        splitArea: { areaStyle: { color: ['rgba(99, 102, 241, 0.02)', 'transparent'] } }
      },
      series: [{
        type: 'radar',
        data: [{
          value: [72, 58, 65, 45, 60, 55],
          name: '当前能力',
          areaStyle: { color: 'rgba(99, 102, 241, 0.25)' },
          lineStyle: { color: '#6366f1', width: 2 },
          itemStyle: { color: '#6366f1' }
        }]
      }],
      color: ['#6366f1'],
      backgroundColor: 'transparent'
    })
  }

  if (pieChartRef.value) {
    pieChart = echarts.init(pieChartRef.value)
    pieChart.setOption({
      tooltip: { trigger: 'item' },
      legend: {
        orient: 'vertical',
        left: 'left',
        textStyle: { color: '#94a3b8' }
      },
      series: [{
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: { borderRadius: 10, borderColor: '#1e293b', borderWidth: 2 },
        label: { show: false, position: 'center' },
        emphasis: {
          label: { show: true, fontSize: 14, fontWeight: 'bold' }
        },
        data: [
          { value: 35, name: 'Java', itemStyle: { color: '#ef4444' } },
          { value: 25, name: 'Python', itemStyle: { color: '#f59e0b' } },
          { value: 20, name: '前端', itemStyle: { color: '#10b981' } },
          { value: 15, name: '数据库', itemStyle: { color: '#3b82f6' } },
          { value: 5, name: '其他', itemStyle: { color: '#8b5cf6' } }
        ]
      }],
      backgroundColor: 'transparent'
    })
  }

  if (lineChartRef.value) {
    lineChart = echarts.init(lineChartRef.value)
    lineChart.setOption({
      tooltip: { trigger: 'axis' },
      grid: { top: 40, right: 30, bottom: 40, left: 50 },
      xAxis: {
        type: 'category',
        data: ['第1周', '第2周', '第3周', '第4周'],
        axisLine: { lineStyle: { color: '#334155' } },
        axisLabel: { color: '#94a3b8' }
      },
      yAxis: {
        type: 'value',
        axisLine: { lineStyle: { color: '#334155' } },
        axisLabel: { color: '#94a3b8' },
        splitLine: { lineStyle: { color: 'rgba(51, 65, 85, 0.5)' } }
      },
      series: [{
        data: [45, 52, 61, 68],
        type: 'line',
        smooth: true,
        symbol: 'circle',
        symbolSize: 8,
        lineStyle: { width: 3, color: '#6366f1' },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(99, 102, 241, 0.3)' },
            { offset: 1, color: 'rgba(99, 102, 241, 0)' }
          ])
        },
        itemStyle: { color: '#6366f1', borderColor: '#fff', borderWidth: 2 }
      }],
      backgroundColor: 'transparent'
    })
  }

  if (barChartRef.value) {
    barChart = echarts.init(barChartRef.value)
    barChart.setOption({
      tooltip: { trigger: 'axis' },
      grid: { top: 40, right: 20, bottom: 40, left: 50 },
      xAxis: {
        type: 'category',
        data: ['已完成', '进行中', '待开始', '已逾期'],
        axisLine: { lineStyle: { color: '#334155' } },
        axisLabel: { color: '#94a3b8' }
      },
      yAxis: {
        type: 'value',
        axisLine: { lineStyle: { color: '#334155' } },
        axisLabel: { color: '#94a3b8' },
        splitLine: { lineStyle: { color: 'rgba(51, 65, 85, 0.5)' } }
      },
      series: [{
        data: [
          { value: 12, itemStyle: { color: '#10b981' } },
          { value: 5, itemStyle: { color: '#3b82f6' } },
          { value: 3, itemStyle: { color: '#f59e0b' } },
          { value: 1, itemStyle: { color: '#ef4444' } }
        ],
        type: 'bar',
        barWidth: 40,
        itemStyle: { borderRadius: [6, 6, 0, 0] }
      }],
      backgroundColor: 'transparent'
    })
  }

  stats.value = {
    totalChats: 47,
    totalTasks: 21,
    studyHours: 86,
    completeness: 73
  }
}

async function refreshData() {
  ElMessage.info('数据刷新中...')
}
</script>

<style lang="scss" scoped>
.dashboard-container {
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
    display: flex;
    align-items: center;
    gap: 16px;
    padding: 24px;
    border-radius: 14px;
    background: var(--card-bg);
    border: 1px solid var(--border-color);

    .stat-icon {
      width: 56px;
      height: 56px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 28px;
      border-radius: 12px;
    }

    .stat-value {
      font-size: 28px;
      font-weight: 700;
      color: var(--text-primary);
    }

    .stat-label {
      font-size: 13px;
      color: var(--text-secondary);
      margin-top: 2px;
    }

    &.gradient-1 .stat-icon { background: rgba(99, 102, 241, 0.15); }
    &.gradient-2 .stat-icon { background: rgba(16, 185, 129, 0.15); }
    &.gradient-3 .stat-icon { background: rgba(245, 158, 11, 0.15); }
    &.gradient-4 .stat-icon { background: rgba(139, 92, 246, 0.15); }
  }
}

.charts-row {
  margin-bottom: 24px;
}

.chart-card {
  border-radius: 14px;

  .card-title {
    font-weight: 600;
    font-size: 15px;
  }

  .chart-container {
    height: 320px;
  }
}
</style>
