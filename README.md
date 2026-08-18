# ecommerce_api

## Configuration Profiles

The application does not select a profile implicitly. Set `SPRING_PROFILES_ACTIVE` explicitly for every deployment.

### Local

Run the application with the local profile:

```powershell
$env:SPRING_PROFILES_ACTIVE = "local"
.\mvnw.cmd spring-boot:run
```

Spring Boot starts and stops PostgreSQL from `compose.yaml` automatically. The local profile defaults to `localhost:5432`, database `ecommerce_api`, user `postgres`, and password `postgres`. Override these values with `POSTGRES_*` environment variables when needed.

Local-only conveniences include SQL logging and Swagger UI. They must not be enabled in production.
Local responses also include exception details to simplify debugging.

### Production

```powershell
$env:SPRING_PROFILES_ACTIVE = "prod"
$env:POSTGRES_HOST = "db.internal"
$env:POSTGRES_PORT = "5432"
$env:POSTGRES_DB = "ecommerce_api"
$env:POSTGRES_USER = "ecommerce_api"
$env:POSTGRES_PASSWORD = "<secret-from-secret-manager>"
$env:JWT_SECRET = "<secret-from-secret-manager>"
$env:JWT_EXPIRATION_MS = "3600000"
.\mvnw.cmd spring-boot:run
```

Production uses bounded connection, lock, statement, and idle-transaction timeouts. SQL logging and Swagger are disabled by default.
Production responses hide exception details and return safe generic error messages. This behavior is controlled by `app.errors.include-details` and is `false` in the `prod` profile.

The main database tuning variables are:

- `DB_POOL_MAX_SIZE`
- `DB_POOL_MIN_IDLE`
- `DB_CONNECTION_TIMEOUT_MS`
- `DB_LOCK_TIMEOUT`
- `DB_STATEMENT_TIMEOUT`
- `DB_IDLE_TRANSACTION_TIMEOUT`
- `DB_MAX_LIFETIME_MS`

Do not put production credentials in `application-prod.yaml` or in source control.
