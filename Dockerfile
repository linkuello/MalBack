# === STAGE 1: Build ===
FROM openjdk:17-jdk-slim AS build
WORKDIR /app

# Копируем проект
COPY . .

# Собираем проект (пропускаем тесты, чтобы быстрее)
RUN ./mvnw clean package -DskipTests

# === STAGE 2: Run ===
FROM openjdk:17-jdk-slim
WORKDIR /app

# Копируем собранный .jar из предыдущего этапа
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
