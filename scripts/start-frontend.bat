@echo off
chcp 65001 >nul
echo ==========================================
echo   CodeInspire 前端开发环境
echo ==========================================

echo.
echo [1/3] 检查 Node.js 环境...
node -v >nul 2>&1
if errorlevel 1 (
    echo [错误] 未检测到Node.js，请安装 Node.js 20+
    pause
    exit /b 1
)
echo       Node.js 环境正常

echo.
echo [2/3] 检查依赖是否安装...
if not exist "..\codeinspire-frontend\node_modules" (
    echo       正在安装依赖...
    cd /d "%~dp0..\codeinspire-frontend"
    call npm install
) else (
    echo       依赖已安装
)

echo.
echo [3/3] 启动前端开发服务器...
cd /d "%~dp0..\codeinspire-frontend"
call npm run dev

pause
