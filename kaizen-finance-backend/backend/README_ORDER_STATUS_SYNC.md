# Order Status Synchronization

## 概述
当管理员在Manager页面修改订单状态后，这个状态变化会在所有地方同步显示，包括用户查看自己订单的页面。

## 工作原理

### 1. 数据流
```
Manager页面修改状态 → 后端API更新数据库 → 用户页面从数据库读取最新状态
```

### 2. 后端实现
- **ManagerController**: 提供 `PUT /api/manager/orders/{id}/status` 端点
- **Service**: 提供 `POST /api/ordersfind` 端点获取用户订单
- **数据库**: 所有状态变化都直接更新到数据库

### 3. 前端实现
- **Manager页面**: 管理员可以编辑和保存订单状态
- **User页面**: 用户查看订单时显示最新状态
- **状态标签**: 统一的颜色和文本显示

## 状态类型

| 状态 | 颜色 | 描述 |
|------|------|------|
| Pending | 橙色 (warning) | 待处理 |
| Approved | 绿色 (success) | 已批准 |
| Rejected | 红色 (danger) | 已拒绝 |
| Completed | 绿色 (success) | 已完成 |

## 测试方法

### 1. 管理员修改状态
1. 登录为chuhan用户
2. 访问Manager页面
3. 在Order Management区域点击状态列的编辑按钮
4. 选择新状态并保存

### 2. 用户查看状态
1. 登录为普通用户
2. 访问Order页面
3. 查看订单状态是否已更新

### 3. 数据库验证
```sql
-- 查看所有订单状态
SELECT id, username, serviceName, status FROM orders ORDER BY id DESC;

-- 查看特定状态
SELECT * FROM orders WHERE status = 'approved';
```

## 技术细节

### 后端API
- `PUT /api/manager/orders/{id}/status` - 更新订单状态
- `POST /api/ordersfind` - 获取用户订单（包含最新状态）

### 前端组件
- `manager.vue` - 管理员订单管理
- `order.vue` - 用户订单查看

### 数据库字段
- `orders.status` - 订单状态字段
- 默认值: "pending"

## 注意事项

1. **权限控制**: 只有chuhan用户可以修改订单状态
2. **状态验证**: 后端验证状态值的有效性
3. **实时同步**: 状态修改后立即在数据库中更新
4. **用户反馈**: 前端显示成功/失败消息

## 故障排除

### 状态不更新
1. 检查后端是否正常运行
2. 验证数据库连接
3. 查看浏览器控制台错误
4. 检查API响应

### 权限问题
1. 确认用户已登录
2. 验证token有效性
3. 检查用户角色

## 更新日志
- v1.0.0: 初始版本，支持订单状态修改和同步