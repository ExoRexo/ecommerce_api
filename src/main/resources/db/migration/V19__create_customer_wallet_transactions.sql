CREATE TABLE customer_wallet_transactions (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    wallet_id BIGINT NOT NULL REFERENCES customer_wallets(id),
    old_balance NUMERIC(10, 2) NOT NULL,
    new_balance NUMERIC(10, 2) NOT NULL,
    delta NUMERIC(10, 2) NOT NULL,
    purpose_type_id SMALLINT NOT NULL REFERENCES c_wallt_transaction_purpose_types(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_c_wallet_tx_wallet_id ON customer_wallet_transactions(wallet_id);
CREATE INDEX idx_c_wallet_tx_purpose_type_id ON customer_wallet_transactions(purpose_type_id);
