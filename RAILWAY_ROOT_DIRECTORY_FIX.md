# Railway Root Directory 配置修复

## ❌ 问题

Railway 在仓库根目录查找项目，但您的后端代码在 `kaizen-finance-backend/backend/` 目录下。

错误信息显示：
```
✖ Railpack could not determine how to build the app.
The app contents that Railpack analyzed contains:
├── kaizen-finance/
├── kaizen-finance-backend/
```

Railway 看到了目录结构，但没有找到 `pom.xml`（因为它在子目录中）。

## ✅ 解决方案

### 方法 1: 在 Railway 设置中配置 Root Directory（推荐）

1. **在 Railway 项目页面**
   - 点击后端服务（显示 Build failed 的那个）
   - 点击 "Settings" 标签

2. **设置 Root Directory**
   - 找到 "Source" 部分
   - 找到 "Root Directory" 设置
   - 设置为：`kaizen-finance-backend/backend`
   - 点击 "Save"

3. **重新部署**
   - Railway 会自动重新部署
   - 或者点击 "Deployments" → "Redeploy"

### 方法 2: 使用根目录的 railway.json（已创建）

我已经在根目录创建了 `railway.json`，它会：
- 自动切换到正确的目录
- 执行构建命令
- 启动应用

Railway 应该会自动识别这个文件。

## 🔍 验证配置

部署后，日志应该显示：
- ✅ 找到 `pom.xml`
- ✅ Maven 开始构建
- ✅ 构建成功
- ✅ 应用启动

## 📝 如果还是失败

1. **确认 Root Directory 设置**
   - 必须是：`kaizen-finance-backend/backend`
   - 不是：`kaizen-finance-backend` 或 `backend`

2. **检查 railway.json**
   - 根目录应该有 `railway.json` 文件
   - 或者使用 Settings 中的 Root Directory

3. **查看新的构建日志**
   - 应该能看到 Maven 构建过程
   - 如果还是失败，复制新的错误信息

