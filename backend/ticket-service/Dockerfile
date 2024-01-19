# Используем образ OpenJDK
FROM openjdk:17

# Устанавливаем директорию приложения
WORKDIR /app

# Копируем JAR-файл в директорию /app
COPY target/ticket-service.jar /app/
COPY src/main/resources/keystore/orgdirectory.jks /app/certs/orgdirectory.jks

# Команда для запуска приложения при старте контейнера
CMD ["java", "-jar", "ticket-service.jar"]
