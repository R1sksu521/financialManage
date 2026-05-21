-- ============================================
-- 迁移脚本：将 DATE 列改为 DATETIME 以支持时分秒显示
-- 数据库：financialmanage
-- 执行方式：在 MySQL 中 source 本文件 或 逐条执行
-- ============================================

USE financialmanage;

-- 1. 心愿单 - wdate 列
ALTER TABLE wishlist MODIFY wdate DATETIME;

-- 2. 备忘录 - recordTime 列
ALTER TABLE memorandum MODIFY recordTime DATETIME;

-- 3. 财务新闻 - recordTime 列
ALTER TABLE news MODIFY recordTime DATETIME;
