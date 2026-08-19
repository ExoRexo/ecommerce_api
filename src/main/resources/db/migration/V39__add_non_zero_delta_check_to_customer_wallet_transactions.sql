ALTER TABLE customer_wallet_transactions
    ADD CONSTRAINT ck_customer_wallet_transactions_delta_not_zero
    CHECK (delta <> 0);
