# Estágio 1: Construir a aplicação com Maven
# Usamos uma imagem oficial do Maven que já tem o Java 21 instalado
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo pom.xml primeiro para aproveitar o cache do Docker
COPY pom.xml .

# Baixa as dependências do projeto
RUN mvn dependency:go-offline

# Copia todo o resto do código-fonte do projeto
COPY src ./src

# Constrói o arquivo .jar executável (pulando os testes)
RUN mvn clean install -DskipTests


# Estágio 2: Executar a aplicação
# Usamos uma imagem oficial do Java 21, que é muito menor e mais segura
FROM eclipse-temurin:21-jre-alpine

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo .jar que foi gerado no estágio anterior
COPY --from=build /app/target/encurtalink-0.0.1-SNAPSHOT.jar .

# Expõe a porta 8080 para que o Render possa se comunicar com a aplicação
EXPOSE 8080

# Comando para iniciar a aplicação quando o container for executado
CMD ["java", "-jar", "encurtalink-0.0.1-SNAPSHOT.jar"]