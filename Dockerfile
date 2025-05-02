FROM openjdk:17-jdk-slim
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests
CMD ["java", "-jar", "target/mal-back-0.0.1-SNAPSHOT.jar"]

