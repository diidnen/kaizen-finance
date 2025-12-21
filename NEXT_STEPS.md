# 🚀 部署下一步操作指南

恭喜！代码已成功推送到 GitHub。现在开始部署。

## 📋 部署流程概览

```
1. 部署后端到 Railway ✅
   ├── 创建项目
   ├── 添加 MySQL 数据库
   ├── 配置环境变量
   └── 获取后端 URL

2. 部署前端到 Vercel ✅
   ├── 导入项目
   ├── 配置环境变量（后端 URL）
   └── 获取前端 URL

3. 连接前后端 ✅
   ├── 更新 CORS 配置
   └── 测试功能
```

---

## 步骤 1: 部署后端到 Railway（先做这个）

### 1.1 登录 Railway

1. 访问 https://railway.app
2. 使用 GitHub 账号登录
3. 点击 "New Project"

### 1.2 导入后端项目

1. 选择 "Deploy from GitHub repo"
2. 选择您的仓库：`diidnen/kaizen-finance`
3. 如果 Railway 没有自动检测到后端目录：
   - 点击服务 → "Settings" → "Root Directory"
   - 设置为：`kaizen-finance-backend/backend`

### 1.3 添加 MySQL 数据库

1. 在项目页面，点击 **"+ New"**
2. 选择 **"Database"**
3. 选择 **"Add MySQL"**
4. 等待数据库创建完成（30-60秒）

### 1.4 配置后端环境变量

1. 点击后端服务（Spring Boot 服务）
2. 点击 **"Variables"** 标签
3. 点击 **"New Variable"** 添加以下变量：

#### 数据库配置（使用 Railway 模板变量）：

```
Name: SPRING_DATASOURCE_URL
Value: jdbc:mysql://${{MySQL.MYSQLHOST}}:${{MySQL.MYSQLPORT}}/${{MySQL.MYSQLDATABASE}}?allowPublicKeyRetrieval=true&useSSL=false
```

```
Name: SPRING_DATASOURCE_USERNAME
Value: ${{MySQL.MYSQLUSER}}
```

```
Name: SPRING_DATASOURCE_PASSWORD
Value: ${{MySQL.MYSQLPASSWORD}}
```

```
Name: SPRING_DATASOURCE_DRIVER_CLASS_NAME
Value: com.mysql.cj.jdbc.Driver
```

#### JPA 配置：

```
Name: SPRING_JPA_HIBERNATE_DDL_AUTO
Value: update
```

```
Name: SPRING_JPA_SHOW_SQL
Value: false
```

#### 邮件服务配置：

```
Name: EMAIL_SERVICE_PROVIDER
Value: brevo
```

```
Name: EMAIL_BREVO_API_KEY
Value: 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
```

```
Name: EMAIL_BREVO_FROM_EMAIL
Value: info@kaizensolution.co.uk
```

```
Name: EMAIL_BREVO_FROM_NAME
Value: Kaizen Solution
```

#### 服务器配置：

```
Name: SERVER_PORT
Value: 8080
```

```
Name: SERVER_ADDRESS
Value: 0.0.0.0
```

### 1.5 等待部署完成

1. Railway 会自动开始构建和部署
2. 查看 "Deployments" 标签页的日志
3. 等待看到：`Started BackendrunningApplication`
4. 通常需要 3-5 分钟

### 1.6 获取后端 URL

1. 部署完成后，Railway 会生成一个公共 URL
2. 格式：`your-app-production.up.railway.app`
3. 在服务页面点击 "Settings" → "Generate Domain" 可以生成自定义域名
4. **复制这个 URL，稍后需要用到**

---

## 步骤 2: 部署前端到 Vercel

### 2.1 登录 Vercel

1. 访问 https://vercel.com
2. 使用 GitHub 账号登录
3. 点击 "Add New..." → "Project"

### 2.2 导入前端项目

1. 在仓库列表中找到 `diidnen/kaizen-finance`
2. 点击 "Import"

### 2.3 配置项目

1. **Project Name**: `kaizen-finance`（或自定义）
2. **Framework Preset**: 选择 "Vite"（Vercel 会自动检测）
3. **Root Directory**: 点击 "Edit"，选择 `kaizen-finance`
4. **Build Command**: `npm run build`（自动填充）
5. **Output Directory**: `dist`（自动填充）

### 2.4 配置环境变量

1. 点击 "Environment Variables"
2. 添加：
   ```
   Name: VITE_API_BASE_URL
   Value: https://your-railway-backend.railway.app/api
   ```
   ⚠️ **重要**：将 `your-railway-backend.railway.app` 替换为步骤 1.6 中获取的实际 Railway 后端 URL

3. 选择环境：Production, Preview, Development（全选）

### 2.5 部署

1. 点击 "Deploy" 按钮
2. 等待构建完成（通常 1-3 分钟）
3. 部署成功后，Vercel 会提供一个域名
4. 格式：`kaizen-finance-xxxxx.vercel.app`
5. **复制这个 URL，稍后需要用到**

---

## 步骤 3: 更新后端 CORS 配置

### 3.1 更新代码

编辑 `kaizen-finance-backend/backend/src/main/java/com/loginapp/loginbackend/config/CorsConfig.java`：

```java
.allowedOrigins(
    "http://localhost:5173", 
    "http://localhost:3000", 
    "http://localhost:4173",
    "https://your-app.vercel.app",  // 替换为实际的 Vercel 域名
    "https://*.vercel.app"  // 允许所有 Vercel 子域名
)
```

同样更新 `SecurityConfig.java` 中的 CORS 配置。

### 3.2 提交并推送

```bash
git add .
git commit -m "Update CORS for production deployment"
git push
```

Railway 会自动检测代码更新并重新部署。

---

## 步骤 4: 测试部署

### 4.1 测试前端

1. 访问 Vercel 提供的域名
2. 应该能看到完整的页面
3. 检查浏览器控制台是否有错误

### 4.2 测试 API

1. 打开浏览器开发者工具
2. 查看 Network 标签
3. 尝试登录或提交表单
4. 确认 API 请求指向 Railway 后端
5. 状态码应该是 200 或 401（未登录），不应该是 404

### 4.3 测试数据库

1. 在 Railway MySQL 服务页面
2. 点击 "Query" 标签
3. 执行：`SHOW TABLES;`
4. 应该看到自动创建的表

---

## ✅ 完成检查清单

- [ ] 后端已部署到 Railway
- [ ] MySQL 数据库已创建
- [ ] 后端环境变量已配置
- [ ] 后端 URL 已获取
- [ ] 前端已部署到 Vercel
- [ ] 前端环境变量已配置（包含后端 URL）
- [ ] CORS 配置已更新
- [ ] 前端可以访问
- [ ] API 调用正常
- [ ] 数据库连接正常

---

## 🆘 遇到问题？

### 后端无法启动
- 检查 Railway 日志
- 确认环境变量是否正确
- 确认数据库是否运行

### 前端无法连接后端
- 检查 Vercel 环境变量 `VITE_API_BASE_URL` 是否正确
- 检查后端 CORS 配置
- 检查浏览器控制台错误

### 数据库连接失败
- 检查 MySQL 服务状态
- 检查数据库环境变量
- 查看后端日志

---

## 📚 相关文档

- `VERCEL_RAILWAY_TUTORIAL.md` - 详细部署教程
- `ENVIRONMENT_VARIABLES_GUIDE.md` - 环境变量详细说明
- `RAILWAY_DATABASE_SETUP.md` - 数据库配置指南

---

**准备好了吗？让我们从步骤 1 开始！** 🚀

