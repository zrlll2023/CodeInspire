#!/bin/bash

set -e

echo "=========================================="
echo "  CodeInspire 部署脚本"
echo "=========================================="

PROJECT_DIR="/opt/codeinspire"
BACKUP_DIR="/opt/codeinspire-backup/$(date +%Y%m%d_%H%M%S)"

echo "[1/6] 创建备份目录..."
mkdir -p "$BACKUP_DIR"

echo "[2/6] 备份当前版本..."
if [ -d "$PROJECT_DIR" ]; then
    cp -r "$PROJECT_DIR" "$BACKUP_DIR/codeinspire"
    echo "  已备份到: $BACKUP_DIR"
else
    mkdir -p "$PROJECT_DIR"
fi

echo "[3/6] 拉取最新代码..."
cd "$PROJECT_DIR"
git pull origin main

echo "[4/6] 构建并启动服务..."
docker-compose pull
docker-compose up -d --remove-orphans

echo "[5/6] 等待服务启动..."
sleep 30

echo "[6/6] 健康检查..."
MAX_RETRIES=10
RETRY_COUNT=0

while [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
    if curl -sf http://localhost:8080/api/health > /dev/null 2>&1; then
        echo ""
        echo "✅ 部署成功！服务已正常运行"
        echo "   API地址: http://localhost:8080"
        echo "   前端地址: http://localhost:80 (需配置Nginx)"
        exit 0
    fi
    
    RETRY_COUNT=$((RETRY_COUNT + 1))
    echo "  等待服务启动... ($RETRY_COUNT/$MAX_RETRIES)"
    sleep 5
done

echo ""
echo "❌ 部署失败！服务未能在预期时间内启动"
echo "   请检查日志: docker-compose logs -f"
exit 1
