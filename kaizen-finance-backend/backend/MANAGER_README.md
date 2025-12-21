# Manager功能使用说明

## 概述
Manager功能是一个只有username为"chuhan"的用户才能访问的管理界面，用于管理系统中所有其他用户的账号和密码。

## 功能特性

### 1. 用户管理
- 查看所有用户的账号信息
- 显示用户密码（可切换显示/隐藏）
- 用户状态监控（活跃/非活跃）
- 用户创建时间统计

### 2. 权限控制
- 只有username为"chuhan"的用户才能访问
- 不能删除或重置自己的账号密码
- 完整的token验证和权限检查

### 3. 操作功能
- 删除用户（除manager账户外）
- 重置用户密码（生成8位随机密码）
- 搜索和筛选用户
- 分页显示

## 安装和配置

### 1. 数据库设置
运行以下SQL脚本来创建manager用户：

```sql
-- 在MySQL中执行
source create_manager_user.sql;
```

或者手动执行：

```sql
INSERT INTO `user` (`username`, `password`, `email`, `fullname`, `created_at`) 
VALUES ('chuhan', 'chuhan123', 'chuhan@kaizenfinance.com', 'Chuhan Manager', NOW());
```

### 2. 后端配置
确保以下文件已正确配置：
- `ManagerController.java` - 处理manager API请求
- `CorsConfig.java` - CORS配置
- `SecurityConfig.java` - 安全配置

### 3. 前端配置
确保以下文件已正确配置：
- `manager.vue` - manager页面组件
- `router/index.js` - 路由配置
- `App.vue` - 导航栏配置

## API接口

### 1. 获取所有用户
```
GET /api/manager/users
Authorization: Bearer {token}
```

### 2. 删除用户
```
DELETE /api/manager/users/{id}
Authorization: Bearer {token}
```

### 3. 重置用户密码
```
POST /api/manager/users/{id}/reset-password
Authorization: Bearer {token}
```

## 使用方法

### 1. 登录
使用以下凭据登录：
- Username: `chuhan`
- Password: `chuhan123`

### 2. 访问Manager页面
登录后，导航栏会显示"Manager"链接，点击进入管理界面。

### 3. 管理用户
- 查看用户列表和统计信息
- 搜索特定用户
- 删除不需要的用户账户
- 重置用户密码

## 安全特性

### 1. 权限验证
- 每次API请求都会验证token
- 验证用户是否为manager（chuhan）
- 防止未授权访问

### 2. 操作限制
- 不能删除manager账户
- 不能重置manager密码
- 所有操作都有确认提示

### 3. 日志记录
- 所有操作都有详细的错误日志
- 便于问题排查和审计

## 注意事项

### 1. 密码安全
- 重置密码后会生成8位随机密码
- 建议用户首次登录后立即修改密码
- 密码重置后用户需要重新登录

### 2. 数据备份
- 删除用户操作不可逆
- 建议在删除前备份重要数据
- 定期备份用户数据库

### 3. 性能考虑
- 用户列表支持分页显示
- 搜索功能优化大数据量查询
- 建议定期清理无效token

## 故障排除

### 1. 无法访问Manager页面
- 检查是否以chuhan用户登录
- 验证token是否有效
- 检查路由配置是否正确

### 2. API请求失败
- 检查Authorization header是否正确
- 验证后端服务是否正常运行
- 查看后端日志获取详细错误信息

### 3. 数据库连接问题
- 检查数据库连接配置
- 验证用户表是否存在
- 确认数据库权限设置

## 技术支持
如有问题，请检查：
1. 后端日志输出
2. 前端浏览器控制台
3. 数据库连接状态
4. 网络请求状态 