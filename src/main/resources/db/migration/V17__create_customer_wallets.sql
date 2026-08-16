CREATE TABLE customer_wallets (
    customer_id BIGINT PRIMARY KEY REFERENCES customers(user_id) ON DELETE CASCADE,
    balance NUMERIC(10, 2) NOT NULL DEFAULT 0.00
);

CREATE INDEX idx_customer_wallets_customer_id ON customer_wallets(customer_id);
