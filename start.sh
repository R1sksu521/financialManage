#!/bin/bash
set -e

DB_HOST=${MYSQLHOST:-localhost}
DB_PORT=${MYSQLPORT:-3306}
DB_NAME=${MYSQLDATABASE:-financialmanage}
DB_USER=${MYSQLUSER:-root}
DB_PASS=${MYSQLPASSWORD:-root}

# 写入 db.properties（src 目录下，Maven 编译时会复制到 target/classes）
mkdir -p src/main/resources 2>/dev/null || true
cat > src/db.properties << EOF
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useUnicode=true&characterEncoding=utf-8&useSSL=false
jdbc.username=${DB_USER}
jdbc.password=${DB_PASS}
EOF

echo "DB: ${DB_HOST}:${DB_PORT}/${DB_NAME}"

exec mvn tomcat7:run
