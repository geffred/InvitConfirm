# Étape de construction
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# Étape d'exécution
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Exposition du port et commande de démarrage
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]