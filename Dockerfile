# Используем официальный образ PostgreSQL
FROM postgres:latest

# Устанавливаем переменные окружения для настроек БД
ENV POSTGRES_DB=mydatabase
ENV POSTGRES_USER=myuser
ENV POSTGRES_PASSWORD=mypassword

# Открываем порт для PostgreSQL
EXPOSE 5432
