# Blog Management REST API

## Project Description

A Spring Boot REST API for managing blog posts, categories, and comments. The project demonstrates Spring Boot fundamentals, REST controller design, Spring Data JPA, validation, global exception handling, logging, profile-based configuration, Swagger documentation, and API testing with Postman or curl.

## Features

- CRUD endpoints for blog posts
- CRUD endpoints for categories
- Comment creation, listing, moderation, and deletion
- Spring Data JPA repositories with H2 for development
- Pagination and sorting for post lists
- Author search and category filtering
- Request validation with Jakarta Bean Validation
- Global API error responses with `@RestControllerAdvice`
- Seed data loaded on application startup
- Swagger UI and H2 console in the development profile
- Postman collection for common API requests

## Technologies

- Java 17
- Spring Boot 3.2.1
- Spring Web
- Spring Data JPA and Hibernate
- Jakarta Validation
- H2 Database for development
- PostgreSQL driver for production profile
- Springdoc OpenAPI
- JUnit 5 and MockMvc

## How to Run

1. Open the `week6-spring-blog-api` folder.
2. Run `mvn spring-boot:run`.
3. Open `http://localhost:8080/swagger-ui.html` for Swagger UI.
4. Open `http://localhost:8080/h2-console` for the H2 console.
5. Use JDBC URL `jdbc:h2:mem:blogdb`, username `sa`, and a blank password.

## Common curl Requests

```bash
curl http://localhost:8080/api/posts

curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -d '{"title":"My First Blog Post","content":"This is my first post using Spring Boot.","author":"John Doe","categoryId":1}'

curl "http://localhost:8080/api/posts?page=0&size=5&sort=createdAt,desc"

curl http://localhost:8080/api/categories

curl -X POST http://localhost:8080/api/comments \
  -H "Content-Type: application/json" \
  -d '{"content":"Helpful article.","author":"Reader One","postId":1}'
```

## API Endpoints

```text
GET    /api/posts
GET    /api/posts/{id}
POST   /api/posts
PUT    /api/posts/{id}
DELETE /api/posts/{id}
GET    /api/posts/category/{categoryId}
GET    /api/posts/search?author={author}

GET    /api/categories
GET    /api/categories/{id}
POST   /api/categories
PUT    /api/categories/{id}
DELETE /api/categories/{id}

GET    /api/comments/post/{postId}
GET    /api/comments/pending
POST   /api/comments
PATCH  /api/comments/{id}/approve
DELETE /api/comments/{id}
```

## Project Structure

```text
week6-spring-blog-api/
|-- src/main/java/com/blogapi/
|   |-- BlogApiApplication.java
|   |-- config/
|   |   `-- SwaggerConfig.java
|   |-- controller/
|   |   |-- PostController.java
|   |   |-- CategoryController.java
|   |   `-- CommentController.java
|   |-- exception/
|   |   |-- ResourceNotFoundException.java
|   |   `-- GlobalExceptionHandler.java
|   |-- model/
|   |   |-- dto/
|   |   `-- entity/
|   |-- repository/
|   |-- service/
|-- src/main/resources/
|   |-- application.properties
|   |-- application-dev.properties
|   `-- application-prod.properties
|-- src/test/java/com/blogapi/
|-- docs/
|   `-- postman_collection.json
|-- pom.xml
|-- README.md
`-- .gitignore
```

## Technical Details

The API uses layered architecture. Controllers validate and route HTTP requests, services hold business logic and transactions, repositories provide database access, and entities model persisted data. `Post` belongs to a `Category`, and `Comment` belongs to a `Post`.

The development profile uses an in-memory H2 database with `ddl-auto=create-drop`, which makes the app easy to run without external setup. The production profile is configured for PostgreSQL using environment variables.

Validation failures, missing resources, duplicate data, and unexpected errors are returned as structured JSON responses through `GlobalExceptionHandler`.

## Testing Evidence

- `BlogApiApplicationTests` verifies the Spring context loads.
- `PostControllerIntegrationTest` verifies seeded posts can be listed.
- `PostControllerIntegrationTest` verifies invalid post creation returns HTTP 400.
- The Postman collection covers listing and creating posts, categories, and comments.

Run tests with:

```bash
mvn test
```

## Quality Standards Checklist

- [x] Spring Boot 3.x project structure
- [x] RESTful endpoints for posts, categories, and comments
- [x] Spring Data JPA repositories
- [x] H2 development configuration
- [x] PostgreSQL production profile
- [x] Request validation
- [x] Global exception handling
- [x] Proper HTTP status codes
- [x] Pagination and sorting
- [x] Logging configuration
- [x] Swagger documentation
- [x] Postman collection
- [x] Basic integration tests
