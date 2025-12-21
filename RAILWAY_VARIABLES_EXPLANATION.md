# Railway 环境变量说明

## 🔍 您看到的情况

Railway 自动扫描了您的代码，找到了 `VITE_APP_BASE_URL` 等变量。这是正常的，但需要注意：

### ⚠️ 重要区分

**前端变量（Vercel 配置）**：
- `VITE_APP_BASE_URL` 或 `VITE_API_BASE_URL`
- 这些应该在 **Vercel** 中配置，不是在 Railway 中

**后端变量（Railway 配置）**：
- 数据库配置（`SPRING_DATASOURCE_*`）
- 邮件服务配置（`EMAIL_*`）
- 服务器配置（`SERVER_*`）
- 这些应该在 **Railway** 中配置

---

## ✅ 在 Railway 中应该做什么

### 1. 删除或忽略前端变量

在 Railway 的 Variables 页面：
- **可以删除** `VITE_APP_BASE_URL`（这是前端用的）
- 或者**保留但设置为空值**（不会影响后端）

### 2. 添加后端需要的变量

在 Railway 的 Variables 页面，点击 "New Variable" 添加：

#### 数据库配置（必须先添加 MySQL 数据库服务）

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

```
Name: SPRING_JPA_HIBERNATE_DDL_AUTO
Value: update
```

```
Name: SPRING_JPA_SHOW_SQL
Value: false
```

#### 邮件服务配置

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

#### 服务器配置

```
Name: SERVER_PORT
Value: ${PORT}
```

```
Name: SERVER_ADDRESS
Value: 0.0.0.0
```

---

## 📋 配置检查清单

### Railway（后端）需要：
- [ ] 删除或忽略 `VITE_APP_BASE_URL`（前端变量）
- [ ] 添加数据库环境变量（使用 `${{MySQL.XXX}}` 模板变量）
- [ ] 添加邮件服务环境变量
- [ ] 添加服务器配置环境变量

### Vercel（前端）需要：
- [ ] 添加 `VITE_API_BASE_URL`（后端 URL，部署后端后填写）
- 格式：`https://your-railway-backend.railway.app/api`

---

## 💡 为什么会有前端变量？

Railway 扫描了整个仓库，包括前端代码，所以检测到了 `VITE_*` 变量。

**解决方案**：
- 在 Railway 中：删除或忽略这些前端变量
- 在 Vercel 中：配置前端变量

---

## 🎯 下一步

1. **在 Railway 中**：
   - 删除 `VITE_APP_BASE_URL`（如果不需要）
   - 添加后端需要的环境变量（数据库、邮件等）

2. **确保已添加 MySQL 数据库**：
   - 在项目主页点击 "+ New" → "Database" → "Add MySQL"
   - 然后才能使用 `${{MySQL.XXX}}` 模板变量

3. **配置完成后**：
   - Railway 会自动重新部署
   - 查看部署日志确认成功

