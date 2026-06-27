# E-commerce Backend with Database Integration

## Project Description

A Spring Boot e-commerce REST API focused on relational database design and Spring Data JPA. It provides product catalog, user, order, inventory, and payment workflows backed by PostgreSQL, managed with Flyway migrations and transactional service methods.

## Features

- Product catalog with pagination, text search, category filtering, and price filtering
- User registration, BCrypt password storage, login verification, and profile updates
- Transactional order creation with pessimistic inventory locking
- Automatic stock deduction and restoration when pending orders are cancelled
- Payment processing linked one-to-one with orders
- Hierarchical categories and complete entity relationships
- Spring Data auditing for creation and modification timestamps
- Flyway schema, seed, and index migrations
- HikariCP connection pooling
- Product caching and optimized order fetch queries
- Structured validation and exception responses
- Swagger/OpenAPI documentation

## Technologies

- Java 17
- Spring Boot 3.2.1
- Spring Data JPA and Hibernate
- PostgreSQL 15
- Flyway
- HikariCP
- Spring Cache
- BCrypt password hashing
- Docker Compose
- H2 PostgreSQL compatibility mode for tests

## Database Relationships

```text
User 1 -------- * Order
Order 1 ------- * OrderItem
Product 1 ----- * OrderItem
Category 1 ---- * Product
Category 1 ---- * child Category
Order 1 ------- 1 Payment
```

The schema is defined in `src/main/resources/db/migration`. `V1` creates tables and constraints, `V2` inserts demonstration data, and `V3` adds indexes for catalog, order, and payment queries.

## Run with Docker

```bash
docker compose up --build
```

The API is available at `http://localhost:8080`. Swagger UI is available at `http://localhost:8080/swagger-ui.html`.

## Run Manually

1. Create a PostgreSQL database named `ecommerce_db`.
2. Set `DATABASE_URL`, `DATABASE_USERNAME`, and `DATABASE_PASSWORD` if they differ from the defaults.
3. Run `mvn spring-boot:run`.
4. Flyway applies all migrations automatically.

Default local configuration:

```text
URL: jdbc:postgresql://localhost:5432/ecommerce_db
Username: postgres
Password: password
```

Seeded API users both use the password `password`:

```text
admin@example.com
customer@example.com
```

## API Endpoints

```text
GET    /api/products
GET    /api/products/{id}
POST   /api/products
PUT    /api/products/{id}
DELETE /api/products/{id}

POST   /api/auth/register
POST   /api/auth/login
GET    /api/users/{id}/profile
PUT    /api/users/{id}/profile

GET    /api/orders?userId={userId}
GET    /api/orders/{id}
POST   /api/orders
PUT    /api/orders/{id}/cancel
GET    /api/orders/report/daily?startDate={ISO_DATE_TIME}

POST   /api/payments
GET    /api/payments/order/{orderId}
```

## Sample Order

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"userId":2,"items":[{"productId":1,"quantity":2},{"productId":2,"quantity":1}],"shippingAddress":"123 Main Street"}'
```

Order creation runs in one database transaction. Each product row is locked before its stock is checked and reduced. Any missing product or insufficient stock causes the entire transaction to roll back.

## Project Structure

```text
week7-ecommerce-backend/
|-- src/main/java/com/ecommerce/
|   |-- EcommerceApplication.java
|   |-- config/
|   |-- controller/
|   |-- exception/
|   |-- model/
|   |   |-- dto/
|   |   |-- entity/
|   |   `-- enums/
|   |-- repository/
|   `-- service/
|-- src/main/resources/
|   |-- db/migration/
|   |   |-- V1__initial_schema.sql
|   |   |-- V2__seed_data.sql
|   |   `-- V3__add_indexes.sql
|   `-- application.yml
|-- src/test/
|-- docker-compose.yml
|-- Dockerfile
|-- pom.xml
|-- README.md
`-- .gitignore
```

## Query Optimization

- Product name, category, active/price, order user/date, status, and foreign key indexes are created by Flyway.
- Catalog filtering uses one JPQL query and fetches the category in the same query.
- Detailed orders use `@EntityGraph` to fetch users, items, and products without the N+1 problem.
- Inventory updates use pessimistic row locks plus optimistic entity versions.
- Product detail responses are cached and invalidated after product writes.
- Hibernate SQL and bind logging can be adjusted in `application.yml`.

## Testing

```bash
mvn test
```

Tests use H2 in PostgreSQL compatibility mode and run the real Flyway migrations. Coverage includes repository filtering, transactional order creation, stock deduction, and rollback when inventory is insufficient.

## Quality Checklist

- [x] PostgreSQL schema and Docker setup
- [x] Complex JPA relationships
- [x] Custom JPQL and native queries
- [x] Transactional order processing
- [x] Pessimistic locking and optimistic versioning
- [x] Flyway schema, seed, and index migrations
- [x] Spring Data auditing
- [x] HikariCP configuration
- [x] Caching and fetch optimization
- [x] Validation and global exception handling
- [x] Integration and repository tests
- [x] Swagger documentation
