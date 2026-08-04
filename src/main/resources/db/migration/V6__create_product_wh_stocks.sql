CREATE TABLE product_wh_stocks (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    physical_quantity INT NOT NULL,
    reserved_quantity INT NOT NULL,
    product_id BIGINT NOT NULL REFERENCES products(id),
    warehouse_id BIGINT NOT NULL REFERENCES warehouses(id),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_product_wh UNIQUE (product_id, warehouse_id)
);
CREATE INDEX idx_product_wh_stocks_product_id ON product_wh_stocks(product_id);
CREATE INDEX idx_product_wh_stocks_warehouse_id ON product_wh_stocks(warehouse_id);
