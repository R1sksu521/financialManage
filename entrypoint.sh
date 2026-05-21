#!/bin/bash
# Railway 自动注入 MYSQL_URL
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

echo "DB: ${DB_HOST}:${DB_PORT}/${DB_NAME} user=${DB_USER}"

# 写入 db.properties
mkdir -p /tmp/war/WEB-INF/classes/
cat > /tmp/war/WEB-INF/classes/db.properties << EOF
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true
jdbc.username=${DB_USER}
jdbc.password=${DB_PASS}
EOF

jar uf /usr/local/tomcat/webapps/ROOT.war -C /tmp/war WEB-INF/classes/db.properties
rm -rf /tmp/war

# 自动初始化数据库表
echo "Initializing database..."
mysql -h${DB_HOST} -P${DB_PORT} -u${DB_USER} -p${DB_PASS} ${DB_NAME} < /init.sql 2>/dev/null
echo "Database init done."

catalina.sh run
