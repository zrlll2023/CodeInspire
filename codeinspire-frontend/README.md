# CodeInspire Frontend

AI驱动的计算机专业学生个性化顾问系统 - 前端项目

## 技术栈

- **Vue 3** - 渐进式 JavaScript 框架 (Composition API)
- **Vite** - 下一代前端构建工具
- **TypeScript** - 类型安全的 JavaScript 超集
- **Element Plus** - Vue 3 组件库
- **Pinia** - 状态管理
- **Vue Router** - 路由管理
- **ECharts** - 数据可视化
- **Axios** - HTTP 客户端
- **Markdown-it** - Markdown 渲染

## 项目结构

```
codeinspire-frontend/
├── src/
│   ├── api/                    # API 接口层
│   │   ├── index.ts           # Axios 实例配置
│   │   ├── auth.ts            # 认证接口
│   │   ├── chat.ts            # 对话接口
│   │   ├── profile.ts         # 画像接口
│   │   └── plan.ts            # 规划/任务接口
│   ├── assets/                # 静态资源
│   │   └── styles/            # 全局样式
│   │       └── global.scss    # 全局样式变量和基础样式
│   ├── layouts/               # 布局组件
│   │   └── MainLayout.vue     # 主布局（侧边栏 + 头部）
│   ├── router/                # 路由配置
│   │   └── index.ts           # 路由定义和守卫
│   ├── stores/                # Pinia 状态管理
│   │   ├── user.ts            # 用户状态
│   │   └── chat.ts            # 对话状态
│   ├── views/                 # 页面视图
│   │   ├── auth/              # 认证页面
│   │   │   ├── LoginView.vue  # 登录页
│   │   │   └── RegisterView.vue # 注册页
│   │   ├── chat/              # AI对话
│   │   │   └── ChatView.vue   # 对话界面（流式输出）
│   │   ├── profile/           # 用户画像
│   │   │   └── ProfileView.vue # 画像展示与编辑
│   │   ├── plans/             # 学习规划
│   │   │   └── PlansView.vue  # 规划列表与管理
│   │   ├── dashboard/         # 数据看板
│   │   │   └── DashboardView.vue # 可视化图表
│   │   └── settings/          # 设置
│   │       └── SettingsView.vue # 通知设置与账户
│   ├── App.vue                # 根组件
│   ├── main.ts                # 应用入口
│   └── env.d.ts               # TypeScript 声明
├── index.html                 # HTML 入口
├── package.json               # 项目依赖
├── tsconfig.json              # TypeScript 配置
├── vite.config.ts             # Vite 配置
└── README.md                  # 项目说明

## 快速开始

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run dev
```

访问 http://localhost:5173

### 构建生产版本

```bash
npm run build
```

### 预览生产构建

```bash
npm run preview
```

## 功能特性

### 🎨 设计特点
- 暗色主题，护眼设计
- 渐变色装饰元素，现代感强
- 流畅的动画过渡效果
- 响应式布局适配

### 🔐 认证系统
- 登录/注册表单验证
- JWT Token 自动管理
- 路由守卫保护
- 自动过期处理

### 💬 AI对话
- 实时流式消息显示（打字机效果）
- Markdown 格式渲染（代码高亮、表格等）
- 多场景切换（求职建议/技术学习/面试准备）
- 快捷操作入口
- 消息复制、反馈功能

### 👤 用户画像
- 分维度信息展示（教育背景/职业目标/技术能力）
- 完整度可视化进度
- 在线编辑更新
- 智能完善建议

### 📋 学习规划
- 规划 CRUD 管理
- 任务分解与追踪
- 进度可视化
- 统计数据面板

### 📊 数据看板
- 能力雷达图（6维度）
- 技能分布饼图
- 学习进度趋势折线图
- 任务完成情况柱状图
- 核心指标卡片

### ⚙️ 设置中心
- 通知偏好设置
- 免打扰时段配置
- 账户信息展示
- 数据导出功能

## API 代理配置

开发环境下，API 请求自动代理到后端：

```
/api/* -> http://localhost:8080/api/*
/ws/* -> ws://localhost:8080/ws/*
```
