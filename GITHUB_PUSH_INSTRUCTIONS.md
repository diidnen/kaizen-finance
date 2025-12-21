# GitHub 推送说明

## ⚠️ 当前问题

GitHub 检测到代码中包含 API Key，阻止了推送。这是 GitHub 的安全保护功能。

## ✅ 解决方案

### 方法 1: 使用 GitHub 链接允许推送（推荐）

1. **访问以下链接**（需要登录 GitHub）：
   ```
   https://github.com/diidnen/kaizen-finance/security/secret-scanning/unblock-secret/378yPAre13CIWOPwygHl14xiJ9P
   ```

2. **在 GitHub 页面上**：
   - 点击 "Allow secret" 或 "Unblock secret"
   - 确认您了解风险
   - 这会允许这次推送

3. **然后运行**：
   ```bash
   git push -u origin main
   ```

### 方法 2: 在 GitHub 仓库设置中禁用推送保护（临时）

1. 访问仓库设置：https://github.com/diidnen/kaizen-finance/settings
2. 进入 "Code security and analysis"
3. 找到 "Push protection" 设置
4. 临时禁用（推送后建议重新启用）

### 方法 3: 创建新分支推送

如果主分支有保护，可以推送到新分支：

```bash
git checkout -b deploy
git push -u origin deploy
```

然后在 GitHub 上创建 Pull Request 合并到 main。

## 🔒 推送后的安全建议

推送成功后，建议：

1. **立即撤销 API Key**（如果已泄露）：
   - 登录 Brevo 账号
   - 生成新的 API Key
   - 更新 Railway 环境变量

2. **检查 .gitignore**：
   - 确保 `target/` 目录被忽略
   - 确保 `application.properties` 不会被提交（使用环境变量）

3. **重新启用推送保护**（如果禁用了）

## 📝 当前状态

- ✅ 代码已准备好推送
- ✅ API Key 已从当前文件中移除
- ⚠️ 历史提交中仍包含 API Key（需要 GitHub 允许或重写历史）

