CREATE DATABASE IF NOT EXISTS codeinspire DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE codeinspire;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 用户画像表
CREATE TABLE IF NOT EXISTS user_profiles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    school_level VARCHAR(30) COMMENT '985/211/一本/二本/独立学院/民办本科/专科',
    school_type VARCHAR(30) COMMENT '综合类/理工类/师范类/专科等',
    education_level VARCHAR(20) COMMENT '专科/本科/硕士/博士',
    major VARCHAR(100) COMMENT '所读专业',
    grade VARCHAR(20) COMMENT '大一/大二/大三/大四/研一/研二等',
    urgency_level VARCHAR(20) COMMENT '紧迫程度：紧急/一般/充裕',
    weekly_available_hours INT COMMENT '每周可用学习时间(小时)',
    coursework_pressure VARCHAR(20) COMMENT '课业压力：轻松/一般/繁重/考研准备中',
    target_city_level VARCHAR(30) COMMENT '目标城市级别：一线/新一线/二三线',
    hometown_consideration VARCHAR(50),
    industry_preference VARCHAR(50),
    payment_willingness VARCHAR(30),
    computer_config VARCHAR(30),
    self_learning_ability VARCHAR(20),
    economic_pressure VARCHAR(20),
    current_status VARCHAR(30),
    major_direction VARCHAR(50),
    target_position VARCHAR(50),
    target_company VARCHAR(50),
    expected_salary VARCHAR(30),
    skills TEXT COMMENT 'JSON格式存储技能列表',
    projects TEXT COMMENT 'JSON格式存储项目经验',
    certifications TEXT COMMENT 'JSON格式存储认证证书',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 对话记录表
CREATE TABLE IF NOT EXISTS conversations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    session_id VARCHAR(100) NOT NULL,
    type VARCHAR(30) NOT NULL COMMENT '对话类型:consult/learning/career/general',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_session (user_id, session_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 消息记录表
CREATE TABLE IF NOT EXISTS messages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    conversation_id BIGINT NOT NULL,
    role VARCHAR(20) NOT NULL COMMENT 'user/assistant/system',
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (conversation_id) REFERENCES conversations(id),
    INDEX idx_conversation (conversation_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- AI调用日志表
CREATE TABLE IF NOT EXISTS ai_call_logs (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Prompt模板表
CREATE TABLE IF NOT EXISTS prompts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '模板名称',
    scene VARCHAR(50) NOT NULL COMMENT '应用场景:career_advice/tech_learning/interview_prep等',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 初始化默认Prompt模板
INSERT INTO prompts (name, scene, content, version, status) VALUES
('通用咨询', 'general', '你是CodeInspire，一个专业的计算机专业学生AI个性化顾问。请根据用户背景信息提供专业、实用、可执行的建议。核心原则：1.不编造信息 2.信息不足时追问 3.低置信度时提示 4.提供具体建议', 1, 'active'),
('求职建议', 'career_advice', '你是CodeInspire求职顾问。根据{{学校层次}}、{{年级}}、{{目标岗位}}、{{目标城市}}等信息，为用户提供差异化求职策略。分析：1.当前阶段判断 2.技能差距分析 3.时间规划建议 4.风险提醒', 1, 'active'),
('技术学习', 'tech_learning', '你是CodeInspire技术导师。针对{{专业方向}}和用户技术水平，提供：1.概念解释（使用类比）2.代码示例 3.学习路径 4.实践建议。保持简洁易懂。', 1, 'active'),
('面试准备', 'interview_prep', '你是CodeInspire面试教练。针对{{目标岗位}}方向，提供：1.高频面试题 2.答题思路 3.模拟练习 4.提升建议。注重实战性。', 1, 'active')
ON DUPLICATE KEY UPDATE name=name;

-- 技能评估记录表
CREATE TABLE IF NOT EXISTS skill_assessments (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 用户成长快照表
CREATE TABLE IF NOT EXISTS user_growth_snapshots (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    snapshot_date DATE COMMENT '快照日期',
    skill_summary JSON COMMENT '技能汇总数据',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_date (user_id, snapshot_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 规划表
CREATE TABLE IF NOT EXISTS plans (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    type VARCHAR(50) DEFAULT 'learning' COMMENT '规划类型:learning/career/interview/project',
    status VARCHAR(20) DEFAULT 'active' COMMENT '状态:active/completed/paused/archived',
    start_date DATE,
    end_date DATE,
    target_goal TEXT COMMENT '目标描述',
    total_tasks INT DEFAULT 0 COMMENT '总任务数',
    completed_tasks INT DEFAULT 0 COMMENT '已完成任务数',
    priority INT DEFAULT 1 COMMENT '优先级 0-5',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_status (user_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 任务表
CREATE TABLE IF NOT EXISTS tasks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    plan_id BIGINT,
    user_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    status VARCHAR(20) DEFAULT 'pending' COMMENT '状态:pending/in_progress/completed/paused/cancelled/overdue',
    priority INT DEFAULT 1 COMMENT '优先级 0-5',
    due_date DATE,
    completed_at DATE,
    category VARCHAR(50) COMMENT '任务分类',
    estimated_hours VARCHAR(20) COMMENT '预估工时',
    sort_order INT DEFAULT 0 COMMENT '排序序号',
    parent_task_id BIGINT COMMENT '父任务ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (plan_id) REFERENCES plans(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_plan (plan_id),
    INDEX idx_user_status (user_id, status),
    INDEX idx_due_date (due_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 任务执行日志表
CREATE TABLE IF NOT EXISTS task_execution_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    action VARCHAR(30) NOT NULL COMMENT '操作类型:create/complete/update/delete/status_change',
    previous_status VARCHAR(20) COMMENT '变更前状态',
    new_status VARCHAR(20) COMMENT '变更后状态',
    notes TEXT COMMENT '操作备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES tasks(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_task (task_id),
    INDEX idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
