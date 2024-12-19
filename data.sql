-- 创建数据库
CREATE DATABASE IF NOT EXISTS campus_card_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE campus_card_system;

-- 用户表
CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    avatar VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 挂失卡片表
CREATE TABLE lost_cards (
    id VARCHAR(36) PRIMARY KEY,
    card_id VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    reason TEXT,
    status VARCHAR(20) NOT NULL,
    time DATETIME NOT NULL,
    lost_time DATETIME,
    location VARCHAR(255),
    user_id VARCHAR(36),
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 拾获卡片表
CREATE TABLE found_cards (
    id VARCHAR(36) PRIMARY KEY,
    card_id VARCHAR(50) NOT NULL,
    location VARCHAR(255) NOT NULL,
    time DATETIME NOT NULL,
    contact VARCHAR(20) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL,
    create_time DATETIME NOT NULL,
    user_id VARCHAR(36),
    finder VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 公告表
CREATE TABLE notices (
    id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    time DATETIME NOT NULL,
    author VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 通知表
CREATE TABLE notifications (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    type VARCHAR(20) NOT NULL,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    related_id VARCHAR(50),
    is_read BOOLEAN DEFAULT FALSE,
    create_time DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 插入初始管理员账号
INSERT INTO users (id, username, password, role, avatar) VALUES
('1', 'admin', '123456', 'admin', 'https://i.pravatar.cc/150?img=1');

-- 插入示例公告
INSERT INTO notices (id, title, content, time, author) VALUES
('1', '系统上线通知', '校园卡管理系统正式上线，欢迎使用！', '2024-03-20 10:00:00', '系统管理员'),
('2', '使用指南', '如果您的校园卡不慎丢失，请及时在系统中进行挂失。如果捡到他人的校园卡，也请及时在系统中登记，方便失主认领。', '2024-03-20 10:30:00', '学生处');

-- 插入示例挂失记录
INSERT INTO lost_cards (id, card_id, name, phone, reason, status, time, lost_time, location, user_id) VALUES
('1', '2024001', '张三', '13800138000', '在图书馆学习时遗失', 'pending', '2024-03-20 14:30:00', '2024-03-20 14:00:00', '图书馆', '1'),
('2', '2024002', '李四', '13800138001', '在食堂就餐时遗失', 'completed', '2024-03-20 13:15:00', '2024-03-20 13:00:00', '第一食堂', '1'),
('3', '2024003', '王五', '13800138002', '在教学楼上课时遗失', 'cancelled', '2024-03-20 09:30:00', '2024-03-20 09:00:00', '教学楼A栋', '1');

-- 插入示例拾获记录
INSERT INTO found_cards (id, card_id, location, time, contact, description, status, create_time, user_id, finder) VALUES
('1', '2024001', '图书馆三楼阅览室', '2024-03-20 15:30:00', '13900139000', '黑色卡套，卡内有学生证', 'waiting', '2024-03-20 15:30:00', '1', 'admin'),
('2', '2024002', '第一食堂二楼', '2024-03-20 14:20:00', '13900139001', '蓝色卡套，卡内有饭卡', 'claimed', '2024-03-20 14:20:00', '1', 'admin'),
('3', '2024004', '体育馆', '2024-03-20 16:00:00', '13900139002', '红色卡套，卡内有现金', 'waiting', '2024-03-20 16:00:00', '1', 'admin');

INSERT INTO activities (title, content, type, create_time, status)
VALUES ('校园卡挂失通知', '学生张三的校园卡已挂失', 'lost', NOW(), 'pending'),
       ('校园卡招领信息', '在图书馆发现一张校园卡', 'found', NOW(), 'waiting');

-- 创建索引
CREATE INDEX idx_lost_cards_user_id ON lost_cards(user_id);
CREATE INDEX idx_lost_cards_status ON lost_cards(status);
CREATE INDEX idx_found_cards_user_id ON found_cards(user_id);
CREATE INDEX idx_found_cards_status ON found_cards(status);
CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_is_read ON notifications(is_read);

CREATE TABLE activities (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   title VARCHAR(255) NOT NULL,
   content TEXT,
   type VARCHAR(50) NOT NULL,
   create_time DATETIME NOT NULL,
   status VARCHAR(50) NOT NULL)
 ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

select * from users;