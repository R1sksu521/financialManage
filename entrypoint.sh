#!/bin/bash
set -e

if [ -n "$MYSQL_URL" ]; then
  DB_HOST=$(echo "$MYSQL_URL" | sed 's|.*@||;s|:.*||')
  DB_PORT=$(echo "$MYSQL_URL" | sed 's|.*:||;s|/.*||')
  DB_USER=$(echo "$MYSQL_URL" | sed 's|.*://||;s|:.*||')
  DB_PASS=$(echo "$MYSQL_URL" | sed 's|.*://.*:||;s|@.*||')
  DB_NAME=$(echo "$MYSQL_URL" | sed 's|.*/||')
else
  DB_HOST=${MYSQLHOST:-localhost}
  DB_PORT=${MYSQLPORT:-3306}
  DB_NAME=${MYSQLDATABASE:-financialmanage}
  DB_USER=${MYSQLUSER:-root}
  DB_PASS=${MYSQLPASSWORD:-root}
fi

cat > /usr/local/tomcat/webapps/ROOT/WEB-INF/classes/db.properties << EOF
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true
jdbc.username=${DB_USER}
jdbc.password=${DB_PASS}
EOF

# 把 localhost 日志重定向到 stdout，这样 Railway 日志里就能看到
rm -f /usr/local/tomcat/logs/localhost.*.log
ln -sf /dev/stdout /usr/local/tomcat/logs/localhost.$(date +%Y-%m-%d).log

exec catalina.sh run
