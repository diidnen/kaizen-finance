-- 创建合同表的SQL脚本

-- 检查并创建合同表（如果不存在）
CREATE TABLE IF NOT EXISTS `contracts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `description` text,
  `file_name` varchar(255) NOT NULL,
  `original_file_name` varchar(255) NOT NULL,
  `file_path` varchar(500) NOT NULL,
  `file_type` varchar(100),
  `file_size` bigint,
  `uploaded_by` varchar(255) NOT NULL,
  `upload_time` datetime NOT NULL,
  `is_active` boolean DEFAULT true,
  `category` varchar(100) DEFAULT 'other',
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category`),
  KEY `idx_uploaded_by` (`uploaded_by`),
  KEY `idx_is_active` (`is_active`),
  KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 插入一些示例合同数据（可选）
INSERT INTO `contracts` (`title`, `description`, `file_name`, `original_file_name`, `file_path`, `file_type`, `file_size`, `uploaded_by`, `upload_time`, `is_active`, `category`, `username`) VALUES
('Standard Service Agreement', 'Standard service agreement template for all clients', 'sample_service.pdf', 'service_agreement.pdf', 'contracts/sample_service.pdf', 'application/pdf', 1024000, 'chuhan', NOW(), true, 'service', 'demo_user'),
('Non-Disclosure Agreement', 'Confidentiality agreement for business partners', 'sample_nda.pdf', 'nda_template.pdf', 'contracts/sample_nda.pdf', 'application/pdf', 512000, 'chuhan', NOW(), true, 'nda', 'demo_user'),
('Employment Contract Template', 'Standard employment contract for new hires', 'sample_employment.pdf', 'employment_contract.pdf', 'contracts/sample_employment.pdf', 'application/pdf', 768000, 'chuhan', NOW(), true, 'employment', 'demo_user');

-- 查看创建的表结构
DESCRIBE `contracts`;

-- 查看插入的数据
SELECT * FROM `contracts`; 