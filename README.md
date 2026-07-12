# Blogger API

A RESTful blog platform backend built with **Spring Boot 3.2.4** and **Java 17**. Supports user registration, JWT-based authentication (RSA-signed), blog post management, and commenting.

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Spring Boot | 3.2.4 | Application framework |
| Java | 17 | Language |
| H2 Database | — | In-memory database |
| Spring Security + OAuth2 Resource Server | — | Authentication & authorization |
| Hibernate Validator | 8.0.1 | Request validation |
| ModelMapper | 3.2.0 | DTO/Entity mapping |
| SpringDoc OpenAPI | 2.2.0 | Swagger UI documentation |
| Lombok | — | Boilerplate reduction |
| Maven | — | Build tool |

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.x

### RSA Key Generation

Generate RSA key pair for JWT signing (place in `src/main/resources/certs/`):

```bash
# Generate private key
openssl genrsa -out src/main/resources/certs/privateKey.pem 2048

# Extract public key
openssl rsa -in src/main/resources/certs/privateKey.pem -pubout -out src/main/resources/certs/publicKey.pem
```

### Run the Application

```bash
./mvnw spring-boot:run
```

The server starts on **port 8081**.

### Default Users

On startup, two users are seeded automatically:

| Role | Email | Username | Password |
|---|---|---|---|
| ADMIN | `admin@admin.com` | Admin | `password` |
| BLOGGER | `user@user.com` | User | `password` |

### Useful URLs

| Resource | URL |
|---|---|
| API Base | `http://localhost:8081` |
| Swagger UI | `http://localhost:8081/swagger-ui.html` |
| H2 Console | `http://localhost:8081/h2-console` |

H2 Console connection: JDBC URL = `jdbc:h2:mem:blogger`, User = `root`, Password = *(empty)*

## API Endpoints

### Authentication

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `POST` | `/sign-up` | None | Register a new user |
| `POST` | `/sign-in` | Basic Auth | Sign in and receive JWT tokens |
| `POST` | `/refresh-token` | Bearer (refresh token) | Get a new access token |
| `POST` | `/logout` | Bearer (access token) | Revoke refresh token and logout |

### Blogs

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `POST` | `/api/blogs` | Bearer | Create a blog post |
| `GET` | `/api/blogs` | None | Get all blogs with comments |
| `GET` | `/api/blogs/{id}` | None | Get a blog by ID |
| `DELETE` | `/api/blogs/{id}` | Bearer | Delete a blog (cascades to comments) |
| `GET` | `/api/blogs/paged?page=0&size=10` | Bearer | Get paginated blogs |

### Comments

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `POST` | `/api/comments` | Bearer | Add a comment to a blog |
| `GET` | `/api/comments` | Bearer | Get all comments |
| `GET` | `/api/comments/{id}` | Bearer | Get a comment by ID |
| `DELETE` | `/api/comments/{id}` | Bearer | Delete a comment |
| `GET` | `/api/comments/paged?page=0&size=10` | Bearer | Get paginated comments |

## Authentication Flow

1. **Register** — `POST /sign-up` with user details. Returns access and refresh tokens.
2. **Sign In** — `POST /sign-in` with Basic Auth (email + password). Returns tokens; refresh token also set as an HttpOnly cookie.
3. **Access API** — Include `Authorization: Bearer <access_token>` header on protected requests.
4. **Refresh** — When the access token expires (15 min), call `POST /refresh-token` with the refresh token to get a new access token.
5. **Logout** — `POST /logout` revokes the refresh token in the database.

### Token Details

| Token | Lifetime | Scope |
|---|---|---|
| Access Token | 15 minutes | Role-based (`READ`, `WRITE`, `DELETE`) |
| Refresh Token | 15 days | `REFRESH_TOKEN` |

### Role Permissions

| Role | Scopes |
|---|---|
| `ROLE_ADMIN` | READ, WRITE, DELETE |
| `ROLE_BLOGGER` | READ |

## Data Model

```
User 1──* Blog 1──* Comment
  │                     │
  │                     │
  └──* RefreshToken     └──1 User
```

### Entities

**User** — `id`, `firstName`, `lastName`, `email` (unique), `username` (unique), `password`, `roles`

**Blog** — `id`, `title`, `blog` (content), `createdOn`, `user` (author), `comments`

**Comment** — `id`, `comment`, `createdOn`, `blog`, `user` (author)

**RefreshToken** — `id`, `refreshToken`, `revoked`, `user`

## Request / Response Examples

### Sign Up

```http
POST /sign-up
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "username": "johndoe",
  "password": "secret123"
}
```

Response:

```json
{
  "access_token": "eyJhbGciOi...",
  "access_token_expiry": 900,
  "token_type": "Bearer",
  "full_name": "John Doe",
  "username": "johndoe",
  "email": "john@example.com",
  "roles": "ROLE_BLOGGER"
}
```

### Create Blog

```http
POST /api/blogs
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "title": "My First Blog Post",
  "blog": "This is the content of my very first blog post on this platform."
}
```

### Add Comment

```http
POST /api/comments
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "comment": "Great post!",
  "blog": { "id": 1 }
}
```

## Project Structure

```
src/main/java/com/blogger/
├── BloggerApplication.java          # Entry point
├── config/
│   ├── InitialDefaultUser.java      # Seeds default admin & user
│   ├── RSAKeyRecord.java            # RSA key pair config
│   ├── SecurityConfig.java          # Security filter chains
│   ├── WebConfig.java               # CORS configuration
│   ├── jwt/                         # JWT filters & utilities
│   └── user/                        # UserDetails & UserManager
├── dto/                             # Request/response DTOs
├── entities/                        # JPA entities
├── enums/                           # ROLES enum
├── exception/                       # Custom exceptions & global handler
├── repository/                      # Spring Data JPA repositories
├── resources/                       # REST controllers
├── service/                         # Service interfaces
│   └── impl/                        # Service implementations
└── utils/                           # Mappers & token generator
```
