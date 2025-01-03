FROM eclipse-temurin:17
RUN mkdir /opt/app
COPY build/libs/*.jar /opt/app/app.jar
CMD ["java", "-Xms256m","-Xmx3G","-jar", "/opt/app/app.jar"]
