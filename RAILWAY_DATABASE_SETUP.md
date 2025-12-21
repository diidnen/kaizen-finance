# Railway MySQL 数据库配置详细指南

## ✅ Railway 完全支持 MySQL 数据库

Railway 提供托管的 MySQL 数据库服务，非常适合 Spring Boot 应用。

## 📋 步骤 1: 添加 MySQL 数据库到 Railway 项目

1. **在 Railway 项目页面**
   - 点击 "+ New" 按钮
   - 选择 "Database"
   - 选择 "Add MySQL"

2. **Railway 会自动**：
   - 创建 MySQL 8.0 实例
   - 生成数据库名称
   - 创建用户和密码
   - 提供连接信息

3. **等待数据库就绪**
   - 通常需要 30-60 秒
   - 状态会显示为 "Active"

## 🔗 步骤 2: 连接数据库到后端服务

### 方法 A: 使用 Railway 模板变量（推荐）

在 Railway 后端服务的环境变量中使用：

```env
SPRING_DATASOURCE_URL=jdbc:mysql://${{MySQL.MYSQLHOST}}:${{MySQL.MYSQLPORT}}/${{MySQL.MYSQLDATABASE}}?allowPublicKeyRetrieval=true&useSSL=false
SPRING_DATASOURCE_USERNAME=${{MySQL.MYSQLUSER}}
SPRING_DATASOURCE_PASSWORD=${{MySQL.MYSQLPASSWORD}}
SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver
```

### 方法 B: 使用 DATABASE_URL（如果可用）

Railway 有时会提供 `DATABASE_URL` 环境变量：

```env
SPRING_DATASOURCE_URL=${{MySQL.DATABASE_URL}}
```

如果这个变量存在，可以直接使用。

### 方法 C: 手动配置（如果模板变量不工作）

1. **获取数据库连接信息**
   - 点击 Railway 项目中的 MySQL 服务
   - 在 "Variables" 标签页查看：
     - `MYSQLHOST` - 数据库主机
     - `MYSQLPORT` - 端口（通常是 3306）
     - `MYSQLDATABASE` - 数据库名
     - `MYSQLUSER` - 用户名
     - `MYSQLPASSWORD` - 密码

2. **手动构建连接字符串**
   ```
   jdbc:mysql://[MYSQLHOST]:[MYSQLPORT]/[MYSQLDATABASE]?allowPublicKeyRetrieval=true&useSSL=false
   ```

3. **设置环境变量**
   ```env
   SPRING_DATASOURCE_URL=jdbc:mysql://your-host.railway.app:3306/railway?allowPublicKeyRetrieval=true&useSSL=false
   SPRING_DATASOURCE_USERNAME=root
   SPRING_DATASOURCE_PASSWORD=your_password
   ```

## 🔧 步骤 3: 配置 Spring Boot 应用

### 更新 application.properties

Railway 会通过环境变量覆盖配置，所以确保你的 `application.properties` 有默认值：

```properties
# 数据库配置（会被环境变量覆盖）
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:mysql://localhost:3306/kaizenfinance?allowPublicKeyRetrieval=true&useSSL=false}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:root}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:password}
spring.datasource.driver-class-name=${SPRING_DATASOURCE_DRIVER_CLASS_NAME:com.mysql.cj.jdbc.Driver}

# JPA 配置
spring.jpa.hibernate.ddl-auto=${SPRING_JPA_HIBERNATE_DDL_AUTO:update}
spring.jpa.show-sql=${SPRING_JPA_SHOW_SQL:false}
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

## ✅ 步骤 4: 验证数据库连接

1. **部署后端服务**
   - Railway 会自动构建和部署
   - 查看日志确认数据库连接成功

2. **检查日志**
   - 在 Railway 后端服务页面查看 "Deployments" > "View Logs"
   - 应该看到类似信息：
     ```
     HikariPool-1 - Starting...
     HikariPool-1 - Start completed.
     ```

3. **测试 API**
   - 访问后端健康检查端点
   - 测试登录功能
   - 检查数据库表是否自动创建

## 🗄️ 数据库管理

### 使用 Railway 的数据库管理工具

1. **访问数据库**
   - 在 Railway MySQL 服务页面
   - 点击 "Query" 标签
   - 可以直接执行 SQL 查询

2. **使用外部工具连接**
   - Railway 提供连接字符串
   - 可以使用 MySQL Workbench、DBeaver 等工具
   - 连接信息在 MySQL 服务的 "Variables" 标签

### 数据库备份

Railway 会自动备份数据库，但也可以：
- 使用 `mysqldump` 导出数据
- 通过 Railway CLI 备份
- 定期导出重要数据

## 💰 成本

- **Railway MySQL**: 包含在 Pro 计划中（$5/月）
- 免费套餐通常不包含数据库，但可以申请试用

## 🔒 安全注意事项

1. **不要提交密码到 Git**
   - 所有敏感信息使用环境变量
   - Railway 会自动保护这些变量

2. **数据库访问**
   - Railway 数据库默认只能从同一项目内的服务访问
   - 外部访问需要配置 Public Networking（不推荐生产环境）

3. **SSL 连接**
   - Railway MySQL 支持 SSL
   - 生产环境建议启用 SSL

## 🐛 常见问题

### Q: 数据库连接失败
**A**: 检查：
- 数据库服务是否运行
- 环境变量是否正确
- 连接字符串格式是否正确
- 端口是否正确（通常是 3306）

### Q: 表没有自动创建
**A**: 确保：
- `spring.jpa.hibernate.ddl-auto=update` 已设置
- 数据库用户有创建表的权限
- 查看日志中的错误信息

### Q: 如何查看数据库内容？
**A**: 
- 使用 Railway 的 Query 工具
- 或使用外部 MySQL 客户端连接

### Q: 数据库数据会丢失吗？
**A**: 
- Railway 会自动备份
- 但建议定期导出重要数据
- 删除服务会删除数据，请谨慎操作

## 📚 相关资源

- [Railway MySQL 文档](https://docs.railway.app/databases/mysql)
- [Railway 环境变量文档](https://docs.railway.app/develop/variables)
- [Spring Boot 数据库配置](https://spring.io/guides/gs/accessing-data-mysql/)

