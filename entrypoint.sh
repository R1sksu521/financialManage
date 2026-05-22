#!/bin/bash
set -e

DB_HOST=${MYSQLHOST:-localhost}
DB_PORT=${MYSQLPORT:-3306}
DB_NAME=${MYSQLDATABASE:-financialmanage}
DB_USER=${MYSQLUSER:-root}
DB_PASS=${MYSQLPASSWORD:-root}

cat > /usr/local/tomcat/webapps/ROOT/WEB-INF/classes/db.properties << EOF
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true
jdbc.username=${DB_USER}
jdbc.password=${DB_PASS}
EOF

echo "DB: ${DB_HOST}:${DB_PORT}/${DB_NAME}"

# Run DB migrations
echo "Running DB migrations..."
mariadb -h "${DB_HOST}" -P "${DB_PORT}" -u "${DB_USER}" -p"${DB_PASS}" "${DB_NAME}" -e "ALTER TABLE shouzhi_record MODIFY COLUMN szr_num DOUBLE;" 2>/dev/null || echo "Migration skipped (column may already be DOUBLE or table doesn't exist yet)"

exec catalina.sh run
