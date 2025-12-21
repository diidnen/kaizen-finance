# 邮件服务配置指南

本项目支持多种邮件发送服务，您可以根据需要选择使用。

## 支持的邮件服务

### 1. Gmail SMTP（当前默认）
- **免费套餐**: 无限制（但每天有发送限制）
- **优点**: 免费，易于配置
- **缺点**: 需要应用专用密码，可能被标记为垃圾邮件

### 2. SendGrid（推荐）
- **注册地址**: https://sendgrid.com/
- **免费套餐**: 100 封/天
- **优点**: 
  - 专业的邮件服务
  - 良好的送达率
  - 详细的发送统计
- **配置步骤**:
  1. 注册 SendGrid 账号
  2. 创建 API Key
  3. 在 `application.properties` 中配置：
     ```
     email.service.provider=sendgrid
     email.sendgrid.api.key=YOUR_SENDGRID_API_KEY
     email.sendgrid.from.email=info@kaizensolution.co.uk
     email.sendgrid.from.name=Kaizen Solution
     ```

### 3. Brevo (原 Sendinblue)（推荐）
- **注册地址**: https://www.brevo.com/
- **免费套餐**: 300 封/天
- **优点**: 
  - 免费额度较高
  - 易于使用
  - 良好的API文档
- **配置步骤**:
  1. 注册 Brevo 账号
  2. 创建 API Key
  3. 在 `application.properties` 中配置：
     ```
     email.service.provider=brevo
     email.brevo.api.key=YOUR_BREVO_API_KEY
     email.brevo.from.email=info@kaizensolution.co.uk
     email.brevo.from.name=Kaizen Solution
     ```

### 4. Mailgun
- **注册地址**: https://www.mailgun.com/
- **免费套餐**: 5000 封/月（前3个月），之后 1000 封/月
- **优点**: 
  - 免费额度高（初期）
  - 专业的邮件服务
- **配置步骤**:
  1. 注册 Mailgun 账号
  2. 验证域名
  3. 获取 API Key 和 Domain
  4. 在 `application.properties` 中配置：
     ```
     email.service.provider=mailgun
     email.mailgun.api.key=YOUR_MAILGUN_API_KEY
     email.mailgun.domain=YOUR_MAILGUN_DOMAIN
     email.mailgun.from.email=info@kaizensolution.co.uk
     email.mailgun.from.name=Kaizen Solution
     ```

## 配置方法

### 步骤 1: 选择邮件服务
在 `application.properties` 中设置：
```properties
email.service.provider=sendgrid  # 或 brevo, mailgun, gmail
```

### 步骤 2: 配置对应的 API Key
根据选择的服务，配置相应的 API Key 和其他参数。

### 步骤 3: 重启应用
重启 Spring Boot 应用以使配置生效。

## 推荐方案

**对于生产环境，推荐使用 Brevo 或 SendGrid**：
- **Brevo**: 如果每天发送量在 300 封以内，免费套餐足够使用
- **SendGrid**: 如果需要更多功能（如邮件模板、统计分析等）

**对于开发/测试环境**：
- 可以继续使用 Gmail SMTP
- 或者使用 Mailgun 的免费套餐（初期 5000 封/月）

## 注意事项

1. **API Key 安全**: 请勿将 API Key 提交到版本控制系统
2. **域名验证**: 使用 Mailgun 需要验证域名
3. **发送限制**: 注意各服务的免费套餐限制
4. **送达率**: API 服务的送达率通常比 SMTP 更高

## 故障排除

如果邮件发送失败，请检查：
1. API Key 是否正确配置
2. 发件邮箱地址是否正确
3. 网络连接是否正常
4. 查看应用日志中的错误信息

