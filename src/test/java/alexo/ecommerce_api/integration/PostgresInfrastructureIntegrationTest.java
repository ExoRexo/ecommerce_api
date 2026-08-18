package alexo.ecommerce_api.integration;

import alexo.ecommerce_api.repository.lock.AdvisoryLockRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

class PostgresInfrastructureIntegrationTest extends PostgresIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AdvisoryLockRepository advisoryLockRepository;

    @Test
    void shouldRunFlywayMigrationsAgainstPostgres() {
        Integer productStockTableCount = jdbcTemplate.queryForObject(
                "select count(*) from information_schema.tables where table_name = 'product_wh_stocks'",
                Integer.class
        );

        assertThat(productStockTableCount).isEqualTo(1);
    }

    @Test
    @Transactional
    void shouldAcquireTransactionAdvisoryLock() {
        advisoryLockRepository.acquireTransactionLock(
                AdvisoryLockRepository.LockCode.INVENTORY_PRODUCT_WAREHOUSE_STOCK_MATRIX_LOCK_KEY
        );

        Integer advisoryLockFunctionResult = jdbcTemplate.queryForObject(
                "select 1 where pg_try_advisory_xact_lock(1)",
                Integer.class
        );

        assertThat(advisoryLockFunctionResult).isEqualTo(1);
    }
}
