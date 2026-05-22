FROM maven:3.8-openjdk-8 AS build
WORKDIR /app
COPY pom.xml .
COPY src/ src/
RUN mvn compile -DskipTests

FROM tomcat:8.5-jdk8
# Build: 20260522-1 (force fresh build)
RUN apt-get update && apt-get install -y mariadb-client && rm -rf /var/lib/apt/lists/*
RUN rm -rf /usr/local/tomcat/webapps/*
COPY WebRoot/ /usr/local/tomcat/webapps/ROOT/
COPY --from=build /app/target/classes/ /usr/local/tomcat/webapps/ROOT/WEB-INF/classes/
COPY --from=build /root/.m2/repository/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar /usr/local/tomcat/webapps/ROOT/WEB-INF/lib/
COPY backup.sql /backup.sql
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
EXPOSE 8080
ENTRYPOINT ["/entrypoint.sh"]
