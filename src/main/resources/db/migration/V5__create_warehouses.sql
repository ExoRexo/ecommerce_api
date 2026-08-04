CREATE TABLE warehouses (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100),
    address_id BIGINT NOT NULL REFERENCES addresses(id)
);
CREATE INDEX idx_warehouses_address_id ON warehouses(address_id);
