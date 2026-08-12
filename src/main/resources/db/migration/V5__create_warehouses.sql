CREATE TABLE warehouses (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address_id BIGINT NOT NULL REFERENCES addresses(id),
    CONSTRAINT uq_warehouses_address_id_name UNIQUE (address_id, name)
);
CREATE INDEX idx_warehouses_address_id ON warehouses(address_id);
