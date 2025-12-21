# Create User Feature

## 概述
管理员可以创建新的用户账号，包括用户名、密码、邮箱和全名。

## 功能特性

### 管理员功能
- 创建新用户账号
- 设置用户名、密码、邮箱和全名
- 验证用户名和邮箱唯一性
- 实时查看创建的用户

### 用户验证
- 用户名：3-20个字符，必填
- 密码：6-20个字符，必填
- 邮箱：有效邮箱格式，必填
- 全名：必填

## 技术实现

### 后端API
- **POST /api/manager/users** - 创建新用户
- 权限验证：只有chuhan用户可以创建用户
- 数据验证：检查必填字段和格式
- 唯一性检查：用户名和邮箱不能重复

### 前端界面
- 创建用户按钮
- 用户创建对话框
- 表单验证
- 实时反馈

## API端点

### 创建用户
```
POST /api/manager/users
Authorization: Bearer <token>
Content-Type: application/json

{
  "username": "newuser",
  "password": "password123",
  "email": "user@example.com",
  "fullname": "New User"
}
```

### 响应格式
```json
{
  "code": 200,
  "message": "User created successfully",
  "user": {
    "id": 1,
    "username": "newuser",
    "email": "user@example.com",
    "fullname": "New User",
    "createdAt": "2024-01-01T00:00:00"
  }
}
```

## 错误处理

### 常见错误
- **400 Bad Request**: 必填字段缺失或格式错误
- **401 Unauthorized**: 无效token
- **403 Forbidden**: 非管理员用户
- **409 Conflict**: 用户名或邮箱已存在
- **500 Internal Server Error**: 服务器错误

### 错误响应示例
```json
{
  "code": 409,
  "message": "Username already exists"
}
```

## 使用方法

### 管理员操作
1. 登录为chuhan用户
2. 访问Manager页面
3. 点击"Create User"按钮
4. 填写用户信息
5. 点击"Create User"确认

### 表单验证
- 用户名：3-20个字符
- 密码：6-20个字符，显示/隐藏切换
- 邮箱：有效邮箱格式
- 全名：必填

## 安全特性
- 管理员权限验证
- 用户名和邮箱唯一性检查
- 密码安全存储
- 输入验证和清理

## 测试方法

### 手动测试
1. 使用提供的测试脚本
2. 测试正常创建流程
3. 测试重复用户名/邮箱
4. 测试无效输入

### 测试脚本
```bash
# Linux/Mac
./test_create_user.sh

# Windows
test_create_user.bat
```

## 数据库影响
- 在`user`表中创建新记录
- 自动设置创建时间
- 初始token为null（需要登录获取）

## 更新日志
- v1.0.0: 初始版本，支持基本的用户创建功能