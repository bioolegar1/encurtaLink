# 🔗 Encurtador de URLs com Autenticação JWT

Um serviço completo de encurtamento de URLs desenvolvido em **Java** com **Spring Boot**, incluindo sistema de autenticação de usuários, geração de QR Codes e controle de acessos.

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias Utilizadas](#️-tecnologias-utilizadas)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Configuração](#-instalação-e-configuração)
- [Como Usar a API](#-como-usar-a-api)
- [Endpoints da API](#-endpoints-da-api)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Exemplos de Uso](#-exemplos-de-uso)
- [Contribuindo](#-contribuindo)
- [Autor](#-autor)

## 📝 Sobre o Projeto

Este projeto demonstra a construção de uma **API RESTful** robusta e segura utilizando o ecossistema Spring. A aplicação permite que usuários registrados criem e gerenciem seus próprios links encurtados, com sistema de autenticação **stateless** baseado em **JSON Web Tokens (JWT)**.

### 🎯 Objetivos de Aprendizado

- Implementação de autenticação e autorização com Spring Security
- Uso de JWT para autenticação stateless
- Criação de APIs RESTful com Spring Boot
- Integração com banco de dados PostgreSQL usando JPA/Hibernate
- Geração de QR Codes com a biblioteca Zxing
- Boas práticas de desenvolvimento em Java

## ✨ Funcionalidades

- 🔐 **Registro e Login de Usuários**: Sistema de autenticação seguro com criptografia de senhas
- 🔗 **Encurtamento de URLs Personalizado**: Cada link pertence a um usuário específico
- 📊 **Histórico Pessoal**: Visualize todos os links criados pelo usuário autenticado
- 📈 **Contagem de Cliques**: Rastreamento automático de acessos para cada link
- 📱 **Geração de QR Code**: Imagem PNG automática para compartilhamento fácil
- ⏰ **Expiração de Links**: Sistema de validade configurável para URLs
- 🔒 **Segurança JWT**: Autenticação stateless e segura

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **Java** | 21 | Linguagem de programação principal |
| **Spring Boot** | 3.x | Framework para desenvolvimento web |
| **Spring Security** | 6.x | Segurança e autenticação |
| **Spring Data JPA** | 3.x | Persistência de dados |
| **JWT** | - | Autenticação baseada em tokens |
| **PostgreSQL** | 15+ | Banco de dados relacional |
| **Maven** | 3.8+ | Gerenciador de dependências |
| **Lombok** | 1.18+ | Redução de código boilerplate |
| **Zxing** | 3.5+ | Geração de QR Codes |
| **Hibernate** | 6.x | ORM (Object-Relational Mapping) |

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- ☕ **Java 21** ou superior
- 🗄️ **PostgreSQL 15** ou superior
- 📦 **Maven 3.8** ou superior
- 🔧 **Git** para controle de versão
- 💻 **IDE** (IntelliJ IDEA, Eclipse, ou VS Code)

## 🚀 Instalação e Configuração

### 1. Clone o Repositório

```bash
git clone https://github.com/seu-usuario/url-shortener.git
cd url-shortener
```

### 2. Configure o Banco de Dados

Crie um banco de dados PostgreSQL:

```sql
CREATE DATABASE url_shortener;
CREATE USER shortener_user WITH PASSWORD 'sua_senha_aqui';
GRANT ALL PRIVILEGES ON DATABASE url_shortener TO shortener_user;
```

### 3. Configure o application.properties

Crie/edite o arquivo `src/main/resources/application.properties`:

```properties
# Configuração do Banco de Dados
spring.datasource.url=jdbc:postgresql://localhost:5432/url_shortener
spring.datasource.username=shortener_user
spring.datasource.password=sua_senha_aqui
spring.datasource.driver-class-name=org.postgresql.Driver

# Configuração JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Configuração JWT
jwt.secret=minha_chave_secreta_super_segura_aqui
jwt.expiration=86400000

# Configuração da Aplicação
server.port=8080
app.base-url=http://localhost:8080
```

### 4. Execute a Aplicação

```bash
# Compile o projeto
mvn clean compile

# Execute os testes
mvn test

# Inicie a aplicação
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## 🔧 Como Usar a API

### 🔐 Fluxo de Autenticação

Para acessar endpoints protegidos, siga este processo:

1. **Registre-se**: Crie uma conta usando `POST /auth/register`
2. **Faça Login**: Autentique-se com `POST /auth/login`
3. **Guarde o Token**: Armazene o JWT retornado
4. **Use o Token**: Inclua em todos os requests protegidos

**Formato do Header de Autorização:**
```
Authorization: Bearer seu_token_jwt_aqui
```

## 📚 Endpoints da API

### 🌐 Endpoints Públicos (Sem Autenticação)

#### 1. Registrar Novo Usuário
```http
POST /auth/register
Content-Type: application/json

{
    "username": "meu_usuario",
    "password": "minha_senha_123"
}
```

**Resposta de Sucesso (201 Created):**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "message": "Usuário registrado com sucesso"
}
```

#### 2. Fazer Login
```http
POST /auth/login
Content-Type: application/json

{
    "username": "meu_usuario",
    "password": "minha_senha_123"
}
```

**Resposta de Sucesso (200 OK):**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "expiresIn": 86400000
}
```

#### 3. Redirecionar para URL Original
```http
GET /{shortKey}
```
Redireciona automaticamente para a URL original e incrementa contador.

#### 4. Obter QR Code
```http
GET /qrcode/{shortKey}
```
Retorna imagem PNG do QR Code.

### 🔒 Endpoints Protegidos (Requerem Token JWT)

#### 1. Encurtar Nova URL
```http
POST /shorten-url
Authorization: Bearer seu_token_aqui
Content-Type: application/json

{
    "url": "https://www.google.com",
    "customAlias": "google" // opcional
}
```

**Resposta de Sucesso (201 Created):**
```json
{
    "shortUrl": "http://localhost:8080/aB1cDef",
    "originalUrl": "https://www.google.com",
    "shortKey": "aB1cDef",
    "qrCodeUrl": "http://localhost:8080/qrcode/aB1cDef",
    "expiresAt": "2024-08-24T10:30:00Z"
}
```

#### 2. Obter Histórico do Usuário
```http
GET /history
Authorization: Bearer seu_token_aqui
```

**Resposta de Sucesso (200 OK):**
```json
[
    {
        "shortUrl": "http://localhost:8080/aB1cDef",
        "originalUrl": "https://www.google.com",
        "shortKey": "aB1cDef",
        "clickCount": 15,
        "createdAt": "2024-07-24T10:30:00Z",
        "expiresAt": "2024-08-24T10:30:00Z",
        "daysToExpire": 29,
        "qrCodeUrl": "http://localhost:8080/qrcode/aB1cDef"
    }
]
```

#### 3. Obter Estatísticas do Link
```http
GET /stats/{shortKey}
Authorization: Bearer seu_token_aqui
```

**Resposta de Sucesso (200 OK):**
```json
{
    "shortKey": "aB1cDef",
    "originalUrl": "https://www.google.com",
    "clickCount": 15,
    "createdAt": "2024-07-24T10:30:00Z",
    "lastAccessed": "2024-07-24T15:45:30Z"
}
```

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/exemplo/urlshortener/
│   │       ├── UrlShortenerApplication.java      # Classe principal
│   │       ├── config/                           # Configurações
│   │       │   ├── SecurityConfig.java           # Config. Spring Security
│   │       │   └── JwtConfig.java                # Config. JWT
│   │       ├── controller/                       # Controladores REST
│   │       │   ├── AuthController.java           # Login/Registro
│   │       │   ├── UrlController.java            # URLs principais
│   │       │   └── QrCodeController.java         # QR Codes
│   │       ├── model/                            # Entidades JPA
│   │       │   ├── User.java                     # Usuário
│   │       │   └── UrlMapping.java               # Mapeamento URL
│   │       ├── repository/                       # Repositories JPA
│   │       │   ├── UserRepository.java           # Repo usuários
│   │       │   └── UrlMappingRepository.java     # Repo URLs
│   │       ├── service/                          # Lógica de negócio
│   │       │   ├── AuthService.java              # Autenticação
│   │       │   ├── UrlShortenerService.java      # Encurtamento
│   │       │   ├── QrCodeService.java            # QR Codes
│   │       │   └── JwtService.java               # JWT
│   │       ├── dto/                              # Data Transfer Objects
│   │       │   ├── LoginRequest.java             # Request login
│   │       │   ├── RegisterRequest.java          # Request registro
│   │       │   └── ShortenUrlRequest.java        # Request encurtar
│   │       └── exception/                        # Tratamento exceções
│   │           └── GlobalExceptionHandler.java   # Handler global
│   └── resources/
│       ├── application.properties                # Configurações app
│       └── data.sql                             # Dados iniciais (opcional)
└── test/                                        # Testes unitários
    └── java/
        └── com/exemplo/urlshortener/
            ├── controller/                      # Testes controllers
            └── service/                         # Testes services
```

## 💡 Exemplos de Uso

### Exemplo Completo com cURL

```bash
# 1. Registrar usuário
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "joao", "password": "senha123"}'

# 2. Fazer login (guarde o token retornado)
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "joao", "password": "senha123"}'

# 3. Encurtar URL (substitua SEU_TOKEN pelo token recebido)
curl -X POST http://localhost:8080/shorten-url \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"url": "https://www.google.com"}'

# 4. Ver histórico
curl -X GET http://localhost:8080/history \
  -H "Authorization: Bearer SEU_TOKEN"

# 5. Acessar link encurtado (substitua SHORT_KEY pela chave retornada)
curl -X GET http://localhost:8080/SHORT_KEY
```

### Exemplo com JavaScript (Frontend)

```javascript
// Função para fazer login
async function login(username, password) {
    const response = await fetch('/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username, password })
    });
    
    const data = await response.json();
    localStorage.setItem('token', data.token);
    return data.token;
}

// Função para encurtar URL
async function shortenUrl(url) {
    const token = localStorage.getItem('token');
    
    const response = await fetch('/shorten-url', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ url })
    });
    
    return await response.json();
}
```

## 🔍 Explicações para Iniciantes

### Como Funciona o JWT?

O **JWT (JSON Web Token)** é como um "passe" digital que prova quem você é:

1. **Login**: Você envia usuário/senha
2. **Token**: O servidor cria um token criptografado
3. **Uso**: Você envia este token em cada requisição
4. **Verificação**: O servidor verifica se o token é válido

### Como Funciona o Spring Security?

O **Spring Security** protege sua aplicação:

- **Filtros**: Interceptam todas as requisições
- **Autenticação**: Verificam se você é quem diz ser
- **Autorização**: Verificam se você pode fazer o que quer
- **Criptografia**: Protegem senhas e dados sensíveis

### Como Funciona o JPA/Hibernate?

**JPA** com **Hibernate** facilita o trabalho com banco de dados:

- **Entidades**: Classes Java que representam tabelas
- **Repositories**: Interfaces para operações no banco
- **Queries**: Métodos que viram comandos SQL automaticamente

## 🧪 Testando a Aplicação

### Testes Unitários

```bash
# Executar todos os testes
mvn test

# Executar teste específico
mvn test -Dtest=AuthServiceTest

# Executar com relatório de cobertura
mvn test jacoco:report