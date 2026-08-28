# ecommerce_api

E-commerce backend API built with Java and Spring Boot. The project models a real online-store domain: catalog, identity, cart, wallet, orders, inventory, and RBAC-based admin operations.

## 1) What is this?

This is a backend service for an e-commerce platform. It exposes REST APIs for:

- catalog browsing and management
- customer identity and authentication
- cart updates and product lists
- wallet balance management
- order creation, cancellation, completion, and detail lookup
- warehouse and stock management
- permission-based admin workflows

The application is designed as a domain-oriented backend rather than a simple demo CRUD service. Business rules are enforced in service layers and data integrity is handled at the database level.

## 2) Why does it exist?

The system is built to support the full lifecycle of an online store:

- customers browse products and manage carts
- users sign up and authenticate with JWT
- orders are created from cart state and validated against stock and wallet constraints
- warehouse stock is adjusted with transaction history
- admin users manage catalog and inventory with role/permission controls

In short, this repository is a backend foundation for a real e-commerce workflow, not just a toy REST app.

## 3) Tech stack

- Java 25
- Spring Boot 4.1
- Spring Security + JWT authentication
- Spring Data JPA + Hibernate
- PostgreSQL
- Flyway for schema versioning
- Maven
- Docker Compose for local PostgreSQL
- Testcontainers for integration tests
- Springdoc OpenAPI / Swagger UI
- GitHub Actions for CI/CD
- Linux systemd + SSH-based deployment on production host

## 4) What is implemented?

### Identity and access

- signup / login / refresh token flow
- JWT-based authentication and authorization
- role and permission model
- admin-only management endpoints
- user status and authority resolution

### Catalog

- product CRUD and search/filtering
- category tree and category management
- product status types
- paginated catalog listing with sorting and filters

### Customer flows

- cart management with quantity updates
- wallet balance operations
- order creation from customer context
- order details and paginated order history
- order cancellation and completion
- stock reservation logic around order processing

### Inventory and stock

- warehouses and addresses
- stock updates and transaction ledger
- warehouse audit-style stock movement history
- guarded database operations for domain consistency

### Platform features

- health endpoints via Spring Actuator
- centralized error handling
- OpenAPI configuration
- production and local profiles
- DB tuning and safe error behavior in production

## 5) Quick start

### Local development

Start PostgreSQL with Docker Compose:

```bash
docker compose up -d
```

Run the API locally:

```bash
SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
$env:SPRING_PROFILES_ACTIVE = "local"
.\mvnw.cmd spring-boot:run
```

The local profile expects PostgreSQL at `localhost:5432` with:

- database: `ecommerce_api`
- user: `postgres`
- password: `postgres`

Swagger UI is available in local mode at:

```text
http://localhost:8080/swagger-ui/index.html
```

### Production mode

```bash
export SPRING_PROFILES_ACTIVE=prod
export POSTGRES_HOST=db.internal
export POSTGRES_PORT=5432
export POSTGRES_DB=ecommerce_api
export POSTGRES_USER=ecommerce_api
export POSTGRES_PASSWORD=<secret>
export JWT_SECRET=<secret>
export JWT_EXPIRATION_MS=3600000
./mvnw spring-boot:run
```

Production-specific configuration is kept in the `prod` profile; secrets should never be committed to source control.

## 6) Production and deployment

The repository includes a real CI/CD and deployment setup:

- GitHub Actions workflow: `.github/workflows/deploy.yml`
- production deployment on a remote Linux host
- systemd service management
- SSH-based release upload and activation
- health-check rollback on failed deployment

Main production characteristics:

- hardened DB timeout configuration
- generic production error responses
- Swagger and SQL logging disabled in prod profile
- actuator health endpoint used as deployment gate (`/actuator/health`)

For the operational deployment details, see the workflow and the release scripts under `ops/`.

## 7) Where are the tests?

The project includes unit, integration, and infrastructure-oriented tests under `src/test/java`.

Examples:

- product and catalog service tests
- order and wallet behavior tests
- authentication and authorization tests
- Postgres/Testcontainers integration tests

Run the full verification suite:

```bash
./mvnw verify
```

Or just run unit tests quickly:

```bash
./mvnw test
```

## 8) Architecture

This service follows a layered backend architecture:

```text
HTTP Controllers
        ↓
Application Services
        ↓
Repositories / JPA Entities
        ↓
PostgreSQL Database
```

Key design points:

- controllers expose REST endpoints and validate request payloads
- services contain domain logic, transaction boundaries, and business invariants
- repositories manage persistence and queries
- entities map the e-commerce domain model (catalog, orders, wallets, inventory, identity)
- security filters enforce JWT authentication and RBAC permissions
- Flyway keeps the database schema versioned and reproducible

## Project structure

```text
src/
  main/
    java/alexo/ecommerce_api/
      configuration/     # security, cache, jackson, error handling
      http/controller/   # REST endpoints
      service/internal/   # business logic
      repository/        # JPA repositories and queries
      entity/            # domain entities
      dto/               # request/response models
      cache/             # cache services
  test/
    java/               # unit + integration tests
ops/                    # deployment and service scripts
compose.yaml            # local PostgreSQL container
.github/workflows/      # CI/CD pipeline
```

## Summary

This repository is a production-style backend for an e-commerce platform with real business workflows, an RBAC security model, a PostgreSQL data layer, and deployment automation. It is not just a demo API; it is structured like a service that could be extended into a real application backend.
