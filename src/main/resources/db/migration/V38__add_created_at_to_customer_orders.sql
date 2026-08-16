ALTER TABLE customer_orders ADD COLUMN created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP;
CREATE INDEX idx_customer_orders_created_at ON customer_orders(created_at);