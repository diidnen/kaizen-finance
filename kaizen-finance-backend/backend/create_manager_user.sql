-- 创建manager用户chuhan的SQL脚本
-- 如果用户表不存在，先创建用户表

-- 检查并创建用户表（如果不存在）
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `token` varchar(255) DEFAULT NULL,
  `token_expiry` datetime DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 插入manager用户chuhan（如果不存在）
INSERT IGNORE INTO `user` (`username`, `password`, `email`, `fullname`, `created_at`) 
VALUES ('chuhan', 'chuhan123', 'chuhan@kaizenfinance.com', 'Chuhan Manager', NOW());

-- 或者使用ON DUPLICATE KEY UPDATE来更新现有用户
INSERT INTO `user` (`username`, `password`, `email`, `fullname`, `created_at`) 
VALUES ('chuhan', 'chuhan123', 'chuhan@kaizenfinance.com', 'Chuhan Manager', NOW())
ON DUPLICATE KEY UPDATE 
  `password` = VALUES(`password`),
  `email` = VALUES(`email`),
  `fullname` = VALUES(`fullname`);

-- 查看创建的用户
SELECT * FROM `user` WHERE `username` = 'chuhan'; 