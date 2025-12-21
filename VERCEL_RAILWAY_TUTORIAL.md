# 🚀 Vercel + Railway 完整部署教程

本教程将详细指导您如何将 Kaizen Finance 应用部署到 Vercel（前端）和 Railway（后端）。

## 📋 目录

1. [准备工作](#准备工作)
2. [部署前端到 Vercel](#部署前端到-vercel)
3. [部署后端到 Railway](#部署后端到-railway)
4. [配置数据库](#配置数据库)
5. [连接前后端](#连接前后端)
6. [测试部署](#测试部署)
7. [常见问题](#常见问题)

---

## 准备工作

### 1.1 注册账号

#### 注册 Vercel（前端）
1. 访问 https://vercel.com
2. 点击右上角 "Sign Up"
3. 选择 "Continue with GitHub"（推荐，方便后续部署）
4. 授权 GitHub 访问权限
5. 完成注册

#### 注册 Railway（后端）
1. 访问 https://railway.app
2. 点击右上角 "Login"
3. 选择 "Login with GitHub"（推荐）
4. 授权 GitHub 访问权限
5. 完成注册

### 1.2 准备 GitHub 仓库

确保您的代码已推送到 GitHub：

```bash
# 如果还没有 Git 仓库
cd C:\Users\ioone\Desktop\kaizen-finance-master
git init
git add .
git commit -m "Initial commit"

# 在 GitHub 创建新仓库，然后：
git remote add origin https://github.com/your-username/kaizen-finance.git
git branch -M main
git push -u origin main
```

### 1.3 更新前端 API 配置

编辑 `kaizen-finance/src/utils/request.js`：

```javascript
import axios from 'axios'
import router from '@/router'
import {ElMessage} from 'element-plus'

export const TokenKey='access_Token'

// 从环境变量读取 API 基础 URL，如果没有则使用相对路径
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api'

const service = axios.create({
  baseURL: API_BASE_URL,  // 添加这行
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// ... 其余代码保持不变
```

### 1.4 更新后端 CORS 配置

编辑 `kaizen-finance-backend/backend/src/main/java/com/loginapp/loginbackend/config/CorsConfig.java`：

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                    "http://localhost:5173", 
                    "http://localhost:3000", 
                    "http://localhost:4173",
                    "https://your-app.vercel.app",  // 添加您的 Vercel 域名
                    "https://*.vercel.app"  // 允许所有 Vercel 子域名
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
```

**注意**：部署后需要将 `your-app.vercel.app` 替换为实际的 Vercel 域名。

---

## 部署前端到 Vercel

### 步骤 1: 导入项目

1. **登录 Vercel**
   - 访问 https://vercel.com
   - 使用 GitHub 账号登录

2. **创建新项目**
   - 点击右上角 "Add New..." → "Project"
   - 或者直接点击 "Add New Project"

3. **选择仓库**
   - 在仓库列表中找到您的 `kaizen-finance` 仓库
   - 点击 "Import"

### 步骤 2: 配置项目

1. **项目设置**
   - **Project Name**: `kaizen-finance`（或自定义）
   - **Framework Preset**: 选择 "Vite"（Vercel 会自动检测）
   - **Root Directory**: 点击 "Edit"，选择 `kaizen-finance` 目录
     - 如果仓库根目录就是前端代码，选择 `./`
     - 如果前端在子目录，选择 `kaizen-finance`

2. **构建配置**
   - **Build Command**: `npm run build`（自动填充）
   - **Output Directory**: `dist`（自动填充）
   - **Install Command**: `npm install`（自动填充）

3. **环境变量**（暂时不填，部署后端后再添加）
   - 点击 "Environment Variables"
   - 添加：
     ```
     Name: VITE_API_BASE_URL
     Value: （暂时留空，部署后端后填写）
     ```
   - 选择环境：Production, Preview, Development（全选）

### 步骤 3: 部署

1. **开始部署**
   - 点击 "Deploy" 按钮
   - 等待构建完成（通常 1-3 分钟）

2. **获取部署域名**
   - 部署成功后，Vercel 会提供一个域名
   - 格式：`kaizen-finance-xxxxx.vercel.app`
   - 或者您可以使用自定义域名

3. **测试前端**
   - 访问提供的域名
   - 应该能看到前端页面（但 API 调用会失败，因为后端还没部署）

---

## 部署后端到 Railway

### 步骤 1: 创建新项目

1. **登录 Railway**
   - 访问 https://railway.app
   - 使用 GitHub 账号登录

2. **创建新项目**
   - 点击 "New Project"
   - 选择 "Deploy from GitHub repo"
   - 选择您的 GitHub 仓库

3. **选择服务目录**
   - Railway 会检测项目结构
   - 如果检测不到，点击 "Configure" 手动设置
   - **Root Directory**: 选择 `kaizen-finance-backend/backend`

### 步骤 2: 添加 MySQL 数据库

1. **添加数据库服务**
   - 在项目页面，点击 "+ New"
   - 选择 "Database"
   - 选择 "Add MySQL"

2. **等待数据库就绪**
   - Railway 会自动创建 MySQL 8.0 实例
   - 等待 30-60 秒，状态变为 "Active"
   - 数据库会自动配置好

3. **查看数据库信息**
   - 点击 MySQL 服务
   - 在 "Variables" 标签页可以看到：
     - `MYSQLHOST`
     - `MYSQLPORT`
     - `MYSQLDATABASE`
     - `MYSQLUSER`
     - `MYSQLPASSWORD`

### 步骤 3: 配置后端环境变量

1. **进入后端服务设置**
   - 在项目页面，点击后端服务（Spring Boot 服务）
   - 点击 "Variables" 标签

2. **添加环境变量**

   点击 "New Variable" 逐个添加：

   **数据库配置**（使用 Railway 模板变量）：
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

   **JPA 配置**：
   ```
   Name: SPRING_JPA_HIBERNATE_DDL_AUTO
   Value: update
   ```

   ```
   Name: SPRING_JPA_SHOW_SQL
   Value: false
   ```

   **邮件服务配置**：
   ```
   Name: EMAIL_SERVICE_PROVIDER
   Value: brevo
   ```

   ```
   Name: EMAIL_BREVO_API_KEY
   Value: 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
   ```
   ⚠️ **重要**：这里填入您的真实 Brevo API Key。这个值只存储在 Railway 的环境变量中，不会出现在代码里，所以是安全的。

   ```
   Name: EMAIL_BREVO_FROM_EMAIL
   Value: info@kaizensolution.co.uk
   ```

   ```
   Name: EMAIL_BREVO_FROM_NAME
   Value: Kaizen Solution
   ```
   
   💡 **说明**：环境变量是安全的配置方式。代码中只使用 `${EMAIL_BREVO_API_KEY}` 占位符，实际值在 Railway 平台设置。这样即使代码公开，API Key 也不会泄露。

   **服务器配置**：
   ```
   Name: SERVER_PORT
   Value: 8080
   ```

   ```
   Name: SERVER_ADDRESS
   Value: 0.0.0.0
   ```

### 步骤 4: 配置构建和部署

1. **检查构建配置**
   - Railway 会自动检测 Maven 项目
   - 如果检测不到，在 "Settings" → "Build" 中设置：
     - **Build Command**: `mvn clean package -DskipTests`
     - **Start Command**: `java -jar target/*.jar`

2. **开始部署**
   - Railway 会自动开始构建
   - 查看 "Deployments" 标签页的日志
   - 等待构建完成（通常 3-5 分钟）

3. **获取后端 URL**
   - 部署成功后，Railway 会生成一个公共 URL
   - 格式：`your-app-production.up.railway.app`
   - 在服务页面点击 "Settings" → "Generate Domain" 可以生成自定义域名

### 步骤 5: 验证后端部署

1. **检查日志**
   - 在 "Deployments" 标签页查看日志
   - 应该看到：
     ```
     Started BackendrunningApplication
     Server is running on port 8080
     ```

2. **测试 API**
   - 访问：`https://your-railway-url.railway.app/api/test`
   - 应该返回 JSON 响应

---

## 配置数据库

### 验证数据库连接

1. **查看后端日志**
   - 在 Railway 后端服务的 "Deployments" → "View Logs"
   - 应该看到数据库连接成功的日志：
     ```
     HikariPool-1 - Starting...
     HikariPool-1 - Start completed.
     ```

2. **检查表是否创建**
   - 在 Railway MySQL 服务页面
   - 点击 "Query" 标签
   - 执行：`SHOW TABLES;`
   - 应该看到自动创建的表（如 `users`, `orders`, `contact_info` 等）

### 如果数据库连接失败

1. **检查环境变量**
   - 确认所有数据库相关的环境变量都已设置
   - 特别是 `SPRING_DATASOURCE_URL` 格式正确

2. **手动构建连接字符串**
   - 如果模板变量 `${{MySQL.XXX}}` 不工作
   - 在 MySQL 服务的 "Variables" 标签查看实际值
   - 手动构建 URL：
     ```
     jdbc:mysql://[实际HOST]:[实际PORT]/[实际DATABASE]?allowPublicKeyRetrieval=true&useSSL=false
     ```

---

## 连接前后端

### 步骤 1: 更新前端环境变量

1. **获取后端 URL**
   - 在 Railway 后端服务页面
   - 复制公共 URL（如：`https://your-app.up.railway.app`）

2. **更新 Vercel 环境变量**
   - 在 Vercel 项目页面
   - 点击 "Settings" → "Environment Variables"
   - 找到 `VITE_API_BASE_URL`
   - 更新值为：`https://your-railway-url.railway.app/api`
   - 点击 "Save"

3. **重新部署前端**
   - 在 Vercel 项目页面
   - 点击 "Deployments" 标签
   - 找到最新的部署，点击 "..." → "Redeploy"
   - 或者推送新代码到 GitHub（会自动触发部署）

### 步骤 2: 更新后端 CORS 配置

1. **更新 CorsConfig.java**
   - 编辑 `kaizen-finance-backend/backend/src/main/java/com/loginapp/loginbackend/config/CorsConfig.java`
   - 将 `your-app.vercel.app` 替换为实际的 Vercel 域名

2. **提交并推送代码**
   ```bash
   git add .
   git commit -m "Update CORS for production"
   git push
   ```

3. **Railway 自动重新部署**
   - Railway 检测到代码更新会自动重新部署
   - 等待部署完成

### 步骤 3: 更新 SecurityConfig（如果需要）

检查 `SecurityConfig.java` 中的 CORS 配置，确保包含 Vercel 域名。

---

## 测试部署

### 1. 测试前端访问
- 访问 Vercel 提供的域名
- 应该能看到完整的页面

### 2. 测试登录功能
- 尝试登录
- 检查浏览器控制台是否有错误
- 检查 Network 标签，确认 API 请求是否成功

### 3. 测试 API 调用
- 打开浏览器开发者工具
- 查看 Network 标签
- 所有 `/api/` 请求应该指向 Railway 后端
- 状态码应该是 200 或 401（未登录），不应该是 404

### 4. 测试数据库操作
- 尝试注册/登录
- 提交联系表单
- 创建订单
- 检查数据是否保存到数据库

---

## 常见问题

### Q1: 前端显示但 API 调用失败

**检查清单**：
- ✅ 前端环境变量 `VITE_API_BASE_URL` 是否正确设置
- ✅ 后端 URL 是否可访问（在浏览器直接访问）
- ✅ CORS 配置是否包含前端域名
- ✅ 浏览器控制台是否有 CORS 错误

**解决方案**：
1. 检查 Vercel 环境变量是否正确
2. 重新部署前端
3. 检查后端 CORS 配置
4. 查看浏览器控制台错误信息

### Q2: 数据库连接失败

**检查清单**：
- ✅ MySQL 服务是否运行（状态为 Active）
- ✅ 环境变量是否正确设置
- ✅ 连接字符串格式是否正确

**解决方案**：
1. 检查 Railway MySQL 服务状态
2. 查看后端日志中的错误信息
3. 尝试手动构建连接字符串
4. 参考 `RAILWAY_DATABASE_SETUP.md` 详细指南

### Q3: 构建失败

**前端构建失败**：
- 检查 `package.json` 中的脚本
- 查看 Vercel 构建日志
- 确保所有依赖都已安装

**后端构建失败**：
- 检查 Maven 配置
- 查看 Railway 构建日志
- 确保 Java 17 可用

### Q4: 邮件发送失败

**检查清单**：
- ✅ Brevo API Key 是否正确
- ✅ 发件邮箱是否已验证
- ✅ 环境变量是否正确设置

**解决方案**：
1. 检查 Brevo 账号设置
2. 验证发件邮箱
3. 查看后端日志中的邮件错误

### Q5: 如何查看日志

**Vercel 日志**：
- 项目页面 → "Deployments" → 点击部署 → "View Function Logs"

**Railway 日志**：
- 服务页面 → "Deployments" → "View Logs"

### Q6: 如何更新代码

**自动部署**：
- 推送代码到 GitHub
- Vercel 和 Railway 会自动检测并部署

**手动触发**：
- Vercel: "Deployments" → "Redeploy"
- Railway: "Deployments" → "Redeploy"

---

## 💰 成本说明

### Vercel（前端）
- **Hobby 计划**: 免费
  - 无限部署
  - 100GB 带宽/月
  - 自动 SSL
  - 自定义域名支持

### Railway（后端 + 数据库）
- **Starter 计划**: $5/月
  - $5 免费额度
  - 包含 MySQL 数据库
  - 500 小时运行时间
  - 100GB 出站流量

**总计**: 约 $5/月（如果超出免费额度）

---

## 🎉 完成！

恭喜！您的应用已经成功部署。现在可以：

1. ✅ 访问前端：`https://your-app.vercel.app`
2. ✅ API 正常工作
3. ✅ 数据库连接正常
4. ✅ 邮件服务正常

### 下一步建议

1. **添加自定义域名**
   - Vercel: Settings → Domains
   - Railway: Settings → Generate Domain

2. **设置监控**
   - Vercel Analytics（可选）
   - Railway Metrics（内置）

3. **配置备份**
   - Railway 自动备份数据库
   - 定期导出重要数据

4. **优化性能**
   - 启用 CDN（Vercel 自动）
   - 优化图片和静态资源

---

## 📚 相关文档

- [Vercel 文档](https://vercel.com/docs)
- [Railway 文档](https://docs.railway.app)
- [RAILWAY_DATABASE_SETUP.md](./RAILWAY_DATABASE_SETUP.md) - 数据库详细配置
- [DEPLOY_VERCEL_RAILWAY.md](./DEPLOY_VERCEL_RAILWAY.md) - 快速参考

---

**需要帮助？** 查看常见问题部分或检查日志获取详细错误信息。

