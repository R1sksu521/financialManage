#!/bin/bash
# 如果有环境变量就用环境变量，否则用默认值
DB_HOST=${MYSQLHOST:-${DB_HOST:-mysql}}
DB_PORT=${MYSQLPORT:-${DB_PORT:-3306}}
DB_NAME=${MYSQLDATABASE:-${DB_NAME:-financialmanage}}
DB_USER=${MYSQLUSER:-${DB_USER:-root}}
DB_PASS=${MYSQLPASSWORD:-${DB_PASS:-suPAN886}}

# 写入 db.properties
cat > /usr/local/tomcat/webapps/ROOT/WEB-INF/classes/db.properties << EOF
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useUnicode=true&characterEncoding=utf-8&useSSL=false
jdbc.username=${DB_USER}
jdbc.password=${DB_PASS}
EOF

# 启动 Tomcat
catalina.sh run
