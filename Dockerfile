# Use uma imagem base do JDK 17
FROM openjdk:17-jdk-alpine

# Configurar o diretório de trabalho dentro do container
WORKDIR /app

# Copiar o arquivo JAR gerado para o diretório de trabalho no container
COPY target/votacao-0.0.1-SNAPSHOT.jar app.jar

# Expor a porta em que a aplicação vai rodar
EXPOSE 8080

# Definir o comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]