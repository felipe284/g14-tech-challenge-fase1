# 📋 Plano de Desenvolvimento – FoodLink API (Fase 1)

---

## 📐 Diagrama de Classes

```
┌──────────────────────────────────────────────┐
│  ENUM: UserType                              │
│  + CLIENTE                                   │
│  + DONO_RESTAURANTE                          │
└──────────────────┬───────────────────────────┘
                   │ @Enumerated(EnumType.STRING)
┌──────────────────▼───────────────────────────┐
│  ENTITY: UserEntity                          │
│  - id: UUID                                  │
│  - nome: String                              │
│  - email: String (unique)                    │
│  - login: String (unique)                    │
│  - senha: String (BCrypt hash)               │
│  - tipoUsuario: UserType                     │
│  - dataUltimaAlteracao: OffsetDateTime       │
│  - address: Address (@Embedded)              │
└──────────────────┬───────────────────────────┘
                   │ @Embedded
┌──────────────────▼───────────────────────────┐
│  EMBEDDABLE: Address                         │
│  - logradouro: String                        │
│  - numero: String                            │
│  - complemento: String                       │
│  - bairro: String                            │
│  - cidade: String                            │
│  - uf: String                                │
│  - cep: String                               │
└──────────────────────────────────────────────┘

        REQUEST DTOs                RESPONSE DTOs
┌───────────────────────┐   ┌──────────────────────────┐
│ UserRequestDTO        │   │ UserResponseDTO           │
│ - nome                │   │ - id                      │
│ - email               │   │ - nome                    │
│ - login               │   │ - email                   │
│ - senha               │   │ - login                   │
│ - tipoUsuario         │   │ - tipoUsuario             │
│   (UserType enum)     │   │   (UserType enum)         │
│ - address             │   │ - dataUltimaAlteracao     │
└───────────────────────┘   │ - address                 │
┌───────────────────────┐   └──────────────────────────┘
│ UserUpdateRequestDTO  │   ┌──────────────────────────┐
│ - nome                │   │ AddressResponseDTO        │
│ - email               │   │ - logradouro...cep        │
│ - tipoUsuario         │   └──────────────────────────┘
│   (UserType enum)     │   ┌──────────────────────────┐
│ - address             │   │ LoginResponseDTO          │
└───────────────────────┘   │ - userId                  │
┌───────────────────────┐   │ - login                   │
│ChangePasswordRequestDTO│  │ - tipoUsuario             │
│ - senhaAtual          │   └──────────────────────────┘
│ - novaSenha           │   ┌──────────────────────────┐
└───────────────────────┘   │ PageResponseDTO<T>        │
┌───────────────────────┐   │ - content: List<T>        │
│ LoginRequestDTO       │   │ - page, size              │
│ - login               │   │ - totalElements           │
│ - senha               │   │ - totalPages              │
└───────────────────────┘   └──────────────────────────┘
```

---

## 🗄️ Estrutura de Banco de Dados

### Tabela: `users`

| Coluna                | Tipo         | Restrição                                    |
|-----------------------|--------------|----------------------------------------------|
| id                    | UUID         | PK                                           |
| nome                  | VARCHAR(150) | NOT NULL                                     |
| email                 | VARCHAR(150) | NOT NULL, UNIQUE                             |
| login                 | VARCHAR(50)  | NOT NULL, UNIQUE                             |
| senha                 | VARCHAR(255) | NOT NULL                                     |
| tipo_usuario          | VARCHAR(50)  | NOT NULL, CHECK (CLIENTE, DONO_RESTAURANTE) |
| data_ultima_alteracao | TIMESTAMPTZ  | NOT NULL                                     |
| logradouro            | VARCHAR(200) |                                              |
| numero                | VARCHAR(10)  |                                              |
| complemento           | VARCHAR(100) |                                              |
| bairro                | VARCHAR(100) |                                              |
| cidade                | VARCHAR(100) |                                              |
| uf                    | VARCHAR(2)   |                                              |
| cep                   | VARCHAR(8)   |                                              |

> `tipo_usuario` armazenado como `VARCHAR` via `@Enumerated(EnumType.STRING)` — sem JOIN, sem tabela auxiliar.

### Script Flyway – `V1__create_users.sql`

```sql
```

---

## 📡 Endpoints da API

---

### `POST /users` – Criar Usuário

**Request Body:**
```json
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "login": "joaosilva",
  "senha": "senha123",
  "tipoUsuario": "CLIENTE",
  "address": {
    "logradouro": "Rua das Flores",
    "numero": "100",
    "complemento": "Apto 5",
    "bairro": "Centro",
    "cidade": "São Paulo",
    "uf": "SP",
    "cep": "01001000"
  }
}
```

**Response `201 Created`:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "nome": "João Silva",
  "email": "joao@email.com",
  "login": "joaosilva",
  "tipoUsuario": "CLIENTE",
  "dataUltimaAlteracao": "2026-04-05T10:00:00Z",
  "address": {
    "logradouro": "Rua das Flores",
    "numero": "100",
    "complemento": "Apto 5",
    "bairro": "Centro",
    "cidade": "São Paulo",
    "uf": "SP",
    "cep": "01001000"
  }
}
```

> Valores aceitos para `tipoUsuario`: `"CLIENTE"` ou `"DONO_RESTAURANTE"`

**Response `400 Bad Request`** (valor de enum inválido):
```json
{ "status": 400, "message": "tipoUsuario inválido. Valores aceitos: CLIENTE, DONO_RESTAURANTE" }
```

---

### `GET /users` – Listar Usuários com Paginação

**Query Parameters:**

| Parâmetro | Tipo    | Padrão | Descrição                              |
|-----------|---------|--------|----------------------------------------|
| `page`    | Integer | `0`    | Número da página (base 0)              |
| `size`    | Integer | `10`   | Quantidade de registros por página     |
| `sort`    | String  | `nome` | Campo de ordenação (ex: `nome`, `email`) |

**Exemplo:** `GET /users?page=0&size=10&sort=nome`

**Response `200 OK`:**
```json
{
  "content": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "nome": "João Silva",
      "email": "joao@email.com",
      "login": "joaosilva",
      "tipoUsuario": "CLIENTE",
      "dataUltimaAlteracao": "2026-04-05T10:00:00Z",
      "address": { "logradouro": "Rua das Flores", "..." : "..." }
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 42,
  "totalPages": 5
}
```

---

### `GET /users/{id}` – Buscar Usuário por ID

**Response `200 OK`:** `UserResponseDTO`

**Response `404 Not Found`:**
```json
{
  "status": 404,
  "message": "Usuário não encontrado"
}
```

---

### `PUT /users/{id}` – Atualizar Dados do Usuário

**Request Body:**
```json
{
  "nome": "João Silva Atualizado",
  "email": "joao.novo@email.com",
  "tipoUsuario": "DONO_RESTAURANTE",
  "address": {
    "logradouro": "Av. Paulista",
    "numero": "1000",
    "complemento": "",
    "bairro": "Bela Vista",
    "cidade": "São Paulo",
    "uf": "SP",
    "cep": "01310100"
  }
}
```

**Response `200 OK`:** `UserResponseDTO` atualizado

**Response `404 Not Found`:**
```json
{
  "status": 404,
  "message": "Usuário não encontrado"
}
```

---

### `DELETE /users/{id}` – Excluir Usuário

**Response `204 No Content`**

**Response `404 Not Found`:**
```json
{
  "status": 404,
  "message": "Usuário não encontrado"
}
```

---

### `PATCH /users/{id}/senha` – Trocar Senha

**Request Body:**
```json
{
  "senhaAtual": "senha123",
  "novaSenha": "novaSenha456"
}
```

**Response `204 No Content`**

**Response `400 Bad Request`:**
```json
{
  "status": 400,
  "message": "Senha atual incorreta"
}
```

---

### `POST /auth/login` – Validação de Login

**Request Body:**
```json
{
  "login": "joaosilva",
  "senha": "senha123"
}
```

**Response `200 OK`:**
```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "login": "joaosilva",
  "tipoUsuario": "CLIENTE"
}
```

**Response `401 Unauthorized`:**
```json
{
  "status": 401,
  "message": "Credenciais inválidas"
}
```

---



## 📁 Estrutura de Pacotes

```
src/main/java/com/fiap_g14/foodlink/api/
│
├── FoodlinkApiApplication.java
│
├── config/
│   ├── GlobalExceptionHandler.java    
│   ├── OpenApiConfig.java             
│   └── security/
│       └── SecurityConfig.java        
│
├── controller/
│   ├── UserController.java            
│   └── AuthController.java            
│
├── domain/
│   ├── UserEntity.java                
│   ├── UserType.java                  
│   └── Address.java                   
│
├── dto/
│   ├── UserRequestDTO.java            
│   ├── UserUpdateRequestDTO.java      
│   ├── UserResponseDTO.java           
│   ├── AddressRequestDTO.java         
│   ├── AddressResponseDTO.java        
│   ├── ChangePasswordRequestDTO.java  
│   ├── LoginRequestDTO.java           
│   ├── LoginResponseDTO.java          
│   └── PageResponseDTO.java           
│
├── mapper/
│   └── UserMapper.java                
│
├── repository/
│   └── UserRepository.java            (+ Pageable)
│
└── service/
    ├── UserService.java               
    └── AuthService.java               
```

```
src/main/resources/db/migration/
└── V1__create_users.sql      
```

---

## ⚙️ Configuração Docker Compose

```yaml
Estrutura do docker compose
```

---

## 📦 Dependências Adicionais (`pom.xml`)

```xml
Colocar as principais dependências
```

---

## 🧪 Testes Unitários

### Estrutura dos Arquivos de Teste

```
src/test/java/com/fiap_g14/foodlink/api/
│
├── controller/
│   ├── UserControllerTest.java
│   └── AuthControllerTest.java
│
├── service/
│   ├── UserServiceTest.java
│   └── AuthServiceTest.java
│
├── mapper/
│   └── UserMapperTest.java
│
├── repository/
│   └── UserRepositoryTest.java
│
└── FoodlinkApiApplicationTests.java
```

## Collections para Teste (Postman)

### Grupo: Users
| # | Método | Endpoint                          | Descrição                |
|---|--------|-----------------------------------|--------------------------|
| 1 | POST   | `/users`                          | Criar usuário            |
| 2 | GET    | `/users?page=0&size=10&sort=nome` | Listar com paginação     |
| 3 | GET    | `/users/{id}`                     | Buscar por ID            |
| 4 | PUT    | `/users/{id}`                     | Atualizar usuário        |
| 5 | DELETE | `/users/{id}`                     | Excluir usuário          |
| 6 | PATCH  | `/users/{id}/senha`               | Trocar senha             |

### Grupo: Auth
| # | Método | Endpoint      | Descrição      |
|---|--------|---------------|----------------|
| 7 | POST   | `/auth/login` | Validar login  |

> A Postman Collection será exportada como `foodlink.postman_collection.json` na raiz do repositório.