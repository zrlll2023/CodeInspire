# AI灵感规划助手 - 需求文档

## 1. 项目概述

### 1.1 项目名称
**CodeInspire** - 计算机专业学生AI个性化顾问系统

### 1.2 核心定位
面向计算机专业大学生和后端开发者的AI驱动顾问系统，通过智能信息收集、个性化分析和主动建议，为不同背景的学生提供精准的技术路线、求职策略和成长规划。系统强调**信息完整收集**和**真实数据驱动**，确保AI输出的建议具有针对性和时效性。

### 1.3 目标用户
- 计算机专业在校大学生（不同层次：985/211/一本/二本/民办）
- 后端开发初学者
- 需要技术路线规划和求职策略的IT学习者
- 寻求个性化发展建议的开发者

---

## 2. 功能需求

### 2.1 用户画像系统 ⭐⭐⭐

#### 2.1.1 用户背景信息采集

##### 📚 教育背景
| 维度 | 选项 | 说明 |
|------|------|------|
| 学校层次 | 985/211/一本/二本/独立学院/民办本科/专科 | 影响求职策略 |
| 学校类型 | 综合类/理工类/师范类/专科等 | 影响资源获取 |
| 学历层次 | 专科/本科/硕士/博士 | 影响发展路径 |
| 所读专业 | 计算机科学与技术/软件工程/信息安全等 | 影响技术方向 |

##### 💻 技术背景
| 维度 | 说明 |
|------|------|
| 当前技术栈 | 用户已掌握的编程语言、框架、工具 |
| 项目经验 | 实习经历、个人项目、竞赛项目、开源贡献 |
| 认证证书 | 技术认证、专业资格证书 |

##### ⏰ 时间维度
| 维度 | 选项 | 说明 |
|------|------|------|
| 当前年级 | 大一/大二/大三/大四/研一/研二/研三 | 影响阶段性策略 |
| 紧迫程度 | 紧急（6个月内）/ 一般（1年内）/ 充裕（2年+） | 影响投入强度 |
| 每周可用时间 | <10小时 / 10-20小时 / 20-30小时 / >30小时 | 影响学习强度 |
| 课业压力 | 轻松/一般/繁重/考研准备中 | 影响可支配时间 |

##### 🌆 地理位置
| 维度 | 选项 | 说明 |
|------|------|------|
| 目标城市级别 | 一线（北京/上海/深圳/广州）/ 新一线（杭州/成都/南京等）/ 二三线 | 影响薪资和机会 |
| 家乡/户籍 | 是否需要考虑户口、离家距离 | 影响城市选择 |
| 产业聚集偏好 | 电商(杭州)/游戏(深圳)/金融(上海)/互联网(北京) | 影响细分领域 |

##### 💰 经济约束
| 维度 | 选项 | 说明 |
|------|------|------|
| 付费意愿 | 完全免费路线 / 可接受低价课程(<500) / 可接受付费(500-2000) / 不差钱 | 影响学习资源选择 |
| 电脑配置 | 普通笔记本 / 游戏本 / 台式机 / Mac | 影响开发方向选择 |
| 自学能力 | 强（免费资源足够）/ 中（需要引导）/ 弱（需要系统化课程） | 影响学习路径 |
| 经济压力 | 无负担 / 有一定压力 / 需要尽快变现 | 影响求职紧迫性 |

##### 🎯 求职目标
| 维度 | 说明 |
|------|------|
| 目标岗位 | 后端开发/全栈/嵌入式/运维/DevOps/测试开发等 |
| 目标企业 | 大厂/中厂/小厂/外企/国企/创业公司/考公 |
| 期望薪资 | 根据城市和级别有不同范围 |

#### 2.1.2 个性化建议引擎
- **差异化建议生成**：根据用户背景生成针对性的求职、学习策略
  - 985学生：侧重精英路线、技术深度、项目背书、大厂直通车
  - 普通一本/二本：侧重项目经验、竞赛奖项、技能广度、中厂策略
  - 民办本科/专科：侧重实战能力、特定赛道（小厂/外包）、差异化竞争
- **竞争策略分析**：分析同层次竞争者的优劣势，提供差异化发展路径
- **机会识别**：识别特定背景下的隐藏机会（如特定企业的人才偏好）
- **风险预警**：提醒特定背景下需要规避的弯路

### 2.2 智能信息收集系统 ⭐⭐

#### 2.2.1 信息收集规则引擎
- **场景化必填信息定义**：每个咨询场景定义必填信息项
- **信息完整性检测**：自动检测用户输入是否包含所有必填信息
- **主动追问机制**：信息不完整时，主动向用户提问补充缺失信息
- **信息优先级排序**：根据场景重要性，优先收集关键信息

#### 2.2.2 场景化必填信息定义

##### 💻 技术栈推荐场景
| 必填项 | 来源 | 说明 |
|--------|------|------|
| 用户当前技术栈 | 用户提供 | 已掌握的编程语言、框架、工具 |
| 目标技术方向 | 用户提供 | 想学习的方向（如：Java后端、云原生） |
| 当前流行技术 | 系统资料库提供 | 自动展示最新技术趋势 |

##### 💼 求职建议场景
| 必填项 | 来源 | 说明 |
|--------|------|------|
| 用户背景画像 | 系统已有 | 学校层次、年级、技术水平 |
| 目标岗位/公司 | 用户提供 | 想投什么岗位、什么级别的公司 |
| 竞争环境分析 | 系统资料库提供 | 该岗位的市场需求和竞争情况 |

##### 📚 学习路径规划场景
| 必填项 | 来源 | 说明 |
|--------|------|------|
| 当前技术水平 | 用户提供 | 起点在哪里 |
| 目标方向 | 用户提供 | 想达到什么水平 |
| 可用时间 | 用户提供 | 每天/每周能投入多少时间 |
| 时间期限 | 用户提供 | 多久内要达到目标 |

##### 📄 简历优化场景
| 必填项 | 来源 | 说明 |
|--------|------|------|
| 用户背景 | 系统已有 | 学校、项目经验 |
| 目标岗位 | 用户提供 | 想投什么岗位 |
| 现有简历 | 用户提供 | 当前简历内容 |

#### 2.2.3 智能引导与决策辅助系统

##### 2.2.3.1 渐进式引导提问
多层次提问策略（封闭式→选择题→排序题→开放式确认）

##### 2.2.3.2 类比解释系统
用用户能理解的事物来解释抽象概念

##### 2.2.3.3 自我评估引导
帮助用户客观评估自己的能力水平

##### 2.2.3.4 不确定时的保守策略
透明告知、提供选项、建议验证、标记置信度

##### 2.2.3.5 决策树辅助
对于常见问题，提供可视化决策树引导

#### 2.2.4 多AI交叉验证
- **并行查询**：同时向多个AI模型发送相同查询
- **结果对比**：对比不同AI的分析差异
- **综合判断**：基于多AI结果给出更可靠的推荐
- **置信度展示**：展示不同AI的共识程度

#### 2.2.5 信息分类
| 信息类型 | 来源 | 说明 |
|---------|------|------|
| **用户内部信息** | 用户提供 | 技术储备、项目经验、学习目标、个人背景等 |
| **市场外部信息** | 系统资料库 | 流行技术栈、企业招聘趋势、行业薪资水平等 |

#### 2.2.6 技术趋势资料库
- **数据来源**：主流招聘平台、GitHub、StackOverflow、技术社区
- **更新机制**：每周自动更新热门技术排行
- **数据展示**：来源标注（北京时间）、可信度评级、多AI交叉验证

### 2.3 反馈追踪系统 📊

#### 2.3.1 进度监控
- **里程碑检测**：自动检测关键学习/求职里程碑是否达成
- **执行状态追踪**：跟踪用户对建议的执行情况
- **数据可视化**：图表展示学习进度和求职进展

#### 2.3.2 智能回访机制
- **定期回访**：按计划节点询问用户执行效果
- **效果评估**：收集用户对建议的实际效果反馈
- **满意度调查**：评估建议的实用性和可执行性

#### 2.3.3 动态策略调整
- **策略自适应**：根据执行效果自动调整后续建议
- **失败预警**：当用户明显落后计划时主动预警

#### 2.3.4 顾问报告生成
- **定期报告**：每周/每月生成个人进展报告
- **对比分析**：对比用户画像相近的群体的平均进展
- **个性化优化建议**：基于数据提出针对性改进建议

### 2.4 学习效果可视化系统 📊

#### 2.4.1 能力雷达图
多维度展示用户的技术能力水平：
```
能力雷达图示例：
         Java
          ↑
     数据库 ●
    ↙     ↑    ↘
前端  ●   ↑   ●  架构
    ↘     ↑    ↙
     ●  算法  ●
    ↙     ↓    ↙
      系统设计
```
| 维度 | 说明 |
|------|------|
| 编程语言 | Java/Python/Go等掌握程度 |
| 数据库 | SQL/NoSQL/缓存等 |
| 框架 | Spring Boot/Django等 |
| 系统设计 | 架构/分布式/微服务等 |
| 算法 | LeetCode刷题量/正确率 |

#### 2.4.2 进度追踪曲线
可视化展示学习进展：
| 图表类型 | 展示内容 |
|---------|---------|
| 周学习时长 | 每周代码学习时间趋势 |
| 技能成长曲线 | 各技能随时间的提升 |
| 任务完成率 | 计划任务按时完成的比例 |
| 求职进度时间线 | 简历投递/面试/offer进展 |

#### 2.4.3 同伴对比分析
| 对比维度 | 说明 |
|---------|------|
| 进展对比 | 与同学校/同年级用户的进度对比 |
| 能力分布 | 在群体中的能力位置 |
| 建议调整 | 根据对比结果给出追赶建议 |

### 2.5 关键时间节点提醒系统 🔔

#### 2.5.1 计算机专业时间线
| 年级 | 时间节点 | 提醒内容 |
|------|---------|---------|
| 大一 | 学期末 | 下学期实习准备开始 |
| 大二 | 3-4月 | 暑期实习申请季开始 |
| 大二 | 6-8月 | 暑期实习关键期 |
| 大三 | 9-11月 | 暑期实习复盘+秋招提前批 |
| 大三 | 3-5月 | 春招实习+秋招准备 |
| 大三 | 6-8月 | 暑期实习+秋招备战 |
| 大四 | 8-10月 | **秋招正式批（金九银十）** |
| 大四 | 11-12月 | 秋招补录+春招准备 |
| 大四 | 3-5月 | 春招补录+毕设冲刺 |

#### 2.5.2 智能提醒机制
- **提前30天**：重要节点前主动提醒
- **每日摘要**：每天推送当日任务和deadline
- **进度预警**：落后于计划时提醒
- **机会提醒**：发现适合用户的实习/招聘信息时通知

### 2.6 应急场景处理系统 🆘

#### 2.6.1 常见应急场景
| 场景 | 系统响应 |
|------|---------|
| **考研失败** | 紧急求职方案、快速备战春招 |
| **秋招失败** | 分析原因、备战春招策略、降低期望或提升实力 |
| **实习被裁** | 心理疏导、快速调整方案、推荐新机会 |
| **签约违约** | 违约金处理、新机会推荐、风险提示 |
| **面试失利** | 问题分析、答题技巧提升、针对性练习 |
| **技术焦虑** | 能力评估、制定可执行的小目标、重建信心 |

#### 2.6.2 应急响应流程
```
触发词检测 → 情绪识别 → 问题诊断 → 应急方案 → 跟踪关怀

示例：
用户："秋招全部被拒了，我不想活了"
      ↓
系统："我理解你现在很沮丧，让我们一起分析一下问题所在。
        失败不是终点，而是成长的机会。
        能否告诉我你面试失败的原因是什么？"
```

### 2.7 新用户引导流程 🚶

#### 2.7.1 5分钟快速入门
| 步骤 | 时间 | 内容 |
|------|------|------|
| 1 | 1分钟 | 欢迎语+系统介绍 |
| 2 | 2分钟 | 快速画像（学校/年级/目标） |
| 3 | 1分钟 | 初始诊断（技术自评） |
| 4 | 1分钟 | 推荐第一个小目标 |

#### 2.7.2 引导对话示例
```
AI：欢迎使用CodeInspire！我是你的AI学习顾问 🎓

让我先了解一下你的情况（大约需要2分钟）：

Q1: 你现在是大几？
   A. 大一  B. 大二  C. 大三  D. 大四  E. 研究生

用户：C

Q2: 你的学校是？
   A. 985/211  B. 一本  C. 二本  D. 其他

用户：B

Q3: 你目前最想解决的问题是？
   A. 学习方向不明确
   B. 找不到实习
   C. 备战秋招
   D. 毕设难题

用户：C

[基于回答生成个性化初始诊断和第一个小目标]
```

### 2.8 数据迁移与导出系统 📥

#### 2.8.1 导出格式
| 导出类型 | 格式 | 说明 |
|---------|------|------|
| 简历 | Word/PDF | 格式精美，可直接使用 |
| 学习计划 | PDF/日历 | 可导入Google Calendar/钉钉 |
| 对话记录 | Markdown | 便于整理笔记 |
| 账户数据 | JSON | 完整数据备份，隐私合规 |

#### 2.8.2 隐私合规
- **数据删除权**：用户可一键清除所有个人数据
- **数据可携带性**：支持账户数据导出
- **删除确认**：删除前二次确认，防止误操作

### 2.9 智能成本控制系统 ⚙️

#### 2.9.1 模型智能路由
根据任务复杂度选择最合适的模型：
| 任务类型 | 推荐模型 | 理由 |
|---------|---------|------|
| 简单问答 | 小米MiMo | 快速、免费额度充足 |
| 技术解释 | 智谱GLM | 中文理解好 |
| 代码生成 | 通义Qwen | 代码能力强 |
| 复杂分析 | DeepSeek | 推理能力强 |
| 综合建议 | 多模型交叉验证 | 结果更可靠 |

#### 2.9.2 缓存策略
| 缓存类型 | 时效 | 说明 |
|---------|------|------|
| 通用问题缓存 | 24小时 | 常见问题直接返回 |
| 技术解释缓存 | 7天 | 技术概念相对稳定 |
| 用户画像缓存 | 实时 | 每次对话参考 |

#### 2.9.3 限流保护
- **接口限流**：每人每分钟最多30次请求
- **AI调用限制**：普通用户每天最多100次AI对话
- **恶意检测**：异常行为自动封禁

### 2.10 Prompt模板库 🎯

#### 2.10.1 场景化Prompt模板
针对不同场景预置专业Prompt模板：
| 场景 | 模板数量 | 说明 |
|------|---------|------|
| 技术栈推荐 | 10+ | 根据用户背景推荐技术栈 |
| 学习路径规划 | 10+ | 不同阶段的学习路线 |
| 简历优化 | 5+ | 不同岗位的简历模板 |
| 面试准备 | 20+ | 各技术方向的面试题 |
| 求职策略 | 10+ | 不同背景的求职方案 |
| 毕设辅助 | 10+ | 选题、技术方案、论文 |

#### 2.10.2 Prompt版本管理
| 功能 | 说明 |
|------|------|
| 版本记录 | 记录每次Prompt的修改历史 |
| 效果追踪 | 关联Prompt版本与用户满意度 |
| 回滚机制 | 支持回退到历史版本 |
| A/B测试 | 支持多版本Prompt同时上线 |

#### 2.10.3 Prompt优化机制
```
Prompt迭代流程：
问题发现 → 原因分析 → Prompt调整 → 效果验证 → 正式上线

示例：
用户反馈："简历建议太笼统"
    ↓
分析原因：Prompt中缺少"根据用户具体经历生成建议"的指令
    ↓
优化Prompt：增加"请结合用户的具体项目经历，给出可操作的具体建议"
    ↓
验证效果：抽样用户满意度提升30%
    ↓
正式上线
```

### 2.11 内置知识库 📖

#### 2.11.1 面试题库
| 分类 | 内容 |
|------|------|
| Java基础 | 集合、多线程、JVM、并发 |
| 数据库 | MySQL索引、事务、Redis |
| 框架 | Spring Boot原理、MyBatis |
| 系统设计 | 分布式、微服务、缓存设计 |
| 算法 | LeetCode高频100题分类 |
| 项目管理 | Git、Docker、K8s |

#### 2.11.2 学习资源库
| 类型 | 内容 |
|------|------|
| 免费视频 | B站、YouTube优质课程推荐 |
| 付费课程 | 各平台付费课程评测 |
| 书籍推荐 | 入门/进阶/经典书籍清单 |
| 学习路线 | 各技术方向的系统学习路径 |

#### 2.11.3 薪资数据
| 数据维度 | 说明 |
|---------|------|
| 城市薪资 | 一线/新一线/二线城市薪资对比 |
| 公司级别 | 大厂/中厂/小厂薪资范围 |
| 职级体系 | P5/P6/P7对应薪资 |
| 校招薪资 | 各公司校招offer薪资 |

#### 2.11.4 知识库更新机制
- **自动更新**：每周从公开渠道抓取最新数据
- **人工审核**：敏感信息人工确认后上线
- **版本标注**：每条知识标注来源和更新时间

### 2.12 通知系统 🔔

#### 2.12.1 通知类型
| 通知类型 | 触发条件 | 推送方式 |
|---------|---------|---------|
| AI回复通知 | AI生成完成 | WebSocket |
| 任务提醒 | 任务到期前1天/当天 | WebSocket+邮件 |
| 时间节点提醒 | 重要节点前30天 | WebSocket+邮件 |
| 进度预警 | 计划落后时 | WebSocket |
| 每日摘要 | 每天早上9点 | 邮件 |
| 系统公告 | 重要通知 | 站内消息 |

#### 2.12.2 通知渠道
| 渠道 | 说明 |
|------|------|
| 站内消息 | 系统消息中心 |
| WebSocket | 实时推送 |
| 邮件 | 重要事项邮件通知 |

#### 2.12.3 通知管理
- **免打扰设置**：用户可设置免打扰时段
- **通知偏好**：用户可选择接收哪些通知
- **历史记录**：保留通知历史记录

### 2.13 数据统计与运营后台 📈

#### 2.13.1 用户统计
| 指标 | 说明 |
|------|------|
| DAU/WAU/MAU | 日活/周活/月活用户数 |
| 新增用户 | 每日/每周新增注册用户 |
| 留存率 | 次日/7日/30日留存 |
| 用户画像分布 | 用户背景数据统计 |

#### 2.13.2 AI调用统计
| 指标 | 说明 |
|------|------|
| 调用量 | 各模型每日/每周/每月调用量 |
| 成功率 | AI调用成功率 |
| 响应时间 | 平均响应时间/P95响应时间 |
| 成本统计 | 各模型调用成本 |

#### 2.13.3 内容质量统计
| 指标 | 说明 |
|------|------|
| 用户满意度 | 用户对AI回复的满意度评分 |
| 反馈统计 | 点赞/踩的统计 |
| 高频问题 | 用户最常问的问题TOP100 |
| 失败案例 | AI回复效果不好的案例分析 |

#### 2.13.4 运营后台功能
| 功能 | 说明 |
|------|------|
| 数据看板 | 核心指标的可视化展示 |
| 报表导出 | 数据导出为Excel |
| 实时监控 | 系统健康状态监控 |
| 预警通知 | 指标异常时自动告警 |

### 2.14 A/B测试框架 🧪

#### 2.14.1 测试类型
| 测试类型 | 用途 | 示例 |
|---------|------|------|
| Prompt测试 | 不同Prompt效果对比 | "简历建议"Prompt A vs Prompt B |
| 模型测试 | 不同AI模型效果对比 | 小米MiMo vs DeepSeek |
| 策略测试 | 不同推荐策略效果对比 | 个性化推荐 vs 通用推荐 |

#### 2.14.2 实验配置
| 配置项 | 说明 |
|------|------|
| 流量分配 | 实验组/对照组的流量比例 |
| 用户分组 | 按用户特征分组 |
| 指标定义 | 衡量标准（满意度/转化率等） |
| 最小样本 | 统计显著性所需最小样本数 |

#### 2.14.3 结果分析
```
实验流程：
设计实验 → 分配流量 → 收集数据 → 统计分析 → 得出结论

分析指标：
- 核心指标提升
- 置信区间
- 统计显著性（p-value）
- 结论：推广/继续测试/放弃
```

### 2.15 内容审核机制 🔍

#### 2.15.1 敏感词过滤
| 过滤类型 | 说明 |
|---------|------|
| 政治敏感 | 涉及政治的敏感话题 |
| 违规内容 | 广告、色情、暴力等内容 |
| 个人信息 | 身份证、银行卡等敏感信息 |
| 商业敏感 | 商业机密、技术专利等 |

#### 2.15.2 质量评分
| 评分维度 | 说明 |
|---------|------|
| 准确性 | 回复内容的准确程度 |
| 完整性 | 是否完整回答了用户问题 |
| 专业性 | 是否符合专业标准 |
| 可读性 | 回复是否清晰易懂 |

#### 2.15.3 审核流程
```
AI生成 → 敏感词过滤 → 质量评分 → 人工抽检 → 结果记录

处理规则：
- 敏感词命中 → 直接拦截，提示用户
- 质量低分 → 标记，提醒人工审核
- 人工抽检 → 高风险内容必检
- 审核记录 → 保存完整审核日志
```

### 2.16 第三方集成 🔗

#### 2.16.1 GitHub集成
| 功能 | 说明 |
|------|------|
| 仓库绑定 | 用户绑定GitHub账号 |
| 项目展示 | 自动同步GitHub项目到简历 |
| 贡献统计 | 展示开源贡献数据 |
| 代码能力评估 | 基于提交记录评估代码能力 |

#### 2.16.2 LeetCode集成
| 功能 | 说明 |
|------|------|
| 账号绑定 | 用户绑定LeetCode账号 |
| 刷题数据 | 同步刷题数量/通过率 |
| 能力雷达图 | 基于AC率生成能力图 |
| 面试评估 | 根据刷题量评估面试通过率 |

#### 2.16.3 招聘平台集成

**实现说明**：Boss直聘、猎聘等招聘平台不提供公开API，因此简历同步功能采用以下方案：

| 功能 | 实现方式 |
|------|---------|
| 简历导入 | 用户手动粘贴简历文本，系统自动解析结构化信息 |
| 投递记录 | 用户手动录入或上传截图备注 |
| Offer管理 | 用户手动录入offer信息 |

**后续扩展方案**：
- 浏览器插件（用户授权后抓取页面数据）
- PDF简历解析（用户上传PDF，系统提取内容）

#### 2.16.4 第三方API
| 集成 | API类型 | 用途 |
|------|---------|------|
| GitHub | REST API | 同步项目数据 |
| LeetCode | 非官方API | 同步刷题数据 |

### 2.17 系统日志与审计 📋

#### 2.17.1 日志类型
| 日志类型 | 内容 | 保留时间 |
|---------|------|---------|
| 操作日志 | 用户登录、修改信息等操作 | 1年 |
| AI调用日志 | 完整的AI对话记录 | 6个月 |
| 接口调用日志 | API请求和响应 | 3个月 |
| 异常日志 | 系统异常的堆栈信息 | 1年 |
| 审计日志 | 管理员操作记录 | 永久 |

#### 2.17.2 敏感操作审计
| 操作类型 | 审计要求 |
|---------|---------|
| 账户注销 | 记录操作人、时间、原因 |
| 数据导出 | 记录导出内容和接收人 |
| 权限变更 | 记录权限变更前后对比 |
| AI回复投诉 | 记录投诉内容和处理结果 |

#### 2.17.3 日志规范
```
日志格式：
[时间戳] [级别] [模块] [操作] [用户ID] [详情]

示例：
[2025-05-07 10:30:15] [INFO] [AI_SERVICE] [chat_completion] [user:12345] [model:mimo, tokens:500]

日志级别：
- DEBUG: 调试信息
- INFO: 正常操作
- WARN: 警告信息
- ERROR: 错误信息
```

### 2.18 核心功能模块

#### 2.18.1 编程学习指导
- **语法与概念解释**：针对Java/Python/Go等语言的语法和概念提供解释
- **代码示例生成**：根据需求生成示例代码
- **错误分析**：帮助分析代码错误并提供解决方案
- **学习路径推荐**：根据用户水平推荐学习路线

#### 2.18.2 技术栈建议
- **技术选型咨询**：根据项目需求推荐合适的技术栈
- **框架对比分析**：对比Spring Boot、Django等框架的优劣
- **工具推荐**：推荐开发工具、插件、库等
- **最新技术追踪**：提供技术趋势和建议

#### 2.18.3 求职就业指导
- **简历优化**：根据用户背景提供简历撰写建议和改进方向
- **面试准备**：提供针对性面试题库和答题思路
- **Offer分析**：帮助分析不同offer的优劣，考虑背景因素
- **职业规划**：提供职业发展建议，考虑差异化竞争策略

#### 2.18.4 毕业设计辅助
- **选题推荐**：根据兴趣和技术水平推荐毕设题目
- **技术方案设计**：协助设计系统架构和技术方案
- **论文写作指导**：提供技术文档和论文写作建议
- **项目进度规划**：帮助规划毕设时间安排

#### 2.18.5 规划管理模块
- **目标设定**：协助用户设定学习目标和计划
- **任务分解**：将大目标分解为可执行的小任务
- **时间规划**：提供时间节点建议和进度跟踪
- **进度回顾**：定期提醒用户回顾和调整计划

#### 2.18.6 AI对话交互
- **多轮对话支持**：支持上下文连贯的多轮对话
- **对话历史管理**：保存和管理对话记录
- **对话导出**：支持将会话内容导出为Markdown文档

### 2.19 WebSocket 实时接口
- 实时流式输出（打字机效果）
- 实时状态推送

---

## 3. 技术架构

### 3.1 技术栈

| 层级 | 技术选型 | 说明 |
|------|----------|------|
| **语言** | Java 17+ | 稳定版本，长期支持 |
| **框架** | Spring Boot 3.x | 主流企业级框架 |
| **AI框架** | Spring AI | 统一的多模型调用框架 |
| **API风格** | REST + WebSocket | 同步+实时双通道 |
| **AI模型** | 小米MiMo、DeepSeek、智谱GLM、通义千问 | 多模型支持，通过Spring AI统一接入 |
| **向量数据库** | Qdrant / Milvus | RAG知识库向量检索 |
| **前端** | Vue 3 + Vite + Element Plus | 现代化响应式Web界面 |
| **数据库** | MySQL 8.x | 关系型数据存储 |
| **缓存** | Redis | 会话缓存、Token管理、热点数据缓存 |
| **安全** | Spring Security + JWT | 认证授权 |
| **文档** | SpringDoc OpenAPI | API文档生成 |
| **构建** | Maven | 项目构建管理 |

### 3.1.1 AI与数据架构层 ⭐

#### 向量数据库选型
| 向量数据库 | 选型理由 |
|-----------|---------|
| **Milvus** | 开源、分布式、支持海量向量、社区活跃 |
| **Qdrant** | 轻量级、部署简单、Rust实现性能好 |

**选型建议**：初期使用 Qdrant（部署简单），后期数据量大后迁移到 Milvus

#### RAG工作流定义
```
用户提问 → 隐私脱敏 → 向量检索 → Prompt拼装 → LLM生成 → 质量审核 → 返回结果

详细流程：
1. 用户输入："帮我看看我的简历有什么问题"
2. 隐私脱敏层：自动识别并脱敏姓名、电话、邮箱等PII
3. 向量检索：从知识库检索相关简历模板和面试技巧
4. Prompt拼装：
   - System: 你是一个专业的简历优化顾问
   - Context: 检索到的简历优化知识
   - User: 用户问题（已脱敏）
5. LLM生成：调用AI模型生成建议
6. 质量审核：敏感词过滤 + 质量评分
7. 返回结果：结构化的简历优化建议
```

#### LLM隐私脱敏机制 ⭐⭐
**为什么重要**：用户简历包含大量PII，发送给第三方API前必须脱敏

| 脱敏类型 | 示例 | 处理方式 |
|---------|------|---------|
| 姓名 | 张三 | [姓名] |
| 手机号 | 13812345678 | [手机号] |
| 邮箱 | user@email.com | [邮箱] |
| 身份证 | 110101199001011234 | [身份证号] |
| 地址 | 北京市朝阳区xxx | [地址] |
| 公司名称 | xx公司 | [公司名称] |

**脱敏流程**：
```
用户输入 → 正则匹配PII → 替换为占位符 → 发送给LLM
                              ↓
                        记录脱敏映射
                              ↓
LLM输出 → 还原PII → 返回用户
```

**脱敏规则配置**：
```yaml
privacy:
  patterns:
    phone: "1[3-9]\d{9}"
    email: "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}"
    id_card: "\d{17}[\dXx]"
  replacement: "[敏感信息]"
```

### 3.2 系统架构设计

```
┌─────────────────────────────────────────────────────────┐
│                      客户端层                            │
│         (Web / App / 小程序 / 第三方系统)                │
└─────────────────────┬───────────────────────────────────┘
                      │ HTTP / WebSocket
┌─────────────────────▼───────────────────────────────────┐
│                    API 网关层                            │
│              (认证 / 限流 / 路由转发)                     │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│                   业务服务层                              │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐   │
│  │ 用户服务  │ │ 灵感服务  │ │ 建议服务  │ │ 规划服务  │   │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘   │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│                   AI 服务层                              │
│     (Spring AI / 多模型调用 / 流式输出 / Prompt工程)    │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│                   知识库层                              │
│        (Qdrant向量数据库 / 知识检索 / RAG)              │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│                   数据层                                 │
│   MySQL (持久化) + Redis (缓存) + Qdrant (向量)         │
└─────────────────────────────────────────────────────────┘
```

### 3.3 数据库设计

#### 用户表 (users)
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### 用户画像表 (user_profiles) ⭐
```sql
CREATE TABLE user_profiles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    -- 教育背景
    school_level VARCHAR(30) NOT NULL COMMENT '985/211/一本/二本/独立学院/民办本科/专科',
    school_type VARCHAR(30) COMMENT '综合类/理工类/师范类/专科等',
    education_level VARCHAR(20) NOT NULL COMMENT '专科/本科/硕士/博士',
    major VARCHAR(100) COMMENT '所读专业：计算机科学与技术/软件工程/信息安全等',
    -- 时间维度
    grade VARCHAR(20) COMMENT '大一/大二/大三/大四/研一/研二等',
    urgency_level VARCHAR(20) COMMENT '紧迫程度：紧急(6个月内)/一般(1年内)/充裕(2年+)',
    weekly_available_hours INT COMMENT '每周可用学习时间(小时)',
    coursework_pressure VARCHAR(20) COMMENT '课业压力：轻松/一般/繁重/考研准备中',
    -- 地理位置
    target_city_level VARCHAR(30) COMMENT '目标城市级别：一线/新一线/二三线',
    hometown_consideration VARCHAR(50) COMMENT '户籍/家乡考虑',
    industry_preference VARCHAR(50) COMMENT '产业聚集偏好：电商/游戏/金融/互联网等',
    -- 经济约束
    payment_willingness VARCHAR(30) COMMENT '付费意愿：完全免费/低价(<500)/可付费(500-2000)/不差钱',
    computer_config VARCHAR(30) COMMENT '电脑配置：普通笔记本/游戏本/台式机/Mac',
    self_learning_ability VARCHAR(20) COMMENT '自学能力：强/中/弱',
    economic_pressure VARCHAR(20) COMMENT '经济压力：无负担/有一定压力/需要尽快变现',
    -- 求职目标
    current_status VARCHAR(30) COMMENT '当前位置：大一在读/春招/秋招/二战/待业等',
    major_direction VARCHAR(50) COMMENT '专业方向：后端开发/前端开发/全栈/AI等',
    target_position VARCHAR(50) COMMENT '目标岗位：后端/全栈/嵌入式/运维/DevOps等',
    target_company VARCHAR(50) COMMENT '目标企业：大厂/中厂/小厂/外企/国企/创业公司/考公',
    expected_salary VARCHAR(30) COMMENT '期望薪资',
    -- 技术背景
    skills TEXT COMMENT '掌握的技能，JSON格式存储',
    projects TEXT COMMENT '项目经验，JSON格式存储',
    certifications TEXT COMMENT '认证证书，JSON格式存储',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

#### 灵感记录表 (inspirations)
```sql
CREATE TABLE inspirations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(200),
    content TEXT NOT NULL,
    category VARCHAR(50),
    tags JSON,
    is_favorite BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

#### 规划表 (plans)
```sql
CREATE TABLE plans (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    status VARCHAR(20) DEFAULT 'active',
    start_date DATE,
    end_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

#### 任务表 (tasks)
```sql
CREATE TABLE tasks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    plan_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    status VARCHAR(20) DEFAULT 'pending',
    priority INT DEFAULT 0,
    due_date DATE,
    completed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (plan_id) REFERENCES plans(id)
);
```

#### 对话记录表 (conversations)
```sql
CREATE TABLE conversations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    session_id VARCHAR(100) NOT NULL,
    type VARCHAR(30) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

#### 消息记录表 (messages)
```sql
CREATE TABLE messages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    conversation_id BIGINT NOT NULL,
    role VARCHAR(20) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (conversation_id) REFERENCES conversations(id)
);
```

#### Prompt模板表 (prompts) ⭐
```sql
CREATE TABLE prompts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '模板名称',
    scene VARCHAR(50) NOT NULL COMMENT '应用场景',
    content TEXT NOT NULL COMMENT 'Prompt模板内容',
    variables JSON COMMENT '模板变量定义',
    version INT DEFAULT 1 COMMENT '版本号',
    status VARCHAR(20) DEFAULT 'active' COMMENT '状态:active/draft/deprecated',
    is_ab_test BOOLEAN DEFAULT FALSE COMMENT '是否参与A/B测试',
    ab_group VARCHAR(10) COMMENT 'A/B测试分组',
    usage_count INT DEFAULT 0 COMMENT '使用次数',
    satisfaction_score DECIMAL(3,2) COMMENT '平均满意度评分',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### Prompt版本历史表 (prompt_versions)
```sql
CREATE TABLE prompt_versions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    prompt_id BIGINT NOT NULL,
    version INT NOT NULL COMMENT '版本号',
    content TEXT NOT NULL COMMENT '模板内容快照',
    change_log TEXT COMMENT '变更说明',
    created_by BIGINT COMMENT '创建人ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (prompt_id) REFERENCES prompts(id)
);
```

#### 知识库索引表 (knowledge_base) ⭐
```sql
CREATE TABLE knowledge_base (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category VARCHAR(50) NOT NULL COMMENT '知识分类:interview/resources/salary/company',
    subcategory VARCHAR(100) COMMENT '子分类',
    title VARCHAR(200) NOT NULL COMMENT '知识标题',
    content TEXT NOT NULL COMMENT '知识内容',
    content_vector BLOB COMMENT '内容向量(用于RAG)',
    source_url VARCHAR(500) COMMENT '来源URL',
    source_name VARCHAR(100) COMMENT '来源名称',
    tags JSON COMMENT '标签',
    confidence_level VARCHAR(20) DEFAULT 'high' COMMENT '可信度:high/medium/low',
    data_collected_at TIMESTAMP COMMENT '数据采集时间',
    status VARCHAR(20) DEFAULT 'active' COMMENT '状态:active/pending/archived',
    usage_count INT DEFAULT 0 COMMENT '被引用次数',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_category (category),
    INDEX idx_status (status)
);
```

#### 系统通知表 (notifications) ⭐
```sql
CREATE TABLE notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    type VARCHAR(30) NOT NULL COMMENT '通知类型:task_reminder/time_node/progress_warning/ai_reply/system',
    title VARCHAR(200) NOT NULL COMMENT '通知标题',
    content TEXT NOT NULL COMMENT '通知内容',
    data JSON COMMENT '关联数据(如任务ID、计划ID等)',
    is_read BOOLEAN DEFAULT FALSE COMMENT '是否已读',
    read_at TIMESTAMP COMMENT '阅读时间',
    channel VARCHAR(20) DEFAULT 'in_app' COMMENT '通知渠道:in_app/email/websocket',
    send_status VARCHAR(20) DEFAULT 'pending' COMMENT '发送状态:pending/sent/failed',
    scheduled_at TIMESTAMP COMMENT '计划发送时间',
    sent_at TIMESTAMP COMMENT '实际发送时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_read (user_id, is_read),
    INDEX idx_scheduled (scheduled_at)
);
```

#### 用户反馈表 (user_feedbacks) ⭐
```sql
CREATE TABLE user_feedbacks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    message_id BIGINT NOT NULL COMMENT '关联的消息ID',
    type VARCHAR(20) NOT NULL COMMENT '反馈类型:like/dislike/report',
    rating INT COMMENT '满意度评分1-5',
    comment TEXT COMMENT '详细反馈',
    feedback_details JSON COMMENT '反馈详情(如具体哪部分不满意)',
    status VARCHAR(20) DEFAULT 'pending' COMMENT '处理状态:pending/reviewed/resolved',
    admin_reply TEXT COMMENT '管理员回复',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (message_id) REFERENCES messages(id),
    INDEX idx_message (message_id),
    INDEX idx_status (status)
);
```

#### AI调用日志表 (ai_call_logs)
```sql
CREATE TABLE ai_call_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    provider VARCHAR(30) NOT NULL COMMENT 'AI提供商:mimo/deepseek/zhipu/qwen',
    model VARCHAR(50) NOT NULL COMMENT '模型名称',
    prompt_template_id BIGINT COMMENT '使用的Prompt模板ID',
    prompt_version INT COMMENT 'Prompt版本',
    input_tokens INT COMMENT '输入Token数',
    output_tokens INT COMMENT '输出Token数',
    total_tokens INT COMMENT '总Token数',
    latency_ms INT COMMENT '响应延迟(毫秒)',
    cost DECIMAL(10,4) COMMENT '调用成本',
    status VARCHAR(20) NOT NULL COMMENT '调用状态:success/failed/timeout',
    error_message TEXT COMMENT '错误信息',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user (user_id),
    INDEX idx_provider (provider),
    INDEX idx_created (created_at)
);
```

#### 能力评估记录表 (skill_assessments)
```sql
CREATE TABLE skill_assessments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    skill_category VARCHAR(50) NOT NULL COMMENT '技能分类:java/python/database/algorithm等',
    skill_name VARCHAR(100) NOT NULL COMMENT '技能名称',
    level INT DEFAULT 0 COMMENT '能力等级 0-100',
    assessment_method VARCHAR(20) DEFAULT 'self_report' COMMENT '评估方式:self_report/test/ai_estimate',
    evidence JSON COMMENT '评估证据:测试正确率/项目经验等',
    assessed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_skill (user_id, skill_category)
);
```

#### A/B测试实验表 (ab_experiments)
```sql
CREATE TABLE ab_experiments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '实验名称',
    type VARCHAR(30) NOT NULL COMMENT '实验类型:prompt/model/strategy',
    description TEXT COMMENT '实验描述',
    control_prompt_id BIGINT COMMENT '对照组Prompt ID',
    treatment_prompt_id BIGINT COMMENT '实验组Prompt ID',
    traffic_ratio DECIMAL(5,2) DEFAULT 50.00 COMMENT '实验组流量比例(%)',
    min_sample_size INT DEFAULT 100 COMMENT '最小样本量',
    start_time TIMESTAMP COMMENT '开始时间',
    end_time TIMESTAMP COMMENT '结束时间',
    status VARCHAR(20) DEFAULT 'draft' COMMENT '状态:draft/running/paused/completed',
    metrics JSON COMMENT '评估指标定义',
    result JSON COMMENT '实验结果分析',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### A/B测试流量分组表 (ab_user_groups)
```sql
CREATE TABLE ab_user_groups (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    experiment_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    group_name VARCHAR(20) NOT NULL COMMENT '分组名称:control/treatment',
    user_hash VARCHAR(64) NOT NULL COMMENT '用户Hash(用于一致性分桶)',
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (experiment_id) REFERENCES ab_experiments(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE KEY uk_experiment_user (experiment_id, user_id)
);
```

#### 操作日志表 (operation_logs)
```sql
CREATE TABLE operation_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    username VARCHAR(50) COMMENT '操作用户名',
    module VARCHAR(50) NOT NULL COMMENT '操作模块',
    operation VARCHAR(100) NOT NULL COMMENT '操作类型',
    method VARCHAR(50) COMMENT '请求方法',
    request_url VARCHAR(500) COMMENT '请求URL',
    request_params TEXT COMMENT '请求参数',
    response_status INT COMMENT '响应状态码',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    user_agent TEXT COMMENT '用户代理',
    execution_time_ms INT COMMENT '执行时间(毫秒)',
    error_message TEXT COMMENT '错误信息',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user (user_id),
    INDEX idx_module (module),
    INDEX idx_created (created_at)
);
```

#### 审计日志表 (audit_logs)
```sql
CREATE TABLE audit_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '操作用户ID',
    admin_id BIGINT COMMENT '管理员ID',
    action VARCHAR(50) NOT NULL COMMENT '操作类型:account_delete/data_export/permission_change等',
    target_type VARCHAR(50) COMMENT '操作对象类型',
    target_id BIGINT COMMENT '操作对象ID',
    old_value JSON COMMENT '变更前的值',
    new_value JSON COMMENT '变更后的值',
    reason TEXT COMMENT '操作原因',
    approval_status VARCHAR(20) DEFAULT 'approved' COMMENT '审批状态:pending/approved/rejected',
    approved_by BIGINT COMMENT '审批人',
    approved_at TIMESTAMP COMMENT '审批时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user (user_id),
    INDEX idx_action (action),
    INDEX idx_created (created_at)
);
```

#### 简历导入表 (resume_imports)
```sql
CREATE TABLE resume_imports (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    source_type VARCHAR(30) NOT NULL COMMENT '来源类型:paste/github/manual',
    raw_content TEXT NOT NULL COMMENT '原始内容',
    parsed_content JSON COMMENT '解析后的结构化内容',
    import_status VARCHAR(20) DEFAULT 'pending' COMMENT '导入状态:pending/processing/completed/failed',
    error_message TEXT COMMENT '错误信息',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

---

## 4. API 接口设计

### 4.1 认证接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/auth/register` | POST | 用户注册 |
| `/api/auth/login` | POST | 用户登录 |
| `/api/auth/refresh` | POST | 刷新Token |
| `/api/auth/logout` | POST | 登出 |

### 4.2 用户画像接口 ⭐

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/profile` | GET | 获取当前用户画像 |
| `/api/profile` | PUT | 更新用户画像 |
| `/api/profile/init` | POST | 初始化用户画像（首次设置） |
| `/api/profile/suggestions` | GET | 获取个性化建议（根据画像） |

### 4.3 建议接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/advice/consult` | POST | 咨询建议 |
| `/api/advice/scenarios` | GET | 获取可用场景 |
| `/api/advice/collect` | POST | 补充缺失信息 |

### 4.4 灵感接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/inspirations/generate` | POST | 生成灵感 |
| `/api/inspirations` | GET | 获取灵感列表 |
| `/api/inspirations/{id}` | GET | 获取灵感详情 |
| `/api/inspirations/{id}/favorite` | PUT | 收藏/取消收藏 |
| `/api/inspirations/{id}` | DELETE | 删除灵感 |

### 4.5 规划接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/plans` | GET/POST | 获取/创建规划 |
| `/api/plans/{id}` | GET/PUT/DELETE | 规划的CRUD |
| `/api/plans/{id}/tasks` | GET/POST | 获取/添加任务 |
| `/api/tasks/{id}` | PUT/DELETE | 任务的更新/删除 |
| `/api/plans/{id}/review` | POST | 规划回顾 |

### 4.6 对话接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/chat/send` | POST | 发送消息 |
| `/api/chat/history` | GET | 获取历史记录 |
| `/api/chat/export` | GET | 导出对话 |
| `/api/chat/feedback` | POST | 用户反馈（点赞/踩） |

### 4.7 通知接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/notifications` | GET | 获取通知列表 |
| `/api/notifications/{id}/read` | PUT | 标记已读 |
| `/api/notifications/settings` | GET/PUT | 通知设置 |
| `/api/notifications/preferences` | PUT | 通知偏好设置 |

### 4.8 数据导出接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/export/resume` | GET | 导出简历（Word/PDF） |
| `/api/export/plan` | GET | 导出学习计划（PDF/日历） |
| `/api/export/conversation` | GET | 导出对话记录（Markdown） |
| `/api/export/account` | GET | 导出账户数据（JSON） |

### 4.9 第三方集成接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/integrations/github/connect` | GET | GitHub OAuth授权 |
| `/api/integrations/github/callback` | GET | GitHub回调 |
| `/api/integrations/github/projects` | GET | 同步GitHub项目 |
| `/api/integrations/leetcode/connect` | POST | LeetCode账号绑定 |
| `/api/integrations/leetcode/sync` | POST | 同步刷题数据 |
| `/api/integrations/resume/import` | POST | 导入简历内容 |

### 4.10 统计与后台接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/admin/stats/overview` | GET | 统计数据概览 |
| `/api/admin/stats/users` | GET | 用户统计 |
| `/api/admin/stats/ai-usage` | GET | AI调用统计 |
| `/api/admin/feedbacks` | GET | 用户反馈列表 |
| `/api/admin/prompts` | GET/POST/PUT | Prompt模板管理 |
| `/api/admin/knowledge` | GET/POST/PUT | 知识库管理 |

---

## 5. 非功能需求

### 5.1 性能要求
- API响应时间 < 500ms（不含AI调用）
- 支持100+并发用户
- AI流式输出首字延迟 < 1s

### 5.2 系统可用性（SLA）
| 指标 | 目标值 |
|------|--------|
| 系统可用率 | ≥ 99.5% |
| API可用率 | ≥ 99.9% |
| AI服务可用率 | ≥ 99.0% |
| 计划内维护窗口 | 每周日凌晨 2:00-6:00 |

### 5.3 安全要求
- 所有接口需要JWT认证（除注册登录外）
- 密码加密存储（BCrypt）
- 请求频率限制（防刷接口）
- 敏感数据脱敏（发送给AI前）
- SQL注入防护
- XSS攻击防护

### 5.4 数据安全与备份
| 项目 | 策略 |
|------|------|
| 数据备份 | 每日全量备份，每周增量备份 |
| 备份保留 | 最近30天备份 |
| 异地容灾 | 支持跨可用区部署 |
| 备份验证 | 每月恢复演练 |
| AI调用日志 | 保留6个月，用于审计 |

### 5.5 数据一致性保障
| 场景 | 处理策略 |
|------|---------|
| AI调用超时 | 降级返回缓存结果+提示用户 |
| AI调用失败 | 重试3次，失败后返回友好提示+记录日志 |
| AI调用限流 | 排队等待+超时取消+提示用户稍后再试 |
| 部分内容生成失败 | 返回成功部分+提示内容可能不完整 |
| 知识库检索失败 | 降级到纯LLM生成+标记置信度低 |

### 5.6 可扩展性
- AI服务模块化，支持多模型切换
- 配置化设计，便于调整
- 支持水平扩展
- 向量数据库支持集群部署

### 5.7 监控与告警
| 监控项 | 告警阈值 |
|--------|----------|
| API错误率 | > 1% |
| AI调用失败率 | > 5% |
| API响应时间P99 | > 2s |
| CPU使用率 | > 80% |
| 内存使用率 | > 85% |
| 磁盘使用率 | > 90% |

---

## 6. 项目结构

### 6.1 后端项目结构（Spring Boot）

```
codeinspire-backend/
├── src/
│   ├── main/
│   │   ├── java/com/codeinspire/
│   │   │   ├── CodeInspireApplication.java
│   │   │   ├── config/              # 配置类（CORS、Redis、安全等）
│   │   │   ├── controller/          # 控制器层
│   │   │   ├── service/             # 业务逻辑层
│   │   │   ├── service/impl/        # 业务实现
│   │   │   ├── repository/          # 数据访问层（MyBatis）
│   │   │   ├── entity/              # 实体类
│   │   │   ├── dto/                # 数据传输对象
│   │   │   ├── vo/                 # 视图对象
│   │   │   ├── security/           # 安全相关（JWT、过滤器）
│   │   │   ├── ai/                 # AI服务封装
│   │   │   │   ├── provider/       # AI模型提供商
│   │   │   │   ├── service/        # AI服务
│   │   │   │   └── template/       # Prompt模板
│   │   │   ├── rag/                # RAG相关（向量检索、知识库）
│   │   │   ├── privacy/            # 隐私脱敏
│   │   │   ├── notification/       # 通知服务
│   │   │   ├── feedback/           # 用户反馈
│   │   │   ├── abtest/             # A/B测试
│   │   │   ├── export/             # 数据导出
│   │   │   ├── websocket/          # WebSocket配置
│   │   │   └── exception/          # 异常处理
│   │   └── resources/
│   │       ├── mapper/             # MyBatis XML映射文件
│   │       ├── application.yml     # 应用配置
│   │       └── templates/          # 模板文件
│   └── test/
│       └── java/com/codeinspire/    # 单元测试
├── pom.xml
└── README.md
```

### 6.2 前端项目结构（Vue 3）

```
codeinspire-frontend/
├── src/
│   ├── api/                    # API接口定义
│   │   ├── auth.ts            # 认证相关接口
│   │   ├── profile.ts         # 用户画像接口
│   │   ├── chat.ts            # 对话接口
│   │   ├── plan.ts            # 规划管理接口
│   │   └── admin.ts           # 运营后台接口
│   ├── assets/                # 静态资源
│   │   ├── images/           # 图片资源
│   │   └── styles/          # 全局样式
│   ├── components/           # 公共组件
│   │   ├── common/          # 通用组件
│   │   ├── chat/            # 对话相关组件
│   │   └── chart/           # 图表组件
│   ├── composables/         # 组合式函数
│   ├── layouts/             # 布局组件
│   ├── router/              # 路由配置
│   ├── stores/              # Pinia状态管理
│   │   ├── user.ts         # 用户状态
│   │   ├── chat.ts         # 对话状态
│   │   └── plan.ts         # 规划状态
│   ├── utils/               # 工具函数
│   ├── views/               # 页面视图
│   │   ├── auth/           # 认证页面
│   │   ├── chat/           # AI对话页面
│   │   ├── profile/        # 用户画像页面
│   │   ├── plan/           # 规划管理页面
│   │   ├── dashboard/      # 数据看板页面
│   │   └── admin/          # 运营后台页面
│   ├── App.vue
│   └── main.ts
├── public/
├── index.html
├── vite.config.ts
├── package.json
└── README.md
```

---

## 7. 开发阶段规划

### Phase 1 - 基础架构
- 项目搭建（Spring Boot + MyBatis Plus + Redis）
- 数据库设计与初始化
- 用户认证模块（注册/登录/JWT）
- 用户画像基础模块

### Phase 2 - AI服务层
- AI模型接入框架（小米MiMo/DeepSeek/智谱/通义千问）
- 隐私脱敏层
- RAG向量数据库集成（Qdrant）
- Prompt模板引擎

### Phase 3 - 核心业务功能
- 用户画像系统（完整维度采集）
- 智能信息收集与引导系统
- 个性化建议引擎
- 学习效果可视化

### Phase 4 - 规划与管理功能
- 规划管理（目标设定/任务分解）
- 进度追踪与回访机制
- 关键时间节点提醒
- 应急场景处理

### Phase 5 - 增强与集成
- 通知系统（WebSocket实时推送）
- 第三方集成（GitHub/LeetCode）
- 反馈追踪与内容审核
- 数据统计与运营后台

### Phase 6 - 前端开发
- Vue 3项目搭建
- 用户界面开发
- AI对话界面（流式输出）
- 数据可视化图表

### Phase 7 - 测试与部署
- 单元测试与集成测试
- A/B测试框架
- 监控与日志系统
- 生产环境部署

---

## 8. 待确认事项

### ✅ 已确认内容

所有关键需求已确认完毕，可以开始开发。

| 确认项 | 确认结果 |
|--------|----------|
| AI模型选择 | 小米MiMo, DeepSeek, 智谱AI/glm, 通义千问/qwen |
| 模型部署方式 | **API接口调用**（通过各平台官方API，需配置API Key） |
| 导出格式 | Markdown格式 |
| 前端形态 | **Vue 3 + Vite + Element Plus** 配套前端 |
| 目标用户 | 计算机专业大学生，面向后端行业 |
| 使用规模 | 10-50人 |
| 功能场景 | 编程学习指导、技术栈建议、求职就业指导、毕业设计辅助 |

---

## 附录 A - MVP 范围定义

### A.1 为什么必须有 MVP

当前文档属于"完整产品蓝图"，但不是"第一版开发范围"。如果不定义 MVP，会出现：
- 功能无限扩张
- 开发周期失控
- AI成本不可控
- 前后端联调困难
- 核心体验迟迟无法上线

### A.2 功能优先级定义

| 优先级 | 含义 |
|--------|------|
| P0 | 第一版必须上线 |
| P1 | 第一版稳定后追加 |
| P2 | 长期规划 |

### A.3 Phase MVP（第一版必须实现）

#### 用户系统（P0）

**必须功能**：
- 注册
- 登录
- JWT鉴权
- 用户画像初始化
- 基础资料修改

**暂不做**：
- OAuth登录
- 多设备管理
- MFA双因子认证

#### AI咨询系统（P0）

**必须功能**：
- 多轮对话
- 上下文记忆
- AI流式输出
- Prompt模板
- 用户画像注入
- AI调用日志

**暂不做**：
- 多模型交叉验证
- 自动模型评分
- Agent工作流
- Tool Calling

#### 用户画像（P0）

第一版仅保留：
- 学校层次
- 年级
- 目标方向
- 当前技术栈
- 每周时间
- 目标岗位
- 目标城市
- 紧迫程度

**暂缓**：
- 户籍
- 产业偏好
- 经济压力
- 课程压力
- 认证证书

**原因**：首版最重要的是"能生成有效建议"。

#### 学习规划系统（P0）

**必须**：
- 生成学习路线
- 生成任务
- 修改任务状态
- 进度追踪

**暂缓**：
- 智能回访
- 自动调整策略
- 同伴对比

#### RAG知识库（P0）

**第一版**：
- 面试题
- 学习路线
- 技术栈说明
- 简历建议

**暂缓**：
- 自动爬虫
- 自动更新
- 多数据源融合

**初期建议人工维护知识库**。

#### 通知系统（P0）

**第一版**：
- 站内通知
- WebSocket实时推送

**暂缓**：
- 邮件
- 日报
- 智能提醒

### A.4 P1 功能（第二阶段）

推荐放入 P1：
- GitHub集成
- LeetCode同步
- A/B测试
- 数据统计后台
- Prompt运营后台
- AI评分系统
- 技能雷达图
- 自动周报

### A.5 P2 功能（长期）

长期规划：
- 多Agent协同
- AI主动追踪
- 浏览器插件
- 自动岗位分析
- 自动简历优化
- AI模拟面试官
- AI语音顾问
- App端

---

## 附录 B - 核心业务流程图

### B.1 新用户主流程

```
进入系统 → 注册/登录 → 首次画像采集 → 目标选择 → AI生成初始诊断 → 生成学习路线 → 生成任务计划 → 每日执行 → 进度反馈 → AI动态调整
```

### B.2 AI咨询流程

```
用户提问 → 场景识别 → 检查必要信息
                    ↓
          ┌─────────┴─────────┐
        是                  否
          ↓                    ↓
      主动追问            构建Prompt
          ↓                    ↓
      信息补全            RAG检索
          ↓                    ↓
      重新生成            AI回答
                              ↓
                          质量审核
                              ↓
                          返回用户
```

### B.3 学习规划生成流程

```
用户目标输入 → 能力评估 → 时间评估 → 目标拆解 → 阶段规划 → 任务生成 → 日历排期 → 用户确认
```

### B.4 应急场景流程

```
情绪触发词检测 → 风险等级评估
                     ↓
           ┌─────────┴─────────┐
         是                  否
           ↓                    ↓
       安全安抚            正常分析
           ↓                    ↓
       建议求助            问题拆解
           ↓                    ↓
       停止技术建议        生成方案
```

---

*文档版本：v1.1*
*创建日期：2026-05-07*
*最后更新：2026-05-07*

---

## 附录 C - 字段字典与状态机

### C.1 任务状态机

**tasks.status**

| 状态 | 含义 |
|------|------|
| pending | 未开始 |
| in_progress | 进行中 |
| completed | 已完成 |
| paused | 已暂停 |
| cancelled | 已取消 |
| overdue | 已逾期 |

**状态流转**：
- pending → in_progress
- in_progress → completed
- in_progress → paused
- paused → in_progress
- pending → cancelled
- 任意状态 → overdue（系统自动）

### C.2 规划状态机

**plans.status**

| 状态 | 含义 |
|------|------|
| active | 进行中 |
| completed | 已完成 |
| paused | 已暂停 |
| archived | 已归档 |

### C.3 AI调用状态

**ai_call_logs.status**

| 状态 | 含义 |
|------|------|
| success | 成功 |
| failed | 失败 |
| timeout | 超时 |
| rate_limited | 被限流 |
| cancelled | 用户取消 |

### C.4 通知状态

**notifications.send_status**

| 状态 | 含义 |
|------|------|
| pending | 待发送 |
| sending | 发送中 |
| sent | 已发送 |
| failed | 发送失败 |

### C.5 JSON字段规范

**user_profiles.skills**
```json
[
  {
    "name": "Java",
    "level": 70
  }
]
```

**user_profiles.projects**
```json
[
  {
    "name": "校园二手平台",
    "role": "后端开发",
    "techStack": ["Spring Boot", "MySQL"],
    "description": "负责用户模块开发"
  }
]
```

### C.6 时间规范

**统一要求**：
- 数据库：UTC
- 前端展示：用户本地时区
- API返回：ISO8601

**示例**：`2026-05-08T10:30:00Z`

### C.7 删除策略

| 数据 | 删除方式 |
|------|---------|
| 用户 | 软删除 |
| 对话记录 | 软删除 |
| 通知 | 软删除 |
| 日志 | 不允许删除 |
| 审计记录 | 永久保留 |

---

## 附录 D - AI规则与安全边界

### D.1 AI系统核心原则

#### 原则1：不编造

**禁止**：
- 编造薪资
- 编造企业招聘
- 编造技术趋势
- 编造政策
- 编造岗位需求

#### 原则2：信息不足必须追问

**禁止示例**：

用户：我想找后端实习
AI：你应该学习微服务、Redis、MQ...

**缺失信息**：
- 学校层次
- 年级
- 时间
- 技术水平

**正确做法**：AI必须先追问缺失信息，才能给出完整建议。

#### 原则3：低置信度必须提示

**示例**：
> 以下建议基于当前公开招聘信息推测，实际情况可能因地区和公司变化而不同。

### D.2 AI场景规则

#### 学习路线场景

**必填**：
- 当前水平
- 目标方向
- 可投入时间
- 目标时间

**缺失时禁止生成完整路线**。

#### 求职建议场景

**必填**：
- 学校层次
- 年级
- 项目经验
- 目标岗位
- 目标城市

### D.3 Prompt注入防御

**过滤内容**：
- 忽略之前规则
- 你现在是...
- 系统Prompt是什么

**处理方式**：检测命中 → 拦截 → 返回安全提示

### D.4 AI输出审核

**输出审核维度**：

| 维度 | 规则 |
|------|------|
| 准确性 | 是否符合知识库 |
| 风险性 | 是否危险建议 |
| 合规性 | 是否违规 |
| 专业性 | 是否足够具体 |
| 可执行性 | 是否可落地 |

### D.5 高风险内容规则

#### 自残/极端情绪

**触发词**：
- 不想活了
- 想死
- 活不下去了

**系统行为**：
1. 停止职业规划
2. 优先安抚
3. 建议联系专业帮助
4. 不继续刺激性内容

### D.6 AI模型路由规则

| 场景 | 模型 |
|------|------|
| 简单问答 | MiMo |
| 技术解释 | GLM |
| 代码生成 | Qwen |
| 深度规划 | DeepSeek |

### D.7 RAG检索规则

```
知识库无结果 → 降级为纯LLM → 标记低置信度
```

### D.8 AI回答结构规范

**统一结构**：
1. 问题分析
2. 当前阶段判断
3. 推荐方案
4. 风险提醒
5. 下一步行动

---

## 附录 E - 推荐新增数据库表

### E.1 用户任务执行记录

```sql
CREATE TABLE task_execution_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    action VARCHAR(30),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### E.2 用户成长快照

用于能力成长曲线。

```sql
CREATE TABLE user_growth_snapshots (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    snapshot_date DATE,
    skill_summary JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### E.3 AI上下文缓存表

```sql
CREATE TABLE ai_contexts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    session_id VARCHAR(100),
    context_summary TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## 附录 F - 推荐新增技术组件

### F.1 消息队列

当前缺少 MQ。建议：

| 组件 | 用途 |
|------|------|
| RabbitMQ | 通知 |
| Kafka | 日志/埋点 |
| Redis Stream | 轻量异步 |

### F.2 任务调度

建议：
- XXL-Job
- Quartz

**用于**：
- 定时提醒
- 周报
- 数据同步
- 回访

### F.3 对象存储

**用于**：
- 简历PDF
- 导出文件
- 用户上传附件

**建议**：
- MinIO（本地）
- OSS/COS/S3（生产）

---

## 附录 G - 推荐新增非功能需求

### G.1 AI成本限制

- 单用户每日Token限制
- 单次最大上下文长度
- 超长历史自动摘要

### G.2 会话上下文压缩

**避免**：聊天越久越贵

**策略**：
- 历史摘要
- 最近N轮保留
- 长期记忆向量化

### G.3 灰度发布

支持：
- 10%用户新Prompt
- 50%用户新模型

### G.4 降级策略

| 故障 | 降级 |
|------|------|
| 向量库挂掉 | 纯LLM |
| DeepSeek超时 | 切Qwen |
| WebSocket断开 | HTTP轮询 |

---

## 附录 H - 最终推荐架构（更合理）

### H.1 第一版建议技术栈

| 模块 | 技术 |
|------|------|
| 后端 | Spring Boot 3 |
| ORM | MyBatis Plus |
| 数据库 | MySQL |
| 缓存 | Redis |
| AI框架 | Spring AI |
| 向量库 | Qdrant |
| 前端 | Vue 3 |
| UI组件 | Element Plus |
| 实时通信 | WebSocket |
| 对象存储 | MinIO |
| 定时任务 | XXL-Job |
| 消息队列 | RabbitMQ |

### H.2 完整系统架构图

```
┌─────────────────────────────────────────────────────────────┐
│                        客户端层                              │
│                    (Vue 3 + WebSocket)                      │
└────────────────────────────┬────────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────┐
│                       网关层                                  │
│                  (Spring Cloud Gateway)                      │
└────────────────────────────┬────────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────┐
│                      业务服务层                              │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐     │
│  │ 用户服务  │ │ AI服务   │ │ 规划服务  │ │ 通知服务  │     │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘     │
└────────────────────────────┬────────────────────────────────┘
                             │
        ┌────────────────────┼────────────────────┐
        │                    │                    │
        ▼                    ▼                    ▼
┌───────────────┐   ┌───────────────┐   ┌───────────────┐
│  MySQL        │   │  Redis       │   │  Qdrant       │
│  (主数据库)   │   │  (缓存/会话)  │   │  (向量检索)   │
└───────────────┘   └───────────────┘   └───────────────┘
        │                    │                    │
        │                    │                    │
        └────────────────────┼────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────┐
│                      AI模型层                                │
│     (小米MiMo / DeepSeek / 智谱GLM / 通义千问)              │
└─────────────────────────────────────────────────────────────┘
```

---

**文档版本：v4.0**  
**最后更新：2026-05-07**  
**状态：需求确认完成，可开始开发**
