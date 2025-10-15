# Etapa 1: Build de la aplicación
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar los archivos del proyecto
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw mvnw
COPY mvnw.cmd mvnw.cmd
COPY src src

# Construir el proyecto (esto genera el .jar)
RUN ./mvnw clean package -DskipTests

# Etapa 2: Imagen liviana para correr la app
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copiar el .jar generado desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Puerto expuesto
EXPOSE 8080

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
