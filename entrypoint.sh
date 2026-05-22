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
MYSQL_CMD=""
for cmd in mariadb mysql; do
    if command -v $cmd >/dev/null 2>&1; then
        MYSQL_CMD=$cmd
        break
    fi
done
if [ -n "$MYSQL_CMD" ]; then
    echo "ALTER TABLE shouzhi_record MODIFY COLUMN szr_num DOUBLE;" | $MYSQL_CMD -h "${DB_HOST}" -P "${DB_PORT}" -u "${DB_USER}" -p"${DB_PASS}" "${DB_NAME}" || echo "Migration warning: ALTER TABLE failed, check logs"
    echo "Column type after migration:"
    echo "DESCRIBE shouzhi_record szr_num;" | $MYSQL_CMD -h "${DB_HOST}" -P "${DB_PORT}" -u "${DB_USER}" -p"${DB_PASS}" "${DB_NAME}" || true
else
    echo "No MySQL client found, skipping migrations"
fi

exec catalina.sh run
