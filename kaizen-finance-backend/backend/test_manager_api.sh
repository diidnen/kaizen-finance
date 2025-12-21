#!/bin/bash

# Manager API 测试脚本
# 使用方法: ./test_manager_api.sh

BASE_URL="http://localhost:8080"
TOKEN=""

echo "=== Manager API 测试脚本 ==="
echo ""

# 1. 登录获取token
echo "1. 测试登录..."
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/api/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"chuhan","password":"chuhan123"}')

echo "登录响应: $LOGIN_RESPONSE"

# 提取token
TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

if [ -z "$TOKEN" ]; then
    echo "❌ 登录失败，无法获取token"
    exit 1
fi

echo "✅ 登录成功，Token: ${TOKEN:0:20}..."
echo ""

# 2. 测试获取所有用户
echo "2. 测试获取所有用户..."
USERS_RESPONSE=$(curl -s -X GET "$BASE_URL/api/manager/users" \
  -H "Authorization: Bearer $TOKEN")

echo "获取用户响应: $USERS_RESPONSE"
echo ""

# 3. 测试删除用户（需要先获取一个用户ID）
echo "3. 测试删除用户..."
# 这里需要根据实际情况调整用户ID
USER_ID=1
DELETE_RESPONSE=$(curl -s -X DELETE "$BASE_URL/api/manager/users/$USER_ID" \
  -H "Authorization: Bearer $TOKEN")

echo "删除用户响应: $DELETE_RESPONSE"
echo ""

# 4. 测试重置密码
echo "4. 测试重置密码..."
RESET_RESPONSE=$(curl -s -X POST "$BASE_URL/api/manager/users/$USER_ID/reset-password" \
  -H "Authorization: Bearer $TOKEN")

echo "重置密码响应: $RESET_RESPONSE"
echo ""

echo "=== 测试完成 ===" 