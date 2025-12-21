@echo off
chcp 65001 >nul
echo === Manager API 测试脚本 ===
echo.

set BASE_URL=http://localhost:8080
set TOKEN=

echo 1. 测试登录...
for /f "tokens=*" %%i in ('curl -s -X POST "%BASE_URL%/api/login" -H "Content-Type: application/json" -d "{\"username\":\"chuhan\",\"password\":\"chuhan123\"}"') do set LOGIN_RESPONSE=%%i

echo 登录响应: %LOGIN_RESPONSE%

REM 提取token（简化版本）
for /f "tokens=2 delims=:" %%a in ('echo %LOGIN_RESPONSE% ^| findstr "token"') do set TOKEN=%%a
set TOKEN=%TOKEN:"=%
set TOKEN=%TOKEN:,=%

if "%TOKEN%"=="" (
    echo ❌ 登录失败，无法获取token
    pause
    exit /b 1
)

echo ✅ 登录成功，Token: %TOKEN:~0,20%...
echo.

echo 2. 测试获取所有用户...
for /f "tokens=*" %%i in ('curl -s -X GET "%BASE_URL%/api/manager/users" -H "Authorization: Bearer %TOKEN%"') do set USERS_RESPONSE=%%i

echo 获取用户响应: %USERS_RESPONSE%
echo.

echo 3. 测试删除用户...
set USER_ID=1
for /f "tokens=*" %%i in ('curl -s -X DELETE "%BASE_URL%/api/manager/users/%USER_ID%" -H "Authorization: Bearer %TOKEN%"') do set DELETE_RESPONSE=%%i

echo 删除用户响应: %DELETE_RESPONSE%
echo.

echo 4. 测试重置密码...
for /f "tokens=*" %%i in ('curl -s -X POST "%BASE_URL%/api/manager/users/%USER_ID%/reset-password" -H "Authorization: Bearer %TOKEN%"') do set RESET_RESPONSE=%%i

echo 重置密码响应: %RESET_RESPONSE%
echo.

echo === 测试完成 ===
pause 