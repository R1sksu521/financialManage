-- 初始化数据库表
-- 用户表
CREATE TABLE IF NOT EXISTS user (
    uid INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 收支类型表
CREATE TABLE IF NOT EXISTS shouzhi_category (
    szcid INT AUTO_INCREMENT PRIMARY KEY,
    parent_category VARCHAR(10) NOT NULL,
    son_category VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 收支记录表
CREATE TABLE IF NOT EXISTS shouzhi_record (
    szrid INT AUTO_INCREMENT PRIMARY KEY,
    szr_num INT NOT NULL,
    szr_date VARCHAR(20),
    szr_comment VARCHAR(200),
    shouzhi_category_id INT,
    user_id INT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 预算表
CREATE TABLE IF NOT EXISTS budget (
    bid INT AUTO_INCREMENT PRIMARY KEY,
    bnum INT,
    bdate VARCHAR(20),
    bcomment VARCHAR(200),
    user_id INT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 心愿单表
CREATE TABLE IF NOT EXISTS wishlist (
    wid INT AUTO_INCREMENT PRIMARY KEY,
    wish VARCHAR(200),
    wdate VARCHAR(20),
    state VARCHAR(10) DEFAULT '未实现',
    user_id INT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 备忘录表
CREATE TABLE IF NOT EXISTS memorandum (
    mid INT AUTO_INCREMENT PRIMARY KEY,
    mtitle VARCHAR(100),
    mcontent TEXT,
    mdate VARCHAR(20),
    user_id INT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 新闻表
CREATE TABLE IF NOT EXISTS news (
    nid INT AUTO_INCREMENT PRIMARY KEY,
    nTitle VARCHAR(200),
    nContent TEXT,
    ndate VARCHAR(20),
    keyword VARCHAR(50),
    visitCount INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 管理员表
CREATE TABLE IF NOT EXISTS admin (
    aid INT AUTO_INCREMENT PRIMARY KEY,
    adminname VARCHAR(50),
    password VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 种子数据：收支类型
INSERT INTO shouzhi_category (parent_category, son_category) VALUES
('收入','工资'),('收入','兼职'),('收入','奖金'),('收入','理财'),('收入','其他'),
('支出','餐饮'),('支出','购物'),('支出','交通'),('支出','娱乐'),('支出','学习'),
('支出','医疗'),('支出','其他');

-- 种子数据：默认管理员
INSERT INTO admin (adminname, password) VALUES ('admin','admin');
