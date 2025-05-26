# Используем официальный образ Maven с JDK 17
FROM maven:3.9.6-eclipse-temurin-17

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем pom.xml и загружаем зависимости (кэширование)
COPY pom.xml . 
RUN mvn dependency:go-offline

# Копируем остальные файлы
COPY . .

# Собираем приложение
RUN mvn clean package -DskipTests

# Запускаем приложение
CMD ["java", "-jar", "target/social-network-1.0-SNAPSHOT.jar"]
