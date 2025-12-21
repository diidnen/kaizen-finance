# Kaizen Finance 部署指南

本文档提供了多种部署前端和后端应用的方案。

## 📋 项目架构

- **前端**: Vue.js 3 + Vite (静态文件)
- **后端**: Spring Boot 3.2.1 (Java 17)
- **数据库**: MySQL 8.0
- **邮件服务**: Brevo API

---

## 🚀 部署方案对比

### 1. **AWS (Amazon Web Services)**

#### 方案 A: EC2 + RDS (推荐用于生产环境)
- **前端**: EC2 + Nginx
- **后端**: EC2 + Spring Boot
- **数据库**: RDS MySQL
- **优点**: 完全控制，可扩展，适合生产环境
- **成本**: 中等（约 $50-200/月）
- **难度**: ⭐⭐⭐⭐

#### 方案 B: Elastic Beanstalk
- **后端**: Elastic Beanstalk (自动部署 Spring Boot)
- **前端**: S3 + CloudFront
- **数据库**: RDS MySQL
- **优点**: 自动化部署，易于管理
- **成本**: 中等（约 $30-150/月）
- **难度**: ⭐⭐⭐

#### 方案 C: ECS/Fargate (容器化)
- **前端**: ECS Fargate + Nginx
- **后端**: ECS Fargate + Spring Boot
- **数据库**: RDS MySQL
- **优点**: 容器化，易于扩展
- **成本**: 中等（约 $40-180/月）
- **难度**: ⭐⭐⭐⭐

---

### 2. **其他云服务提供商**

#### **Vercel (前端) + Railway/Render (后端)** ⭐ 推荐
- **前端**: Vercel (免费套餐可用)
- **后端**: Railway 或 Render (免费套餐可用)
- **数据库**: Railway/Render 提供的 MySQL
- **优点**: 
  - 免费套餐可用
  - 自动部署（GitHub集成）
  - 易于使用
- **成本**: 免费或 $5-20/月
- **难度**: ⭐⭐

#### **Netlify (前端) + Fly.io (后端)**
- **前端**: Netlify (免费套餐)
- **后端**: Fly.io (免费套餐)
- **数据库**: PlanetScale 或 Supabase (免费套餐)
- **优点**: 免费套餐，易于部署
- **成本**: 免费或 $5-15/月
- **难度**: ⭐⭐

#### **DigitalOcean App Platform**
- **前端 + 后端**: 统一平台
- **数据库**: DigitalOcean Managed Database
- **优点**: 简单，一键部署
- **成本**: $12-25/月
- **难度**: ⭐⭐

#### **Heroku**
- **前端**: Heroku Static Site
- **后端**: Heroku Dyno
- **数据库**: Heroku Postgres (需迁移到PostgreSQL)
- **优点**: 简单易用
- **缺点**: 已停止免费套餐
- **成本**: $7-25/月
- **难度**: ⭐⭐

#### **Azure (Microsoft)**
- **前端**: Azure Static Web Apps
- **后端**: Azure App Service
- **数据库**: Azure Database for MySQL
- **优点**: 企业级支持
- **成本**: 中等（约 $30-150/月）
- **难度**: ⭐⭐⭐

#### **Google Cloud Platform (GCP)**
- **前端**: Cloud Storage + Cloud CDN
- **后端**: Cloud Run 或 App Engine
- **数据库**: Cloud SQL for MySQL
- **优点**: 强大的基础设施
- **成本**: 中等（约 $30-150/月）
- **难度**: ⭐⭐⭐

---

### 3. **传统 VPS 提供商**

#### **DigitalOcean Droplets**
- **优点**: 简单，价格透明
- **成本**: $6-12/月 (基础配置)
- **难度**: ⭐⭐⭐

#### **Linode / Akamai**
- **优点**: 性能好，价格合理
- **成本**: $5-12/月
- **难度**: ⭐⭐⭐

#### **Vultr**
- **优点**: 全球节点多
- **成本**: $2.50-6/月
- **难度**: ⭐⭐⭐

#### **Hetzner**
- **优点**: 欧洲服务器，价格便宜
- **成本**: €4-8/月
- **难度**: ⭐⭐⭐

---

## 🎯 推荐方案（按需求）

### 预算有限 / 学习项目
**Vercel (前端) + Railway (后端)**
- 免费套餐可用
- 自动部署
- 易于使用

### 小型商业项目
**DigitalOcean App Platform**
- 统一平台
- 价格合理
- 易于管理

### 中大型项目
**AWS EC2 + RDS**
- 完全控制
- 可扩展性强
- 适合生产环境

---

## 📝 快速部署步骤（以 Vercel + Railway 为例）

### 前端部署到 Vercel

1. **准备构建**
   ```bash
   cd kaizen-finance
   npm run build
   ```

2. **部署到 Vercel**
   - 访问 https://vercel.com
   - 使用 GitHub 登录
   - 导入项目
   - 设置构建命令: `npm run build`
   - 设置输出目录: `dist`
   - 添加环境变量（如需要）

### 后端部署到 Railway

1. **准备项目**
   - 确保 `application.properties` 使用环境变量
   - 创建 `railway.json` 配置文件

2. **部署到 Railway**
   - 访问 https://railway.app
   - 使用 GitHub 登录
   - 新建项目，选择 "Deploy from GitHub repo"
   - 选择后端项目
   - 添加 MySQL 数据库服务
   - 配置环境变量

---

## 🐳 Docker 部署（通用方案）

### 创建 Dockerfile

#### 前端 Dockerfile
```dockerfile
# 构建阶段
FROM node:18-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# 生产阶段
FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

#### 后端 Dockerfile
```dockerfile
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Docker Compose
```yaml
version: '3.8'
services:
  frontend:
    build: ./kaizen-finance
    ports:
      - "80:80"
    depends_on:
      - backend
  
  backend:
    build: ./kaizen-finance-backend/backend
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/kaizenfinance
      - SPRING_DATASOURCE_USERNAME=root
      - SPRING_DATASOURCE_PASSWORD=yourpassword
    depends_on:
      - db
  
  db:
    image: mysql:8.0
    environment:
      - MYSQL_ROOT_PASSWORD=yourpassword
      - MYSQL_DATABASE=kaizenfinance
    volumes:
      - mysql_data:/var/lib/mysql

volumes:
  mysql_data:
```

---

## ⚙️ 环境变量配置

### 前端环境变量
创建 `.env.production`:
```env
VITE_API_BASE_URL=https://your-backend-domain.com/api
```

### 后端环境变量
在部署平台设置：
```env
SPRING_DATASOURCE_URL=jdbc:mysql://your-db-host:3306/kaizenfinance
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password
EMAIL_BREVO_API_KEY=your_brevo_api_key
EMAIL_BREVO_FROM_EMAIL=info@kaizensolution.co.uk
EMAIL_SERVICE_PROVIDER=brevo
```

---

## 🔒 安全注意事项

1. **不要提交敏感信息**
   - 使用环境变量存储密码和API密钥
   - 添加 `.env` 到 `.gitignore`

2. **数据库安全**
   - 使用强密码
   - 限制数据库访问IP
   - 启用SSL连接

3. **HTTPS**
   - 所有部署都应使用HTTPS
   - 大多数平台自动提供SSL证书

4. **CORS配置**
   - 更新 `CorsConfig.java` 中的允许来源
   - 只允许生产域名

---

## 📊 成本对比表

| 方案 | 月成本 | 免费套餐 | 难度 | 推荐度 |
|------|--------|----------|------|--------|
| Vercel + Railway | $0-20 | ✅ | ⭐⭐ | ⭐⭐⭐⭐⭐ |
| DigitalOcean App Platform | $12-25 | ❌ | ⭐⭐ | ⭐⭐⭐⭐ |
| AWS EC2 + RDS | $50-200 | ❌ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
| Netlify + Fly.io | $0-15 | ✅ | ⭐⭐ | ⭐⭐⭐⭐ |
| Heroku | $7-25 | ❌ | ⭐⭐ | ⭐⭐⭐ |
| Azure | $30-150 | ❌ | ⭐⭐⭐ | ⭐⭐⭐ |
| GCP | $30-150 | ❌ | ⭐⭐⭐ | ⭐⭐⭐ |
| VPS (DigitalOcean) | $6-12 | ❌ | ⭐⭐⭐ | ⭐⭐⭐ |

---

## 🛠️ 下一步

1. 选择适合的部署方案
2. 准备环境变量
3. 配置数据库
4. 更新 CORS 设置
5. 部署并测试

需要我帮您创建具体的部署配置文件吗？

