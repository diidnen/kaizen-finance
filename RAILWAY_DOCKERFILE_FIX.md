# Railway Dockerfile 启动命令修复

## 🔍 问题分析

从构建日志可以看到：
- Railway 检测到了 `Dockerfile`，使用了 Docker 构建
- Dockerfile 中 JAR 文件被复制为：`app.jar`
- 但 Start Command 还在找：`target/backend-1.0-SNAPSHOT.jar`

## ✅ 解决方案

### 方法 1: 更新 Start Command（推荐）

如果使用了 Dockerfile，JAR 文件在容器中的路径是 `app.jar`。

**在 Railway Settings → Deploy → Start Command**：
```
java -jar app.jar
```

### 方法 2: 检查 Dockerfile

查看 `kaizen-finance-backend/backend/Dockerfile`，确认 JAR 文件被复制为什么名称。

通常 Dockerfile 中会有：
```dockerfile
COPY --from=builder /app/target/*.jar app.jar
```

所以启动命令应该是：`java -jar app.jar`

## 🔧 立即修复

1. **在 Railway 服务设置中**：
   - Settings → Deploy → Custom Start Command
   - 更新为：`java -jar app.jar`
   - 点击 "Update"

2. **或者删除 Dockerfile**（如果想用 Maven 直接构建）：
   - 如果不想用 Docker，可以删除 Dockerfile
   - Railway 会使用 Maven 直接构建
   - 然后 Start Command 用：`java -jar target/*.jar`

## 📋 检查清单

- [ ] 确认是否使用了 Dockerfile
- [ ] 查看 Dockerfile 中 JAR 文件的名称
- [ ] 更新 Start Command 为正确的 JAR 文件名
- [ ] 重新部署

