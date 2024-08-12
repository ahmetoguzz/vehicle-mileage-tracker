
FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/carfueltracker-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

#WORKDIR /app
#COPY . .
#RUN ./mvnw clean package -DskipTests
#CMD ["java", "-jar", "target/carfueltracker.jar"]

