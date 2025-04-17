FROM eclipse-temurin:17-jdk
VOLUME /tmp
WORKDIR /app

# Copia o JAR gerado
COPY target/contratacao-service.jar app.jar

# Expõe a porta padrão
EXPOSE 8080

# Comando de inicialização
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]