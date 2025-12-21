# Contract Management System

## 概述
合同管理系统允许登录用户下载自己的合同文件，管理员可以上传、管理和删除合同。每个用户只能看到和下载分配给自己的合同。

## 功能特性

### 用户功能
- 查看自己的合同
- 下载自己的合同文件
- 搜索和筛选自己的合同
- 按分类浏览自己的合同

### 管理员功能
- 为特定用户上传合同文件
- 管理现有合同（激活/停用）
- 删除合同
- 查看所有合同（包括停用的）
- 指定合同的目标用户

## 技术实现

### 后端
- **Contract.java**: 合同实体类
- **ContractRepository.java**: 数据访问层
- **ContractController.java**: REST API控制器

### 前端
- **contract.vue**: 合同管理页面
- 支持文件上传和下载
- 响应式设计

## API端点

### 需要认证的端点
- `GET /api/contracts/user` - 获取用户自己的合同
- `GET /api/contracts/admin` - 获取所有合同（仅管理员）
- `POST /api/contracts/upload` - 上传新合同（仅管理员）
- `GET /api/contracts/download/{id}` - 下载合同文件
- `PUT /api/contracts/{id}/status` - 更新合同状态（仅管理员）
- `DELETE /api/contracts/{id}` - 删除合同（仅管理员）

## 文件支持
- 支持的文件格式：PDF, DOC, DOCX
- 最大文件大小：10MB
- 文件存储在 `contracts/` 目录

## 合同分类
- Service Agreement (服务协议)
- Non-Disclosure Agreement (保密协议)
- Employment Contract (雇佣合同)
- Consulting Agreement (咨询协议)
- Other (其他)

## 安装和配置

### 1. 创建数据库表
```sql
-- 运行 create_contracts_table.sql 脚本
```

### 2. 创建上传目录
```bash
# 在项目根目录创建contracts文件夹
mkdir contracts
```

### 3. 配置权限
确保只有 `chuhan` 用户有管理员权限

## 使用方法

### 用户
1. 访问 `/contracts` 页面
2. 浏览自己的合同
3. 点击下载按钮获取合同文件

### 管理员
1. 以 `chuhan` 用户身份登录
2. 访问 `/contracts` 页面
3. 使用上传表单为特定用户添加新合同
4. 管理现有合同状态

## 安全特性
- 登录验证：只有登录用户才能访问合同页面
- 用户隔离：每个用户只能看到自己的合同
- 文件类型验证
- 文件大小限制
- 管理员权限验证
- 安全的文件存储
- 下载权限控制

## 故障排除

### 常见问题
1. **文件上传失败**: 检查文件大小和格式
2. **下载失败**: 确认文件存在且合同状态为活跃
3. **权限错误**: 确认用户身份和权限

### 日志
查看Spring Boot应用日志以获取详细错误信息

## 更新日志
- v1.0.0: 初始版本，支持基本的合同上传和下载功能 