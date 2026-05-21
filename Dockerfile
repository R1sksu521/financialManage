FROM maven:3.8-openjdk-8 AS build
WORKDIR /app
COPY pom.xml .
COPY src/ src/
# 只编译，不打 WAR 包
RUN mvn compile -DskipTests

FROM tomcat:9.0-jdk8
RUN apt-get update && apt-get install -y mariadb-client && rm -rf /var/lib/apt/lists/*
RUN rm -rf /usr/local/tomcat/webapps/*
# 直接复制 WebRoot 到 Tomcat（不用 WAR，避免 jar 冲突）
COPY WebRoot/ /usr/local/tomcat/webapps/ROOT/
# 把 Maven 编译的 class 文件拷进去
COPY --from=build /app/target/classes/ /usr/local/tomcat/webapps/ROOT/WEB-INF/classes/
# 把新版 MySQL 驱动也拷进去（旧 jar 已被删）
COPY --from=build /root/.m2/repository/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar /usr/local/tomcat/webapps/ROOT/WEB-INF/lib/
COPY entrypoint.sh /entrypoint.sh
COPY init.sql /init.sql
RUN chmod +x /entrypoint.sh
EXPOSE 8080
ENTRYPOINT ["/entrypoint.sh"]
