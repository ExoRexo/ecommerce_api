# Software Engineering Conventions & Architecture Guidelines
**Project Stack:** Java 25 | Spring Boot 3.4+ | PostgreSQL 16+ | Flyway

---

## 1. Overview & Core Philosophy

This document serves as the single source of truth for architectural standards, code style, and development patterns for both **human developers** and **AI coding assistants** working on this repository.

### Strategic Priorities
1. **Predictability over Cleverness:** Code should be simple, explicit, and easy to read. Avoid obscure language features or overly complex abstractions.
2. **Type Safety & Immutability:** Leverage modern Java 25 features (Records, Pattern Matching, Sealed Classes) to build compile-time safe data structures.
3. **Layered Separation of Concerns:** Maintain clean boundaries between presentation, service (business logic), data access (persistence), and external integrations.
4. **AI-Friendly Architecture:** Code structures, naming conventions, and project hierarchy must be standardized so LLMs/AI agents can accurately navigate, reason about, and modify the codebase without context bleeding.

---

## 2. Project Architecture & Layering

We follow a classic **Layered Architecture (N-Tier Architecture)** pattern.

```
src/main/java/com/project/
├── controller/              # Presentation Layer (REST Controllers, Web API)
│   ├── request/             # Request DTOs (Validation tags)
│   └── response/            # Response DTOs
│
├── service/                 # Service / Business Logic Layer
│   ├── dto/                 # Internal Service DTOs / Commands
│   └── exception/           # Custom Business Exceptions
│
├── repository/              # Data Access Layer (Spring Data JPA Repositories)
│   └── entity/              # JPA / Hibernate Entities
│
├── client/                  # External API Integrations (Feign, WebClient, REST Clients)
├── config/                  # Spring Configuration Beans & Properties
└── mapper/                  # MapStruct Mappers (DTO <-> Entity / DTO <-> Response)
```

### Layer Interaction Rules
* **Controller Layer:** Handles HTTP requests, input validation, and delegates business operations to the Service layer. It returns Response DTOs.
* **Service Layer:** Contains core business logic, transaction boundaries (`@Transactional`), and orchestrates operations between Repositories and External Clients.
* **Repository Layer:** Handles database persistence and queries using JPA/Hibernate Entities.
* **Dependency Flow:** Strictly top-down: `Controller` -> `Service` -> `Repository`. Lower layers MUST NOT depend on higher layers.

---

## 3. Java 25 Standards & Language Features

We leverage modern Java 25 standards. All code must adhere to these language features:

### 3.1 Records for Data Carriers
Use `record` for all read-only Data Transfer Objects (DTOs), API payloads, and internal communication events.
```java
// Preferred for DTOs and API Payloads
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

## 4. Documentation & Javadoc Standards

Documentation is critical for both long-term maintenance and context awareness for AI assistants.

### 4.1 When Javadoc is Required
* **Service Interfaces & Public Methods:** All public service methods must have Javadoc explaining business intent, preconditions, and potential exceptions.
* **Complex Business Logic:** Any public method containing multi-step algorithms, calculations, or non-trivial state transitions.
* **Configuration & External Clients:** Custom Spring configurations, bean factories, or integration clients.

### 4.2 When Javadoc is Omitted (Self-Documenting Code)
* Simple getters/setters, `record` components, or trivial CRUD implementations where method signature and type names make the intent obvious.
* REST Controller endpoints if OpenAPI/Swagger annotations (`@Operation`, `@ApiResponse`) are already present.

### 4.3 Javadoc Style Guidelines
* **Intent-Oriented:** Describe *why* the method exists and *what* business requirement it fulfills, not *how* the Java code is implemented.
* **Markdown Snippets:** Use modern Javadoc tags (`{@snippet ...}`) introduced in Java 18+ for embedding code examples.
* **Records Documentation:** Document record components directly using `@param` tags on the record header Javadoc.

```java
/**
 * Service for orchestrating order creation and payment processing.
 */
public interface OrderService {

    /**
     * Processes a new user order, reserves stock, and initializes payment.
     *
     * @param dto payload containing target product IDs, quantities, and user identity
     * @return the unique identifiers and initial state of the created order
     * @throws InsufficientStockException if requested items exceed available database inventory
     * @throws PaymentInitializationException if third-party gateway rejects transaction
     */
    OrderResponseDto createOrder(CreateOrderRequestDto dto);
}
```

---

## 5. Spring Boot 3.4+ Best Practices

### 5.1 Dependency Injection
* **Constructor Injection Only:** Field injection (`@Autowired` on fields) is **strictly forbidden**.
* Keep constructor parameter lists reasonable (max 5 parameters). If more are needed, refactor the class to respect Single Responsibility.
```java
@Service
@RequiredArgsConstructor // Lombok constructor injection
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final PaymentClient paymentClient;
    private final OrderMapper orderMapper;
}
```

### 5.2 Configuration Properties
Use strongly-typed `@ConfigurationProperties` records instead of `@Value`.
```java
@ConfigurationProperties(prefix = "app.payment")
public record PaymentProperties(
    String apiKey,
    URI baseUrl,
    Duration timeout
) {}
```

### 5.3 Validation
* Validate all incoming REST requests using `jakarta.validation` annotations on Request DTO records.
* Trigger validation with `@Valid` on Controller parameters.

---

## 6. Persistence & PostgreSQL Standards

### 6.1 Entities & Data Access
* Entities reside strictly in `repository/entity/`.
* Use MapStruct mappers to translate between JPA Entities and DTOs.
* Do **NOT** expose `@Entity` classes directly outside the Service/Repository layers (never return entities from REST Controllers).

### 6.2 Database Migrations (Flyway)
* **Never** use `hibernate.hbm2ddl.auto = update` or `create`.
* All database schema changes MUST be managed through Flyway migrations in `src/main/resources/db/migration/` (e.g., `V1__initial_schema.sql`, `V2__add_index_users_email.sql`).

### 6.3 Naming & Primary Keys
* **Primary Keys:** Use `BIGINT`, `INT`, or `UUID` (dependent on performance requirements).
* **Naming Conventions:**
    * Tables: `snake_case`, plural (e.g., `orders`, `user_accounts`).
    * Columns: `snake_case` (e.g., `created_at`, `payment_status`).
    * Foreign Keys: `fk_<source_table>_<target_table>` (e.g., `fk_orders_user_accounts`).
    * Indexes: `idx_<table_name>_<column_name>` (e.g., `idx_user_accounts_email`).

### 6.4 Performance & Query Optimization
* Always define fetch types explicitly: default to `FetchType.LAZY` on `@ManyToOne` and `@OneToMany` relationships.
* Avoid N+1 query issues by using `JOIN FETCH` or Spring Data JPA `@EntityGraph`.
* Read-only operations must be annotated with `@Transactional(readOnly = true)`.

---

## 7. Error Handling & REST API Design

### 7.1 Unified Exception Handling
Use `@RestControllerAdvice` and RFC 7807 **Problem Details** (`ProblemDetail`) for structured error responses.

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleNotFound(EntityNotFoundException ex) {
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

### 7.2 Standard HTTP Status Codes
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

## 8. Testing Strategy

We enforce the **Testing Pyramid**: Unit Tests > Integration Tests > E2E Tests.

### 8.1 Unit Tests (JUnit 5 + Mockito / AssertJ)
* Target Services and business logic components.
* Fast, lightweight, no Spring context loading.
```java
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void shouldCreateOrderSuccessfully() {
        // Given
        var request = new CreateOrderRequestDto(...);
        // When & Then
        assertThat(result).isNotNull();
    }
}
```

### 8.2 Integration Tests (`@SpringBootTest` + Testcontainers)
* Used for Testing Repositories, DB Queries, and External Client integration.
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

## 9. Guidelines for AI Agents & LLMs

When generating, refactoring, or inspecting code in this project, **AI Agents MUST strictly adhere to the following rules**:

1. **Context Boundary Rules:**
    * When creating a new feature, follow the top-down or bottom-up layered workflow: `Entity` -> `Repository` -> `Service` -> `Controller` (with DTOs and Mappers).
    * Ensure strict dependency order (`Controller` -> `Service` -> `Repository`). Never inject Repositories directly into Controllers.
2. **DTO & Entity Isolation:**
    * Do NOT return `@Entity` objects directly from REST controllers or expose them as API responses.
    * Map Request DTOs to Entities/Service DTOs before passing them to business logic.
3. **No Unneeded Abstractions:**
    * Do NOT create unnecessary interface layers if there is only one implementation (unless explicitly required by contract or team standards).
4. **Immutability First:**
    * Use Java `record` for Request/Response DTOs and internal data carriers.
    * Keep fields encapsulated and private.
5. **Modification Protocol:**
    * When altering DB schema, ALWAYS generate a corresponding Flyway SQL migration script (`V<Version>__<Description>.sql`) in `src/main/resources/db/migration/`. Do NOT modify existing executed migration scripts.
    * Include corresponding JUnit/Testcontainers tests for any newly added business logic or database queries.
6. **Documentation Compliance:**
    * Always write clear Javadoc for public service methods, complex business logic, and custom interfaces generated or updated during the task.