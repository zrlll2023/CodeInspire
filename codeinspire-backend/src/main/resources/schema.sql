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
