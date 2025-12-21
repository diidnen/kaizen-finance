# Testimonials Management System

## 概述
客户评价管理系统，允许管理员添加、编辑和删除客户评价，并在首页展示给用户查看。

## 功能特性

### 用户功能
- 在首页查看客户评价
- 支持左右滑动浏览评价
- 响应式设计，移动端友好

### 管理员功能
- 添加新评价
- 编辑现有评价
- 删除评价
- 管理评价显示顺序
- 控制评价显示状态

## 技术实现

### 后端
- **Testimonial.java**: 评价实体类
- **TestimonialRepository.java**: 数据访问层
- **TestimonialController.java**: REST API控制器

### 前端
- **home.vue**: 首页评价展示
- **manager.vue**: 管理员评价管理

## API端点

### 公开端点
- `GET /api/testimonials/public` - 获取所有活跃的评价

### 管理员端点
- `GET /api/testimonials/admin` - 获取所有评价
- `POST /api/testimonials` - 创建新评价
- `PUT /api/testimonials/{id}` - 更新评价
- `DELETE /api/testimonials/{id}` - 删除评价

## 数据结构

### 评价字段
- `id`: 评价ID
- `customerName`: 客户姓名
- `companyName`: 公司名称
- `testimonial`: 评价内容
- `avatarUrl`: 头像URL
- `createdBy`: 创建者
- `createdAt`: 创建时间
- `isActive`: 是否活跃
- `displayOrder`: 显示顺序

## 首页展示

### 设计特点
- 类似图片中的样式设计
- 卡片式布局
- 左右滑动导航
- 客户头像和公司信息
- 响应式设计

### 交互功能
- 左右箭头导航
- 平滑滑动动画
- 图片加载错误处理
- 移动端触摸支持

## 管理员界面

### 功能特点
- 评价列表展示
- 添加/编辑/删除操作
- 状态管理
- 显示顺序控制

## 安装和配置

### 1. 创建数据库表
```sql
-- 运行 create_testimonials_table.sql 脚本
```

### 2. 插入示例数据
```sql
-- 脚本中包含示例评价数据
```

## 使用方法

### 用户查看
1. 访问首页
2. 滚动到评价区域
3. 使用左右箭头浏览评价

### 管理员管理
1. 登录为chuhan用户
2. 访问Manager页面
3. 在Testimonials Management区域管理评价

## 样式特点

### 首页评价区域
- 浅灰色背景 (#f8f9fa)
- 白色卡片设计
- 圆角边框和阴影
- 客户头像圆形显示
- 引用符号装饰

### 响应式设计
- 桌面端：3个评价卡片并排
- 移动端：1个评价卡片
- 自适应导航按钮

## 安全特性
- 管理员权限验证
- 输入数据验证
- 图片URL安全检查
- 错误处理机制

## 更新日志
- v1.0.0: 初始版本，支持基本的评价管理功能