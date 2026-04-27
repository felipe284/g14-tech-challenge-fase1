# g14-tech-challenge-fase1

# 🚀 Foodlink API — Guia de Configuração e Execução

Este repositório contém a API do projeto Foodlink (backend). Abaixo estão instruções rápidas para preparar, executar e testar a aplicação, além do collection do Postman.

Tecnologias principais:

- Java 21
- Spring Boot 4.0.5
- JPA (Hibernate)
- Flyway (migrations)
- PostgreSQL
- Docker / Docker Compose

---

# Checklist rápido

- [x] Pré-requisitos instalados (Docker, Docker Compose)
- [x] Rodar via Docker Compose
- [x] Importar collection do Postman para testar endpoints

---

# 📦 Pré-requisitos

Antes de iniciar, certifique-se de ter instalado:
- Docker
- Docker Compose

Verifique com:

```bash
docker -v
docker-compose -v
```

---

# Como executar (via Docker Compose)

```bash
# no diretório raiz do repositório
cd foodlink-api
docker-compose up --build
```

Depois do startup, a API estará disponível por padrão em http://localhost:8080. 

---

# Testes

Para rodar os testes automatizados (unit/integration):

```bash

# entre na pasta do projeto
cd foodlink-api
# execute os testes usando o Maven Wrapper
./mvnw test
```

---

# Postman Collection

Este repositório inclui um Postman Collection que facilita testar os endpoints da API.

- Importar a coleção localmente (arquivo presente no repositório):

- `foodlink-api.postman_collection.json` (na raiz do repositório)

- Link para baixar diretamente (arquivo no repositório):

  - [Baixar collection (JSON)](./foodlink-api.postman_collection.json)

 - No Postman: File -> Import -> Select File -> escolha `foodlink-api.postman_collection.json`. 

 ---

# Postman: variáveis e script de login

Para facilitar o uso ao importar a collection, a coleção inclui variáveis e um script automático que preenche `{{userId}}` após o login.

- Variáveis da collection (já incluídas no JSON):
  - `baseUrl` — URL base da API (padrão: `http://localhost:8080`).
  - `userId` — vazio por padrão; será preenchido automaticamente pelo script de login. 
- Script de login:
  - O request `Auth > Login - POST /auth/login` contém um script na aba "Tests" que extrai `userId` do corpo da resposta e define a variável de collection `userId` automaticamente.
  - Assim, ao executar o request de login no Postman, as requests seguintes que usam `{{userId}}` (ex.: `GET /users/{{userId}}`) usarão o valor retornado pelo servidor.

# Estrutura do projeto (resumo)

- `foodlink-api/` - código fonte da aplicação Spring Boot
- `foodlink-api.postman_collection.json` - collection principal para importar no Postman

