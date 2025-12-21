@echo off
echo Testing Create User API...

echo 1. Logging in as chuhan...
curl -s -X POST http://localhost:8080/api/login -H "Content-Type: application/json" -d "{\"username\": \"chuhan\", \"password\": \"chuhan123\"}" > login_response.json

echo Login response:
type login_response.json

echo.
echo 2. Creating new user...
curl -s -X POST http://localhost:8080/api/manager/users -H "Content-Type: application/json" -H "Authorization: Bearer YOUR_TOKEN_HERE" -d "{\"username\": \"testuser\", \"password\": \"testpass123\", \"email\": \"test@example.com\", \"fullname\": \"Test User\"}" > create_response.json

echo Create user response:
type create_response.json

echo.
echo 3. Testing duplicate username...
curl -s -X POST http://localhost:8080/api/manager/users -H "Content-Type: application/json" -H "Authorization: Bearer YOUR_TOKEN_HERE" -d "{\"username\": \"testuser\", \"password\": \"testpass123\", \"email\": \"test2@example.com\", \"fullname\": \"Test User 2\"}" > duplicate_response.json

echo Duplicate username response:
type duplicate_response.json

echo.
echo Test completed!
pause