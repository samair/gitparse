FROM eclipse-temurin:17
RUN mkdir /opt/app
COPY build/libs/gitparser-1.0-SNAPSHOT-all.jar /opt/app
CMD ["java", "-jar", "/opt/app/gitparser-1.0-SNAPSHOT-all.jar"]
