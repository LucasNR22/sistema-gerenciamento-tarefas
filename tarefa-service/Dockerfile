FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiar o arquivo JAR do projeto
COPY target/*.jar app.jar

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "/app/app.jar"]