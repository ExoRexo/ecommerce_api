# Software Engineering Conventions & Architecture Guidelines
**Project Stack:** Java 25 | Spring Boot 3.4+ | PostgreSQL 16+ | Liquibase

---

## 1. Overview & Core Philosophy

This document serves as the single source of truth for architectural standards, code style, and development patterns for both **human developers** and **AI coding assistants** working on this repository.

### Strategic Priorities
1. **Predictability over Cleverness:** Code should be simple, explicit, and easy to read. Avoid obscure language features or overly complex abstractions.
2. **Type Safety & Immutability:** Leverage modern Java 25 features (Records, Pattern Matching, Sealed Classes) to build compile-time safe domains.
3. **Domain-Driven Isolation:** Keep business logic independent of external frameworks, delivery mechanisms (REST/gRPC), and persistence implementation details.
4. **AI-Friendly Architecture:** Code structures, naming conventions, and project hierarchy must be standardized so LLMs/AI agents can accurately navigate, reason about, and modify the codebase without context bleeding.

---

## 2. Project Architecture & Layering

We follow a **Clean / Hexagonal Architecture (Ports and Adapters)** pattern with modular boundaries.

```
src/main/java/com/project/
├── domain/                  # Pure Business Logic (Framework Agnostic)
│   ├── model/               # Aggregates, Entities, Value Objects (Records/Classes)
│   ├── exception/           # Pure Domain Exceptions
│   └── port/                # Interfaces
│       ├── inbound/         # Use Case Interfaces (Input Ports)
│       └── outbound/        # Repository / External Service Interfaces (Output Ports)
│
├── application/             # Application Services & Orchestration
│   ├── service/             # Use Case Implementations
│   └── dto/                 # Application Commands / Queries
│
├── infrastructure/          # External Integrations & Concrete Implementations
│   ├── persistence/         # Database Access
│   │   ├── entity/          # JPA / Spring Data Entities
│   │   ├── mapper/          # MapStruct Mappers (Domain <-> JPA)
│   │   └── adapter/         # Output Port Implementations (Repositories)
│   ├── client/              # External APIs (REST/Feign/WebClient)
│   └── config/              # Spring Configuration Beans
│
└── presentation/            # Controllers & Delivery Layer
    ├── rest/                # Spring MVC / WebFlux Controllers
    │   ├── request/         # Request DTOs (Validation tags)
    │   └── response/        # Response DTOs
    └── mapper/              # MapStruct Mappers (Domain <-> REST DTO)
```

### Layer Interaction Rules
* **Domain** MUST NOT import anything from `springframework`, `jakarta.persistence`, or `presentation`.
* **Application** depends ONLY on **Domain** and Outbound Port interfaces.
* **Infrastructure** implements Outbound Ports and handles DB/External I/O.
* **Presentation** calls Inbound Use Cases (Application Services) and handles HTTP serialization/validation.

---

## 3. Java 25 Standards & Language Features

We leverage modern Java 25 standards. All code must adhere to these language features:

### 3.1 Records for Data Carriers
Use `record` for all read-only Data Transfer Objects (DTOs), API payloads, Value Objects, and internal communication events.
```java
// Preferred for DTOs and Value Objects
public record UserResponseDto(
    UUID id,
    String email,
    String fullName,
    Instant createdAt
) {}
```

### 3.2 Sealed Classes for Explicit Domain Hierarchy
Use `sealed` interfaces/classes to constrain domain hierarchies and ensure exhaustive pattern matching.
```java
public sealed interface OrderStatus permits OrderStatus.Pending, OrderStatus.Paid, OrderStatus.Cancelled {
    record Pending(Instant createdAt) implements OrderStatus {}
    record Paid(Instant paidAt, String transactionId) implements OrderStatus {}
    record Cancelled(String reason) implements OrderStatus {}
}
```

### 3.3 Pattern Matching & Switch Expressions
Always use pattern matching and switch expressions over complex `if-else` chains.
```java
public String resolveStatusMessage(OrderStatus status) {
    return switch (status) {
        case OrderStatus.Pending p -> "Order placed at " + p.createdAt();
        case OrderStatus.Paid p    -> "Order paid with ref: " + p.transactionId();
        case OrderStatus.Cancelled c -> "Order cancelled: " + c.reason();
    };
}
```

### 3.4 Virtual Threads (Project Loom)
Java 25 handles high-throughput I/O natively via Virtual Threads.
* Do **NOT** use custom thread pools or `CompletableFuture` for simple blocking calls.
* Enable Virtual Threads in Spring Boot (`spring.threads.virtual.enabled=true`).
* Avoid `synchronized` blocks around blocking I/O to prevent thread pinning; use `ReentrantLock` if locking is necessary.

### 3.5 Text Blocks
Use Multiline Text Blocks (`"""..."""`) for SQL queries, JSON templates, or multi-line strings.
```java
String sql = """
    SELECT u.id, u.email, u.status 
    FROM users u 
    WHERE u.status = :status AND u.created_at >= :startDate
    """;
```

---

## 4. Spring Boot 3.4+ Best Practices

### 4.1 Dependency Injection
* **Constructor Injection Only:** Field injection (`@Autowired` on fields) is **strictly forbidden**.
* Keep constructor parameter lists reasonable (max 5 parameters). If more are needed, refactor the class to respect Single Responsibility.
```java
@Service
@RequiredArgsConstructor // Lombok constructor injection
public class OrderService implements CreateOrderUseCase {
    private final OrderRepositoryPort orderRepository;
    private final PaymentGatewayPort paymentGateway;
}
```

### 4.2 Configuration Properties
Use strongly-typed `@ConfigurationProperties` records instead of `@Value`.
```java
@ConfigurationProperties(prefix = "app.payment")
public record PaymentProperties(
    String apiKey,
    URI baseUrl,
    Duration timeout
) {}
```

### 4.3 Validation
* Validate all incoming REST requests using `jakarta.validation` annotations on Request DTO records.
* Trigger validation with `@Valid` on Controller parameters.

---

## 5. Persistence & PostgreSQL Standards

### 5.1 Separation of Domain Models & Database Entities
* **Never use `@Entity` annotations on Domain Models.**
* Database entities reside strictly in `infrastructure/persistence/entity/`.
* Domain aggregates reside in `domain/model/`.
* Use MapStruct mappers to translate between JPA Entities and Domain Models.

### 5.2 Database Migrations (Liquibase)
* **Never** use `hibernate.hbm2ddl.auto = update` or `create`.
* All database schema changes MUST be managed through Liquibase changelogs (`src/main/resources/db/changelog/`).
* Changelogs must be formatted in SQL or YAML and split by version:
    * `db/changelog/db.changelog-master.xml`
    * `db/changelog/changes/001-initial-schema.sql`
    * `db/changelog/changes/002-add-index-users-email.sql`

### 5.3 Naming & Primary Keys
* **Primary Keys:** Use `UUID` (v7 preferred) or `BIGINT` (dependent on performance requirements).
* **Naming Conventions:**
    * Tables: `snake_case`, plural (e.g., `orders`, `user_accounts`).
    * Columns: `snake_case` (e.g., `created_at`, `payment_status`).
    * Foreign Keys: `fk_<source_table>_<target_table>` (e.g., `fk_orders_user_accounts`).
    * Indexes: `idx_<table_name>_<column_name>` (e.g., `idx_user_accounts_email`).

### 5.4 Performance & Query Optimization
* Always define fetch types explicitly: default to `FetchType.LAZY` on `@ManyToOne` and `@OneToMany` relationships.
* Avoid N+1 query issues by using `JOIN FETCH` or Spring Data JPA `@EntityGraph`.
* Read-only operations must be annotated with `@Transactional(readOnly = true)`.
### 5.5 Money
Money computations only using BigDecimal from default java library, do NOT use float or double for money computations.

---

## 6. Error Handling & REST API Design

### 6.1 Unified Exception Handling
Use `@RestControllerAdvice` and RFC 7807 **Problem Details** (`ProblemDetail`) for structured error responses.

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainNotFoundException.class)
    public ProblemDetail handleNotFound(DomainNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND, 
            ex.getMessage()
        );
        problem.setTitle("Resource Not Found");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}
```

### 6.2 Standard HTTP Status Codes
* `200 OK`: Successful fetch or update.
* `201 Created`: Successful creation (include `Location` header).
* `204 No Content`: Successful deletion or action with no response body.
* `400 Bad Request`: Validation failure or malformed body.
* `401 Unauthorized`: Missing or invalid authentication token.
* `403 Forbidden`: Authenticated user lacks permission.
* `404 Not Found`: Entity does not exist.
* `409 Conflict`: Business rule violation (e.g., duplicate email).
* `500 Internal Server Error`: Unhandled application exception.

---

## 7. Testing Strategy

We enforce the **Testing Pyramid**: Unit Tests > Integration Tests > E2E Tests.

### 7.1 Unit Tests (JUnit 5 + Mockito / AssertJ)
* Target Domain models, Application Services, and pure logic.
* Fast, lightweight, no Spring context loading.
```java
class OrderServiceTest {

    @Test
    void shouldCreateOrderSuccessfully() {
        // Given
        var command = new CreateOrderCommand(...);
        // When & Then
        assertThat(result).isNotNull();
    }
}
```

### 7.2 Integration Tests (`@SpringBootTest` + Testcontainers)
* Used for Testing Repositories, DB Queries, and External Adapter integration.
* **Mandatory:** Use **Testcontainers** for PostgreSQL integration tests. Do NOT use H2 (H2 does not accurately represent PostgreSQL JSONB, locking, or dialect behaviors).

```java
@SpringBootTest
@Testcontainers
class UserRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
}
```

---

## 8. Guidelines for AI Agents & LLMs

When generating, refactoring, or inspecting code in this project, **AI Agents MUST strictly adhere to the following rules**:

1. **Context Boundary Rules:**
    * When creating a new feature, construct the **Domain Model/Port** first, followed by **Application Service**, then **Infrastructure Adapters**, and finally **Controllers**.
    * Never introduce Spring imports inside `domain/`.
2. **DTO & Entity Isolation:**
    * Do NOT return `@Entity` objects directly from REST controllers.
    * Do NOT pass REST Request DTOs into application or domain services. Convert them to Domain Commands/Objects via MapStruct or explicit mapping.
3. **No Unneeded Abstractions:**
    * Do NOT create interfaces for Application Services if there is only one implementation (e.g., `UserService` does not need `UserServiceImpl`). Interfaces ARE required for Infrastructure Ports (e.g., `UserRepositoryPort`).
4. **Immutability First:**
    * Use Java `record` for DTOs and Value Objects.
    * Mark all fields in entities/classes `private final` unless mutability is explicitly required by JPA/Hibernate.
5. **Modification Protocol:**
    * When altering DB schema, ALWAYS generate a corresponding Liquibase XML/SQL migration script in `src/main/resources/db/changelog/changes/`. Do NOT modify existing executed migration scripts.
    * Include corresponding JUnit/Testcontainers tests for any newly added business logic or database queries.