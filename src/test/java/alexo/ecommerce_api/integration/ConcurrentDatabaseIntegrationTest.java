package alexo.ecommerce_api.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class ConcurrentDatabaseIntegrationTest extends PostgresIntegrationTest {

    @Autowired
    private CatalogInventoryFixture catalogInventoryFixture;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @Test
    void shouldNotOversellWhenManyTransactionsDecrementOneStockRow() {
        CatalogInventoryFixture.CatalogInventoryData inventory =
                catalogInventoryFixture.createCatalogInventory();
        long stockId = inventory.stock().getId();
        int initialQuantity = inventory.stock().getPhysicalQuantity();
        int workerCount = 8;
        int attemptsPerWorker = 4;

        ExecutorService executor = Executors.newFixedThreadPool(workerCount);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<Integer>> futures = new ArrayList<>();

        try {
            for (int worker = 0; worker < workerCount; worker++) {
                futures.add(executor.submit(() -> {
                    start.await();
                    int successfulUpdates = 0;
                    for (int attempt = 0; attempt < attemptsPerWorker; attempt++) {
                        successfulUpdates += jdbcTemplate.update("""
                                update product_wh_stocks
                                set physical_quantity = physical_quantity - 1,
                                    updated_at = current_timestamp
                                where id = ?
                                  and physical_quantity - reserved_quantity >= 1
                                """, stockId);
                    }
                    return successfulUpdates;
                }));
            }

            start.countDown();

            int successfulUpdates = futures.stream()
                    .mapToInt(future -> getIntegerResultWithinTimeout(future, 10))
                    .sum();
            Integer finalQuantity = jdbcTemplate.queryForObject(
                    "select physical_quantity from product_wh_stocks where id = ?",
                    Integer.class,
                    stockId
            );

            assertThat(successfulUpdates).isEqualTo(initialQuantity);
            assertThat(finalQuantity).isZero();
        } finally {
            executor.shutdownNow();
        }
    }

    @Test
    void shouldCompleteConcurrentTransactionsWhenTheyLockStocksInSameOrder() {
        CatalogInventoryFixture.CatalogInventoryData first =
                catalogInventoryFixture.createCatalogInventory();
        CatalogInventoryFixture.CatalogInventoryData second =
                catalogInventoryFixture.createCatalogInventory();
        List<Long> stockIds = Stream.of(first.stock().getId(), second.stock().getId())
                .sorted(Comparator.naturalOrder())
                .toList();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<Void>> futures = List.of(
                executor.submit(() -> {
                    lockStocksInOrder(start, stockIds);
                    return null;
                }),
                executor.submit(() -> {
                    lockStocksInOrder(start, stockIds);
                    return null;
                })
        );

        try {
            start.countDown();
                assertThatCode(() -> futures.forEach(future -> awaitCompletion(future, 10)))
                    .doesNotThrowAnyException();
        } finally {
            executor.shutdownNow();
        }
    }

    private void lockStocksInOrder(CountDownLatch start, List<Long> stockIds) throws Exception {
        start.await();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(
                    "select id from product_wh_stocks where id = ? for update")) {
                for (Long stockId : stockIds) {
                    statement.setLong(1, stockId);
                    try (ResultSet ignored = statement.executeQuery()) {
                        assertThat(ignored.next()).isTrue();
                    }
                }
            }
            connection.commit();
        }
    }

    private static int getIntegerResultWithinTimeout(Future<Integer> future, int timeoutSeconds) {
        try {
            return future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (Exception exception) {
            throw new AssertionError("Concurrent stock update did not finish in time", exception);
        }
    }

    private static void awaitCompletion(Future<Void> future, int timeoutSeconds) {
        try {
            future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (Exception exception) {
            throw new AssertionError("Concurrent stock locking did not finish in time", exception);
        }
    }
}
