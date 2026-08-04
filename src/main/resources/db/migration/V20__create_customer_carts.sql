CREATE TABLE customer_carts (
    customer_id BIGINT PRIMARY KEY REFERENCES customers(user_id) ON DELETE CASCADE
);
