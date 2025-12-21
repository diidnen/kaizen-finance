-- 创建评价表的SQL脚本

-- 检查并创建评价表（如果不存在）
CREATE TABLE IF NOT EXISTS `testimonials` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(255) NOT NULL,
  `company_name` varchar(255) DEFAULT '',
  `testimonial` text NOT NULL,
  `avatar_url` varchar(500) DEFAULT '',
  `created_by` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL,
  `is_active` boolean DEFAULT true,
  `display_order` int DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_is_active` (`is_active`),
  KEY `idx_display_order` (`display_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 插入一些示例评价数据
INSERT INTO `testimonials` (`customer_name`, `company_name`, `testimonial`, `avatar_url`, `created_by`, `created_at`, `is_active`, `display_order`) VALUES
('Koen van den Heuvel', 'Fitune', 'We can\'t recommend Kaizen Finance highly enough! They did an incredible job clearing our accounting backlog. The team is not only highly skilled and efficient but also extremely approachable and always happy to jump on a call whenever we needed them. If you\'re looking for accountants who are reliable, responsive, and truly care about your success, Kaizen Finance is the way to go.', 'https://via.placeholder.com/60x60/4A90E2/FFFFFF?text=K', 'chuhan', NOW(), true, 1),
('Chris Emmerson', 'Caption Pro', 'Kaizen Finance are the perfect fit for us as we grow, not only because of their expertise, but mainly because of how available and supportive they are. When searching for a new accountant we needed someone affordable, but we also needed someone we could chat to about the ins and outs of our business whenever we had any questions. Kaizen Finance ticks all of those boxes.', 'https://via.placeholder.com/60x60/8E44AD/FFFFFF?text=C', 'chuhan', NOW(), true, 2),
('Staffa', 'Co-Four', 'Kaizen Finance has been a great company to work with. I was working with their platform as I wanted continuity and to learn more about their services. Our accountant Kate has been both the teacher and the guide. She sends a clear task list of what to do. The difference they make is remarkable.', 'https://via.placeholder.com/60x60/2ECC71/FFFFFF?text=S', 'chuhan', NOW(), true, 3);

-- 查看创建的表结构
DESCRIBE `testimonials`;

-- 查看插入的数据
SELECT * FROM `testimonials` ORDER BY `display_order` ASC;