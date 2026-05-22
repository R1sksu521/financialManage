#!/bin/bash
set -e

DB_HOST=${MYSQLHOST:-localhost}
DB_PORT=${MYSQLPORT:-3306}
DB_NAME=${MYSQLDATABASE:-financialmanage}
DB_USER=${MYSQLUSER:-root}
DB_PASS=${MYSQLPASSWORD:-root}

cat > src/db.properties << EOF
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useUnicode=true&characterEncoding=utf-8&useSSL=false
jdbc.username=${DB_USER}
jdbc.password=${DB_PASS}
EOF

echo "DB: ${DB_HOST}:${DB_PORT}/${DB_NAME}"
exec mvn tomcat7:run
