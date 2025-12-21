# Railway 构建失败 - 故障排除指南

## 🔍 第一步：查看详细错误日志

1. **在 Railway 项目页面**
   - 点击 "kaizen-finance" 服务（显示 Build failed 的那个）
   - 点击 "Deployments" 标签（在顶部导航栏）
   - 点击失败的部署（红色标记的那个）
   - 点击 "View Logs" 或 "View Build Logs"
   - **复制完整的错误信息**

## 📋 常见错误和解决方案

### ❌ 错误 1: "Could not find or load main class"

**原因**：Railway 找不到主类或 JAR 文件路径错误

**解决方案**：
1. 在 Railway 服务设置中：
   - Settings → Deploy → Start Command
   - 设置为：`java -jar target/backend-1.0-SNAPSHOT.jar`

2. 或者检查 JAR 文件实际名称：
   - 查看构建日志中的实际 JAR 文件名
   - 可能是 `backend-1.0-SNAPSHOT.jar` 或其他名称

### ❌ 错误 2: "Maven build failed"

**原因**：Maven 构建过程中出错

**解决方案**：
1. 检查 Java 版本（需要 Java 17）
2. 检查依赖下载是否成功
3. 查看构建日志中的具体错误

### ❌ 错误 3: "Port already in use" 或端口问题

**原因**：端口配置不正确

**解决方案**：
- ✅ 已更新 `application.properties` 支持 Railway 的 PORT 环境变量
- Railway 会自动设置 `PORT` 环境变量
- 代码已更新为：`server.port=${PORT:8080}`

### ❌ 错误 4: "Root directory not found"

**原因**：Railway 找不到后端代码

**解决方案**：
1. 在 Railway 服务设置中：
   - Settings → Source → Root Directory
   - 设置为：`kaizen-finance-backend/backend`

### ❌ 错误 5: "Build command failed"

**原因**：构建命令执行失败

**解决方案**：
- ✅ 已创建 `railway.json` 配置文件
- 如果还不工作，在 Settings → Build 中手动设置：
  - Build Command: `mvn clean package -DskipTests`
  - Start Command: `java -jar target/backend-1.0-SNAPSHOT.jar`

## 🔧 快速修复检查清单

- [ ] 检查 Root Directory 是否正确：`kaizen-finance-backend/backend`
- [ ] 检查 Build Command：`mvn clean package -DskipTests`
- [ ] 检查 Start Command：`java -jar target/backend-1.0-SNAPSHOT.jar`
- [ ] 检查 Java 版本：应该是 17
- [ ] 查看构建日志中的具体错误信息

## 📝 需要帮助？

**请提供以下信息**：
1. Railway 构建日志中的完整错误信息（从 "View Logs" 复制）
2. Railway 服务设置中的：
   - Root Directory
   - Build Command
   - Start Command

这样我可以更精确地帮您解决问题！

