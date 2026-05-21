#!/bin/bash
set -e  # 任何命令失败就退出

if [ -n "$MYSQL_URL" ]; then
  DB_HOST=$(echo "$MYSQL_URL" | sed 's|.*@||;s|:.*||')
  DB_PORT=$(echo "$MYSQL_URL" | sed 's|.*:||;s|/.*||')
  DB_USER=$(echo "$MYSQL_URL" | sed 's|.*://||;s|:.*||')
  DB_PASS=$(echo "$MYSQL_URL" | sed 's|.*://.*:||;s|@.*||')
  DB_NAME=$(echo "$MYSQL_URL" | sed 's|.*/||')
else
  DB_HOST=${MYSQL_HOST:-${MYSQLHOST:-localhost}}
  DB_PORT=${MYSQL_PORT:-${MYSQLPORT:-3306}}
  DB_NAME=${MYSQL_DATABASE:-${MYSQLDATABASE:-financialmanage}}
  DB_USER=${MYSQL_USER:-${MYSQLUSER:-root}}
  DB_PASS=${MYSQL_PASSWORD:-${MYSQLPASSWORD:-root}}
fi

echo "============================================"
echo "DB_HOST=${DB_HOST}"
echo "DB_PORT=${DB_PORT}"
echo "DB_NAME=${DB_NAME}"
echo "DB_USER=${DB_USER}"
echo "MYSQL_URL env=${MYSQL_URL:-NOT SET}"
echo "============================================"

# 写入新配置
mkdir -p /tmp/war/WEB-INF/classes/
cat > /tmp/war/WEB-INF/classes/db.properties << EOF
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true
jdbc.username=${DB_USER}
jdbc.password=${DB_PASS}
EOF

echo "=== db.properties content ==="
cat /tmp/war/WEB-INF/classes/db.properties
echo "============================="

# 更新 WAR
echo "Updating WAR..."
jar uf /usr/local/tomcat/webapps/ROOT.war -C /tmp/war WEB-INF/classes/db.properties
echo "WAR updated OK"

# 验证 WAR 里确实有 db.properties
echo "=== Verifying WAR content ==="
jar tf /usr/local/tomcat/webapps/ROOT.war | grep db.properties
echo "============================="

rm -rf /tmp/war

# 初始化数据库
echo "Running init.sql..."
mysql -h${DB_HOST} -P${DB_PORT} -u${DB_USER} -p${DB_PASS} ${DB_NAME} < /init.sql
echo "init.sql done"

exec catalina.sh run
