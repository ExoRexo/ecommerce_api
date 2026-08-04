CREATE TABLE customer_orders (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES customers(user_id),
    status_type_id SMALLINT NOT NULL REFERENCES customer_order_status_types(id)
);

CREATE INDEX idx_customer_orders_customer_id ON customer_orders(customer_id);
CREATE INDEX idx_customer_orders_status_type_id ON customer_orders(status_type_id);
