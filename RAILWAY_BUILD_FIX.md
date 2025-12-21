# Railway 构建失败解决方案

## 🔍 如何查看错误日志

1. **在 Railway 项目页面**
   - 点击 "kaizen-finance" 服务
   - 点击 "Deployments" 标签
   - 点击失败的部署
   - 点击 "View Logs" 查看详细错误信息

2. **常见错误和解决方案**

---

## ❌ 常见错误 1: Maven 构建失败

### 错误信息
```
[ERROR] Failed to execute goal ...
[ERROR] Compilation failure
```

### 解决方案

**检查 Java 版本**：
- Railway 需要 Java 17
- 在 Railway 服务设置中检查 Java 版本

**添加 railway.json 配置文件**：

在 `kaizen-finance-backend/backend/` 目录创建 `railway.json`：

```json
{
  "$schema": "https://railway.app/railway.schema.json",
  "build": {
    "builder": "NIXPACKS",
    "buildCommand": "mvn clean package -DskipTests"
  },
  "deploy": {
    "startCommand": "java -jar target/*.jar",
    "restartPolicyType": "ON_FAILURE",
    "restartPolicyMaxRetries": 10
  }
}
```

---

## ❌ 常见错误 2: 找不到主类

### 错误信息
```
Error: Could not find or load main class
```

### 解决方案

**检查主类名称**：
- 主类应该是：`com.loginapp.backendrunning`
- 确保 `pom.xml` 中配置正确

**添加启动命令**：
在 Railway 服务设置中：
- Settings → Deploy → Start Command
- 设置为：`java -jar target/backend-1.0-SNAPSHOT.jar`

---

## ❌ 常见错误 3: 依赖下载失败

### 错误信息
```
[ERROR] Failed to download ...
```

### 解决方案

**检查网络连接**：
- Railway 需要访问 Maven 中央仓库
- 如果在中国，可能需要配置镜像

**添加 Maven 镜像配置**（如果需要）：

在 `pom.xml` 中添加：

```xml
<repositories>
    <repository>
        <id>central</id>
        <url>https://repo1.maven.org/maven2</url>
    </repository>
</repositories>
```

---

## ❌ 常见错误 4: 端口配置错误

### 错误信息
```
Port already in use
```

### 解决方案

**确保使用环境变量配置端口**：
- Railway 会自动设置 `PORT` 环境变量
- 确保 `application.properties` 支持：`server.port=${PORT:8080}`

---

## ✅ 推荐的 Railway 配置

### 1. 创建 railway.json

在 `kaizen-finance-backend/backend/railway.json`：

```json
{
  "$schema": "https://railway.app/railway.schema.json",
  "build": {
    "builder": "NIXPACKS",
    "buildCommand": "mvn clean package -DskipTests"
  },
  "deploy": {
    "startCommand": "java -jar target/backend-1.0-SNAPSHOT.jar",
    "restartPolicyType": "ON_FAILURE",
    "restartPolicyMaxRetries": 10
  }
}
```

### 2. 更新 application.properties

确保端口配置支持环境变量：

```properties
server.port=${PORT:8080}
server.address=0.0.0.0
```

### 3. Railway 服务设置

在 Railway 服务设置中：
- **Root Directory**: `kaizen-finance-backend/backend`
- **Build Command**: `mvn clean package -DskipTests`（如果 railway.json 不工作）
- **Start Command**: `java -jar target/backend-1.0-SNAPSHOT.jar`

---

## 🔧 快速修复步骤

1. **查看日志**：在 Railway 中点击失败的部署 → View Logs
2. **复制错误信息**：找到具体的错误信息
3. **根据错误类型**：参考上面的解决方案
4. **重新部署**：修复后，Railway 会自动重新部署

---

## 📝 需要帮助？

请提供 Railway 日志中的具体错误信息，我可以帮您精确诊断问题。

