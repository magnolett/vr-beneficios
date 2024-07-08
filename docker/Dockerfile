# Use uma imagem base do JDK
FROM openjdk:17-jdk-alpine

# Configurar o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copiar o arquivo JAR gerado para o contêiner
COPY target/*.jar app.jar

# Expor a porta que a aplicação Spring Boot vai rodar
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]