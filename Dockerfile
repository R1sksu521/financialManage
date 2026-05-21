FROM maven:3.8-openjdk-8 AS build
WORKDIR /app
COPY pom.xml .
COPY src/ src/
COPY WebRoot/ WebRoot/
RUN mvn package -DskipTests

FROM tomcat:9.0-jdk8
RUN apt-get update && apt-get install -y mariadb-client && rm -rf /var/lib/apt/lists/*
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war
COPY entrypoint.sh /entrypoint.sh
COPY init.sql /init.sql
RUN chmod +x /entrypoint.sh
EXPOSE 8080
ENTRYPOINT ["/entrypoint.sh"]
