-- 诊断 wishlist 表状态
USE financialmanage;

-- 1. 查看当前所有数据
SELECT id, wid, wish, wdate, state, user_id FROM wishlist;

-- 2. 查看 wid 的十六进制（检查实际存储的字节）
SELECT id, HEX(wid) AS wid_hex, wish FROM wishlist;

-- 3. 检查表结构和字符集
SHOW CREATE TABLE wishlist;

-- 4. 查看数据库字符集
SHOW VARIABLES LIKE 'character_set_%';
