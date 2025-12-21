# Railway JAR 文件找不到 - 修复指南

## ❌ 错误信息

```
Error: Unable to access jarfile target/backend-1.0-SNAPSHOT.jar
```

## 🔍 问题原因

Railway 找不到 JAR 文件，可能的原因：
1. JAR 文件名不对（实际文件名可能不同）
2. 构建没有成功生成 JAR 文件
3. Start Command 路径不对

## ✅ 解决方案

### 方法 1: 检查实际的 JAR 文件名

1. **查看 Build Logs**（不是 Deploy Logs）
   - 在 Railway 部署页面
   - 点击 "Build Logs" 标签
   - 查找类似这样的信息：
     ```
     [INFO] Building jar: .../target/backend-1.0-SNAPSHOT.jar
     ```
   - 或者查找实际生成的 JAR 文件名

2. **如果文件名不同**，更新 Start Command：
   - 在 Settings → Deploy → Start Command
   - 使用实际的 JAR 文件名
   - 例如：`java -jar target/backend-1.0-SNAPSHOT.jar`

### 方法 2: 使用通配符（推荐）

如果 JAR 文件名可能变化，使用通配符：

**在 Railway Settings → Deploy → Start Command**：
```
java -jar target/*.jar
```

这会自动找到 target 目录下的第一个 JAR 文件。

### 方法 3: 检查构建是否成功

1. **查看 Build Logs**：
   - 点击 "Build Logs" 标签
   - 确认看到：
     ```
     [INFO] BUILD SUCCESS
     [INFO] Building jar: ...
     ```

2. **如果构建失败**：
   - 查看构建错误信息
   - 可能是依赖下载失败
   - 可能是编译错误

### 方法 4: 使用 Spring Boot Maven Plugin 的默认方式

Spring Boot Maven Plugin 生成的 JAR 文件名格式通常是：
- `backend-1.0-SNAPSHOT.jar`（如果 pom.xml 中 artifactId 是 backend）

**检查 pom.xml**：
```xml
<artifactId>backend</artifactId>
<version>1.0-SNAPSHOT</version>
```

所以 JAR 文件名应该是：`backend-1.0-SNAPSHOT.jar`

## 🔧 快速修复步骤

### 步骤 1: 查看 Build Logs

1. 在 Railway 部署页面
2. 点击 "Build Logs" 标签
3. 查找 "Building jar" 或 "BUILD SUCCESS"
4. 确认 JAR 文件是否生成

### 步骤 2: 更新 Start Command

**选项 A：使用通配符（最简单）**
```
java -jar target/*.jar
```

**选项 B：使用完整路径（如果知道确切文件名）**
```
java -jar target/backend-1.0-SNAPSHOT.jar
```

### 步骤 3: 重新部署

1. 保存设置
2. Railway 会自动重新部署
3. 或者手动触发 Redeploy

## 📋 检查清单

- [ ] 查看 Build Logs 确认构建成功
- [ ] 确认 JAR 文件已生成
- [ ] 更新 Start Command（使用通配符或正确文件名）
- [ ] 确认 Root Directory 正确：`kaizen-finance-backend/backend`
- [ ] 重新部署并查看日志

## 💡 推荐配置

**Start Command**（在 Railway Settings → Deploy）：
```
java -jar target/*.jar
```

这个命令会：
- 自动找到 target 目录下的 JAR 文件
- 不需要知道确切的文件名
- 更灵活，即使文件名变化也能工作

