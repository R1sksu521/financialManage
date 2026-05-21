FROM maven:3.8-openjdk-8 AS build
WORKDIR /app
COPY pom.xml .
COPY src/ src/
COPY WebRoot/ WebRoot/
RUN mvn package -DskipTests

FROM tomcat:9.0-jdk8
RUN apt-get update && apt-get install -y unzip && rm -rf /var/lib/apt/lists/*
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/*.war /tmp/app.war
# 在构建时解压 WAR（避免运行时 entrypoint.sh 找不到目录）
RUN mkdir -p /usr/local/tomcat/webapps/ROOT && \
    unzip -q /tmp/app.war -d /usr/local/tomcat/webapps/ROOT && \
    rm /tmp/app.war
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
EXPOSE 8080
ENTRYPOINT ["/entrypoint.sh"]
