@echo off
chcp 65001 >nul
echo ==========================================
echo   CodeInspire 开发环境启动脚本
echo ==========================================

echo.
echo [1/5] 检查 Java 环境...
java -version >nul 2>&1
if errorlevel 1 (
    echo [错误] 未检测到Java，请安装 JDK 17+
    pause
    exit /b 1
)
echo       Java 环境正常

echo.
echo [2/5] 检查 Maven 环境...
mvn -version >nul 2>&1
if errorlevel 1 (
    echo [错误] 未检测到Maven，请安装 Maven
    pause
    exit /b 1
)
echo       Maven 环境正常

echo.
echo [3/5] 检查 MySQL 连接...
mysqladmin ping -h localhost -u root -proot >nul 2>&1
if errorlevel 1 (
    echo [警告] MySQL 可能未启动或连接配置不正确
) else (
    echo       MySQL 连接正常
)

echo.
echo [4/5] 检查 Redis 连接...
redis-cli ping >nul 2>&1
if errorlevel 1 (
    echo [警告] Redis 可能未启动
) else (
    echo       Redis 连接正常
)

echo.
echo [5/5] 启动后端服务...
cd /d "%~dp0..\codeinspire-backend"
call mvn spring-boot:run

pause
