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

## Automatic Deployment

Pushes to `master` run `.github/workflows/deploy.yml`. The workflow uses Java 25,
runs `./mvnw verify` (including Testcontainers integration tests), then uploads the
tested JAR as `ecommerce_api-<run number>-<commit sha>.jar`. A failed verification
never connects to production.

The production host uses two accounts:

- `deploy` is the GitHub Actions SSH account. It can upload a JAR and invoke one
	constrained sudo command.
- `ecommerce` owns and runs the application through systemd.

Prepare the host once as an administrator:

```bash
sudo useradd --system --create-home --shell /usr/sbin/nologin ecommerce
sudo useradd --create-home --shell /bin/bash deploy
sudo install -d -o ecommerce -g ecommerce -m 0755 /opt/ecommerce-api
sudo install -d -o deploy -g ecommerce -m 0775 /opt/ecommerce-api/releases
sudo install -d -o root -g ecommerce -m 0750 /etc/ecommerce-api
sudo install -o root -g ecommerce -m 0640 /dev/null /etc/ecommerce-api/ecommerce-api.env
sudo install -o root -g root -m 0755 ops/ecommerce-api-deploy /usr/local/sbin/ecommerce-api-deploy
sudo install -o root -g root -m 0644 ops/ecommerce-api.service /etc/systemd/system/ecommerce-api.service
sudo systemctl daemon-reload
sudo systemctl enable ecommerce-api.service
```

Set `/etc/ecommerce-api/ecommerce-api.env` to the production environment variables.
It must include `SPRING_PROFILES_ACTIVE=prod`, database variables, `JWT_SECRET`, and
`JWT_EXPIRATION_MS`; use the names documented in the Production section above.

Generate a dedicated CI key locally, install only its public half for `deploy`, and
store the private half only in GitHub:

```bash
ssh-keygen -t ed25519 -f github-actions-ecommerce-deploy -C github-actions-ecommerce-deploy
sudo install -d -o deploy -g deploy -m 0700 /home/deploy/.ssh
sudo install -o deploy -g deploy -m 0600 github-actions-ecommerce-deploy.pub /home/deploy/.ssh/authorized_keys
sudo visudo -f /etc/sudoers.d/ecommerce-api-deploy
```

The sudoers file must contain exactly this command permission:

```sudoers
deploy ALL=(root) NOPASSWD: /usr/local/sbin/ecommerce-api-deploy
```

In the GitHub repository's `production` environment, configure these secrets:

- `DEPLOY_HOST`: production host name or IP address.
- `DEPLOY_PORT`: SSH port, or leave it unset for `22`.
- `DEPLOY_SSH_PRIVATE_KEY`: full contents of `github-actions-ecommerce-deploy`.
- `DEPLOY_SSH_KNOWN_HOSTS`: output of `ssh-keyscan -H -p <port> <host>`, verified
	against the server's SSH host key fingerprint before storing it.

The privileged script validates the release name, changes `current.jar` atomically,
restarts `ecommerce-api`, and requires `/actuator/health` to return `{"status":"UP"}`.
On a failed health check it switches back to the prior JAR and restarts the service;
the GitHub Actions run remains failed so the incident is visible.
