# Railway 服务配置完整指南

## ✅ 已完成的配置
- Root Directory: `kaizen-finance-backend/backend` ✓

## 🔧 需要配置的部分

### 1. Build（构建配置）

#### Custom Build Command（自定义构建命令）
- **是否启用**：可以启用，也可以不启用（如果根目录有 `railway.json`）
- **如果启用**：设置为
  ```
  mvn clean package -DskipTests
  ```
- **如果根目录有 railway.json**：可以留空，Railway 会自动使用配置文件

#### Builder
- **保持默认**：Railpack（Default）
- 不需要修改

#### Metal Build Environment
- **保持默认**：不启用（除非需要更快构建）
- 可以暂时不启用

---

### 2. Deploy（部署配置）

#### Custom Start Command（自定义启动命令）⭐ **重要**
- **启用**：点击启用
- **设置为**：
  ```
  java -jar target/backend-1.0-SNAPSHOT.jar
  ```
- 这是启动 Spring Boot 应用的命令

#### Restart Policy
- **保持默认**：On Failure（失败时重启）
- **Max restart retries**：10（默认即可）

#### Healthcheck Path
- **可以留空**：Spring Boot 会自动处理
- 或者设置为：`/api/test`（如果有健康检查端点）

---

### 3. Variables（环境变量）⭐ **最重要**

点击左侧边栏的 **"Variables"** 标签，添加以下环境变量：

#### 数据库配置（使用 Railway 模板变量）

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

#### JPA 配置

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

### 4. Networking（网络配置）

#### Public Networking
- **Generate Domain**：点击这个按钮生成公共域名
- 这会生成一个类似 `your-app.up.railway.app` 的 URL
- **重要**：生成后复制这个 URL，稍后配置前端时需要用到

#### Private Networking
- **保持默认**：不需要修改
- 这是服务内部通信用的

---

### 5. 其他配置（保持默认）

以下配置可以保持默认，不需要修改：

- ✅ **Regions**：保持默认（US West）
- ✅ **Resource Limits**：保持默认（1 GB 内存足够）
- ✅ **Cron Schedule**：不需要
- ✅ **Serverless**：不需要
- ✅ **Config-as-code**：如果根目录有 `railway.json`，可以启用

---

## 📋 配置检查清单

### 必须配置：
- [x] Root Directory: `kaizen-finance-backend/backend`
- [ ] Start Command: `java -jar target/backend-1.0-SNAPSHOT.jar`
- [ ] 环境变量（Variables）- 所有数据库和邮件配置
- [ ] Generate Domain（生成公共域名）

### 可选配置：
- [ ] Custom Build Command（如果 railway.json 不工作）
- [ ] Healthcheck Path

---

## 🚀 配置完成后

1. **保存所有设置**
2. **生成公共域名**（在 Networking 部分）
3. **等待自动重新部署**
4. **查看部署日志**，确认构建成功
5. **复制公共域名**，稍后配置前端时需要

---

## ⚠️ 重要提示

1. **先添加 MySQL 数据库服务**（如果还没有）
   - 在项目主页点击 "+ New" → "Database" → "Add MySQL"
   - 然后才能使用 `${{MySQL.XXX}}` 模板变量

2. **环境变量中的模板变量**
   - `${{MySQL.MYSQLHOST}}` 等是 Railway 的模板变量
   - 只有在添加了 MySQL 服务后才会生效

3. **Start Command 很重要**
   - 如果没有设置，Railway 不知道如何启动应用
   - 必须设置为：`java -jar target/backend-1.0-SNAPSHOT.jar`

