ALTER TABLE warehouse_stock_transactions
    ADD CONSTRAINT ck_warehouse_stock_transactions_delta_not_zero
    CHECK (delta <> 0);
