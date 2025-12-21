# 🔐 环境变量配置指南

## ❓ 为什么代码中不能包含 API Key？

**安全原因**：
- ✅ 代码会提交到 GitHub（公开或私有）
- ✅ 任何人都能看到代码内容
- ✅ API Key 泄露会导致安全风险
- ✅ 如果 API Key 泄露，需要立即撤销并重新生成

**最佳实践**：
- ✅ 敏感信息（API Key、密码等）存储在**环境变量**中
- ✅ 代码从环境变量读取配置
- ✅ 每个部署环境（开发、生产）使用不同的配置

---

## 🎯 环境变量工作原理

### 概念说明

**环境变量** = 在部署平台（Vercel/Railway）中设置的配置值，应用运行时可以读取

```
代码中（安全）:
  email.brevo.api.key=${EMAIL_BREVO_API_KEY:默认值}
  
部署平台设置（Vercel/Railway）:
  EMAIL_BREVO_API_KEY = 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
  
应用运行时:
  读取环境变量 → 使用真实的 API Key
```

---

## 📍 在哪里配置环境变量？

### 后端（Railway）- 配置位置

1. **登录 Railway**
   - 访问 https://railway.app
   - 进入您的项目

2. **找到后端服务**
   - 点击 Spring Boot 后端服务

3. **进入 Variables 标签**
   - 点击 "Variables" 标签页
   - 这里就是配置环境变量的地方

4. **添加环境变量**
   - 点击 "New Variable"
   - 输入变量名和值
   - 保存

5. **Railway 会自动应用**
   - 保存后，Railway 会自动重新部署
   - 应用启动时会读取这些环境变量

### 前端（Vercel）- 配置位置

1. **登录 Vercel**
   - 访问 https://vercel.com
   - 进入您的项目

2. **进入 Settings**
   - 点击 "Settings" 标签

3. **找到 Environment Variables**
   - 在左侧菜单找到 "Environment Variables"
   - 这里就是配置环境变量的地方

4. **添加环境变量**
   - 点击 "Add New"
   - 输入变量名和值
   - 选择环境（Production, Preview, Development）

5. **重新部署**
   - 添加后需要重新部署才能生效
   - 点击 "Deployments" → 选择最新部署 → "Redeploy"

---

## 🔧 具体配置步骤

### 后端环境变量（Railway）

在 Railway 后端服务的 "Variables" 标签中添加：

```env
# 数据库配置（使用 Railway 模板变量）
SPRING_DATASOURCE_URL=jdbc:mysql://${{MySQL.MYSQLHOST}}:${{MySQL.MYSQLPORT}}/${{MySQL.MYSQLDATABASE}}?allowPublicKeyRetrieval=true&useSSL=false
SPRING_DATASOURCE_USERNAME=${{MySQL.MYSQLUSER}}
SPRING_DATASOURCE_PASSWORD=${{MySQL.MYSQLPASSWORD}}
SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver

# JPA 配置
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=false

# 邮件服务配置（这里填入真实的 API Key）
EMAIL_SERVICE_PROVIDER=brevo
EMAIL_BREVO_API_KEY=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
EMAIL_BREVO_FROM_EMAIL=info@kaizensolution.co.uk
EMAIL_BREVO_FROM_NAME=Kaizen Solution

# 服务器配置
SERVER_PORT=8080
SERVER_ADDRESS=0.0.0.0
```

### 前端环境变量（Vercel）

在 Vercel 项目的 "Settings" → "Environment Variables" 中添加：

```env
# API 基础 URL（后端地址）
VITE_API_BASE_URL=https://your-railway-backend.railway.app/api
```

---

## 💡 代码如何读取环境变量？

### 后端（Spring Boot）

在 `application.properties` 中使用 `${变量名:默认值}` 格式：

```properties
# 如果环境变量 EMAIL_BREVO_API_KEY 存在，使用它；否则使用默认值
email.brevo.api.key=${EMAIL_BREVO_API_KEY:YOUR_BREVO_API_KEY_HERE}
```

**工作原理**：
1. Spring Boot 启动时读取 `application.properties`
2. 遇到 `${EMAIL_BREVO_API_KEY:默认值}` 时
3. 先查找环境变量 `EMAIL_BREVO_API_KEY`
4. 如果找到，使用环境变量的值
5. 如果没找到，使用默认值

### 前端（Vue.js）

在代码中使用 `import.meta.env.VITE_XXX`：

```javascript
// 从环境变量读取 API URL
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api'
```

**工作原理**：
1. Vite 构建时读取 `.env` 文件或 Vercel 环境变量
2. 以 `VITE_` 开头的变量会被注入到代码中
3. 通过 `import.meta.env.VITE_XXX` 访问

---

## 🔒 安全性说明

### ✅ 为什么这样更安全？

1. **代码中不包含敏感信息**
   - GitHub 上看到的只是占位符
   - 即使代码泄露，API Key 也不会泄露

2. **每个环境独立配置**
   - 开发环境用测试 API Key
   - 生产环境用真实 API Key
   - 互不干扰

3. **可以随时更新**
   - 不需要修改代码
   - 只需要更新环境变量
   - 重新部署即可

4. **访问控制**
   - 只有有权限的人才能看到环境变量
   - Railway/Vercel 会加密存储

---

## 📝 完整配置示例

### Railway 后端环境变量列表

```
SPRING_DATASOURCE_URL = jdbc:mysql://${{MySQL.MYSQLHOST}}:${{MySQL.MYSQLPORT}}/${{MySQL.MYSQLDATABASE}}?allowPublicKeyRetrieval=true&useSSL=false
SPRING_DATASOURCE_USERNAME = ${{MySQL.MYSQLUSER}}
SPRING_DATASOURCE_PASSWORD = ${{MySQL.MYSQLPASSWORD}}
SPRING_DATASOURCE_DRIVER_CLASS_NAME = com.mysql.cj.jdbc.Driver
SPRING_JPA_HIBERNATE_DDL_AUTO = update
SPRING_JPA_SHOW_SQL = false
EMAIL_SERVICE_PROVIDER = brevo
EMAIL_BREVO_API_KEY = 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
EMAIL_BREVO_FROM_EMAIL = info@kaizensolution.co.uk
EMAIL_BREVO_FROM_NAME = Kaizen Solution
SERVER_PORT = 8080
SERVER_ADDRESS = 0.0.0.0
```

### Vercel 前端环境变量列表

```
VITE_API_BASE_URL = https://your-railway-backend.railway.app/api
```

---

## 🎯 总结

**问题**：代码中不能包含 API Key，部署后如何知道邮箱地址和 API Key？

**答案**：
1. ✅ **在部署平台（Railway/Vercel）的设置中配置环境变量**
2. ✅ **代码从环境变量读取配置**
3. ✅ **这样既安全又灵活**

**配置位置**：
- **后端**：Railway 项目 → 后端服务 → Variables 标签
- **前端**：Vercel 项目 → Settings → Environment Variables

**优势**：
- 🔒 更安全（代码中不包含敏感信息）
- 🔄 更灵活（可以随时更新，无需改代码）
- 🎯 更专业（行业标准做法）

---

## 📚 相关文档

- [Railway 环境变量文档](https://docs.railway.app/develop/variables)
- [Vercel 环境变量文档](https://vercel.com/docs/concepts/projects/environment-variables)
- [Spring Boot 外部化配置](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)

