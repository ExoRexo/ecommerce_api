CREATE TABLE products (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    price_rub NUMERIC(10, 2) NOT NULL,
    status_type_id SMALLINT NOT NULL REFERENCES product_status_types(id),
    category_id BIGINT NOT NULL REFERENCES categories(id)
);
CREATE INDEX idx_products_category_id ON products(category_id);
CREATE INDEX idx_products_status_type_id ON products(status_type_id);
