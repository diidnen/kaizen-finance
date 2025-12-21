# Railway Root Directory 设置步骤（图文说明）

## 📍 重要：Root Directory 在服务设置中，不在项目设置中

您当前在 **Project Settings**（项目设置），但 Root Directory 需要在 **Service Settings**（服务设置）中配置。

## 🔍 如何找到 Root Directory 设置

### 步骤 1: 回到项目主页面

1. **点击左上角的项目名称**（"easygoing-healing"）
   - 或者点击浏览器后退按钮
   - 回到项目的主页面（显示服务列表的页面）

### 步骤 2: 找到后端服务

在项目主页面，您应该看到：
- **kaizen-finance** 服务（显示 Build failed 的那个）
- 可能还有 MySQL 数据库服务

### 步骤 3: 进入服务设置

1. **点击 "kaizen-finance" 服务**（不是项目名称）
2. 点击顶部导航栏的 **"Settings"** 标签
3. 在左侧边栏找到 **"Source"** 或 **"Deploy"** 部分

### 步骤 4: 设置 Root Directory

在 "Source" 或 "Deploy" 部分，您应该看到：
- **Root Directory** 输入框
- 可能显示为：`./` 或空白
- **修改为**：`kaizen-finance-backend/backend`
- 点击 **"Save"** 或 **"Update"**

## 🎯 完整路径导航

```
Railway 主页
  └─> 您的项目（easygoing-healing）
      └─> kaizen-finance 服务（点击这个！）
          └─> Settings 标签
              └─> Source 部分
                  └─> Root Directory（在这里设置）
```

## 📸 如果还是找不到

### 替代方法：在服务创建时设置

1. **删除当前服务**（如果还没配置重要内容）
   - Settings → Danger → Delete Service

2. **重新创建服务**
   - 点击 "+ New"
   - 选择 "GitHub Repo"
   - 选择仓库
   - **在创建时**，会有一个 "Root Directory" 选项
   - 设置为：`kaizen-finance-backend/backend`

### 或者：使用根目录的 railway.json（已创建）

我已经在根目录创建了 `railway.json`，它会自动处理目录切换。

如果 Railway 自动重新部署后还是失败，请：
1. 确认代码已推送（应该已经推送了）
2. 等待 Railway 自动重新部署
3. 或者手动触发 Redeploy

## 🔧 快速检查

**确认您是否在正确的位置**：
- ❌ 项目设置（Project Settings）- 这里没有 Root Directory
- ✅ 服务设置（Service Settings）- 这里有 Root Directory

**如何确认**：
- 如果左侧边栏显示 "General", "Usage", "Environments" 等 → 这是**项目设置**（错误的位置）
- 如果左侧边栏显示 "Source", "Deploy", "Variables" 等 → 这是**服务设置**（正确的位置）

## 💡 提示

如果实在找不到，可以：
1. 点击服务名称（kaizen-finance）
2. 查看是否有 "Configure" 或 "Edit" 按钮
3. 或者查看服务卡片上是否有设置图标

