FROM openjdk:17-jdk-slim-buster

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src

# Establece permisos de ejecución para gradlew y construye la aplicación
RUN chmod +x ./gradlew && ./gradlew build -x test

# Puerto que expone la aplicación
EXPOSE 8081

# Comando para ejecutar la aplicación
ENTRYPOINT ["java","-jar","build/libs/ServicioCuentas-0.0.1-SNAPSHOT.jar"]