#!/bin/bash

# 测试创建用户功能的脚本

echo "Testing Create User API..."

# 首先登录获取token
echo "1. Logging in as chuhan..."
LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "chuhan",
    "password": "chuhan123"
  }')

echo "Login response: $LOGIN_RESPONSE"

# 提取token
TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
echo "Token: $TOKEN"

if [ -z "$TOKEN" ]; then
  echo "Failed to get token. Exiting."
  exit 1
fi

# 创建新用户
echo "2. Creating new user..."
CREATE_RESPONSE=$(curl -s -X POST http://localhost:8080/api/manager/users \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "username": "testuser",
    "password": "testpass123",
    "email": "test@example.com",
    "fullname": "Test User"
  }')

echo "Create user response: $CREATE_RESPONSE"

# 测试重复用户名
echo "3. Testing duplicate username..."
DUPLICATE_RESPONSE=$(curl -s -X POST http://localhost:8080/api/manager/users \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "username": "testuser",
    "password": "testpass123",
    "email": "test2@example.com",
    "fullname": "Test User 2"
  }')

echo "Duplicate username response: $DUPLICATE_RESPONSE"

echo "Test completed!"