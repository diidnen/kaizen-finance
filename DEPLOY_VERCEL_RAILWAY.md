# Vercel + Railway 部署指南

这是最简单的部署方案，适合预算有限的项目。

## 📋 前置要求

1. GitHub 账号
2. Vercel 账号（免费）
3. Railway 账号（免费）

## 🚀 步骤 1: 准备代码

### 1.1 更新前端 API 配置

编辑 `kaizen-finance/src/utils/request.js`，确保 API 基础 URL 可以从环境变量读取：

```javascript
const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
});
```

### 1.2 创建环境变量文件

在 `kaizen-finance` 目录创建 `.env.production`:
```env
VITE_API_BASE_URL=https://your-railway-backend.railway.app/api
```

## 🎨 步骤 2: 部署前端到 Vercel

1. **访问 Vercel**
   - 前往 https://vercel.com
   - 使用 GitHub 账号登录

2. **导入项目**
   - 点击 "Add New Project"
   - 选择你的 GitHub 仓库
   - 选择 `kaizen-finance` 目录

3. **配置构建**
   - Framework Preset: Vite
   - Build Command: `npm run build`
   - Output Directory: `dist`
   - Install Command: `npm install`

4. **环境变量**
   - 添加 `VITE_API_BASE_URL`（部署后端后更新）

5. **部署**
   - 点击 "Deploy"
   - 等待构建完成

## ⚙️ 步骤 3: 部署后端到 Railway

1. **访问 Railway**
   - 前往 https://railway.app
   - 使用 GitHub 账号登录

2. **创建新项目**
   - 点击 "New Project"
   - 选择 "Deploy from GitHub repo"
   - 选择你的仓库和 `kaizen-finance-backend/backend` 目录

3. **添加 MySQL 数据库** ✅
   - 在项目页面点击 "+ New"
   - 选择 "Database" > "Add MySQL"
   - Railway 会自动创建 MySQL 数据库实例
   - 数据库会自动配置好，无需手动设置
   - Railway 会提供数据库连接信息

4. **配置环境变量**
   在 Railway 后端服务设置中添加环境变量：
   
   **数据库配置（使用 Railway 提供的变量）**：
   ```
   SPRING_DATASOURCE_URL=${{MySQL.DATABASE_URL}}
   SPRING_DATASOURCE_USERNAME=${{MySQL.MYSQLUSER}}
   SPRING_DATASOURCE_PASSWORD=${{MySQL.MYSQLPASSWORD}}
   ```
   
   **或者手动配置（如果上面的变量不工作）**：
   ```
   SPRING_DATASOURCE_URL=jdbc:mysql://${{MySQL.MYSQLHOST}}:${{MySQL.MYSQLPORT}}/${{MySQL.MYSQLDATABASE}}?allowPublicKeyRetrieval=true&useSSL=false
   SPRING_DATASOURCE_USERNAME=${{MySQL.MYSQLUSER}}
   SPRING_DATASOURCE_PASSWORD=${{MySQL.MYSQLPASSWORD}}
   ```
   
   **其他配置**：
   ```
   SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver
   SPRING_JPA_HIBERNATE_DDL_AUTO=update
   SPRING_JPA_SHOW_SQL=false
   SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.MySQLDialect
   EMAIL_SERVICE_PROVIDER=brevo
   EMAIL_BREVO_API_KEY=your_brevo_api_key
   EMAIL_BREVO_FROM_EMAIL=info@kaizensolution.co.uk
   EMAIL_BREVO_FROM_NAME=Kaizen Solution
   SERVER_PORT=8080
   SERVER_ADDRESS=0.0.0.0
   ```

5. **部署**
   - Railway 会自动检测 Maven 项目
   - 自动构建和部署
   - 等待部署完成

6. **获取后端 URL**
   - 在 Railway 项目页面
   - 点击后端服务
   - 复制生成的域名（如：`your-app.railway.app`）

## 🔗 步骤 4: 连接前后端

1. **更新前端环境变量**
   - 在 Vercel 项目设置中
   - 更新 `VITE_API_BASE_URL` 为你的 Railway 后端 URL
   - 重新部署前端

2. **更新后端 CORS**
   - 编辑 `CorsConfig.java`
   - 添加你的 Vercel 域名到允许列表

## ✅ 验证部署

1. 访问 Vercel 提供的域名
2. 测试登录功能
3. 测试 API 调用

## 💰 成本

- **Vercel**: 免费（Hobby 计划）
- **Railway**: $5/月（Pro 计划，有免费额度）
- **总计**: 约 $5/月

## 🔧 故障排除

### 前端无法连接后端
- 检查 CORS 配置
- 确认后端 URL 正确
- 检查浏览器控制台错误

### 数据库连接失败
- 检查 Railway 数据库状态（确保数据库服务正在运行）
- 确认环境变量正确（特别是 `${{MySQL.DATABASE_URL}}` 格式）
- 如果使用手动配置，检查主机、端口、数据库名是否正确
- 查看 Railway 后端服务日志
- 确保数据库已创建（Railway 会自动创建，但可能需要等待几秒钟）

### 数据库变量格式问题
如果 `${{MySQL.DATABASE_URL}}` 不工作，可以：
1. 在 Railway 数据库服务页面查看连接信息
2. 手动构建连接字符串：
   ```
   jdbc:mysql://[MYSQLHOST]:[MYSQLPORT]/[MYSQLDATABASE]?allowPublicKeyRetrieval=true&useSSL=false
   ```
3. 使用 Railway 提供的模板变量：
   - `${{MySQL.MYSQLHOST}}`
   - `${{MySQL.MYSQLPORT}}`
   - `${{MySQL.MYSQLDATABASE}}`
   - `${{MySQL.MYSQLUSER}}`
   - `${{MySQL.MYSQLPASSWORD}}`

### 邮件发送失败
- 检查 Brevo API Key
- 确认发件邮箱已验证

