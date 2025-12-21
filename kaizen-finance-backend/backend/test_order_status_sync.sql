-- 测试订单状态同步的SQL脚本

-- 查看所有订单及其状态
SELECT 
    id,
    username,
    serviceName,
    price,
    status,
    orderDate,
    serviceTime
FROM orders 
ORDER BY id DESC;

-- 更新特定订单状态（示例）
-- UPDATE orders SET status = 'approved' WHERE id = 1;
-- UPDATE orders SET status = 'rejected' WHERE id = 2;
-- UPDATE orders SET status = 'completed' WHERE id = 3;

-- 查看特定用户的订单
-- SELECT * FROM orders WHERE username = 'test_user';

-- 验证状态更新
-- SELECT id, username, serviceName, status FROM orders WHERE status = 'approved';