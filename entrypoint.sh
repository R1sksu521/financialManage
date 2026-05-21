#!/bin/bash
DB_HOST=${MYSQLHOST:-mysql}
DB_PORT=${MYSQLPORT:-3306}
DB_NAME=${MYSQLDATABASE:-financialmanage}
DB_USER=${MYSQLUSER:-root}
DB_PASS=${MYSQLPASSWORD:-suPAN886}

# 创建临时目录写入新配置文件
mkdir -p /tmp/war/WEB-INF/classes/
cat > /tmp/war/WEB-INF/classes/db.properties << EOF
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useUnicode=true&characterEncoding=utf-8&useSSL=false
jdbc.username=${DB_USER}
jdbc.password=${DB_PASS}
EOF

# 把新配置注入 WAR 包
cd /tmp/war
jar uf /usr/local/tomcat/webapps/ROOT.war WEB-INF/classes/db.properties
rm -rf /tmp/war

catalina.sh run
