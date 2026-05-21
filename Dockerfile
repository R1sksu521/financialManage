FROM maven:3.8-openjdk-8 AS build
WORKDIR /app
COPY pom.xml .
COPY src/ src/
RUN mvn compile -DskipTests

FROM tomcat:9.0-jdk8
RUN rm -rf /usr/local/tomcat/webapps/*
COPY WebRoot/ /usr/local/tomcat/webapps/ROOT/
COPY --from=build /app/target/classes/ /usr/local/tomcat/webapps/ROOT/WEB-INF/classes/
COPY --from=build /root/.m2/repository/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar /usr/local/tomcat/webapps/ROOT/WEB-INF/lib/
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
EXPOSE 8080
ENTRYPOINT ["/entrypoint.sh"]
