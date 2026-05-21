-- 修复 wishlist 表中已存储的乱码 wid
-- 乱码原因：javac 用 GBK 解释了 UTF-8 编码的"心愿单"三个字，存入了乱码字符
-- 修复方式：定位 wid 中日期开始的位置，重新拼接正确前缀
USE financialmanage;

-- 第一步：查看当前数据（确认乱码范围）
SELECT id, wid, wish FROM wishlist;

-- 第二步：修复。LOCATE('20', wid) 找到年份起始位置，截取日期部分再接上正确前缀
-- 心愿单 = 心愿单
UPDATE wishlist
SET wid = CONCAT('心愿单', SUBSTRING(wid, LOCATE('20', wid)))
WHERE wid NOT LIKE '心愿单%';

-- 第三步：验证修复结果
SELECT id, wid, wish FROM wishlist;
