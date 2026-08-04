CREATE TABLE warehouse_stock_transactions (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(id),
    warehouse_id BIGINT NOT NULL REFERENCES warehouses(id),
    old_quantity INT NOT NULL,
    new_quantity INT NOT NULL,
    delta INT NOT NULL,
    purpose_type_id SMALLINT NOT NULL REFERENCES wh_st_transaction_purpose_types(id),
    user_id BIGINT NOT NULL REFERENCES users(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_wh_stock_tx_product_id ON warehouse_stock_transactions(product_id);
CREATE INDEX idx_wh_stock_tx_warehouse_id ON warehouse_stock_transactions(warehouse_id);
CREATE INDEX idx_wh_stock_tx_user_id ON warehouse_stock_transactions(user_id);
