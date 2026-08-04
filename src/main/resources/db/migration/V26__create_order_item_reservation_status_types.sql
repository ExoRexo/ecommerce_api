CREATE TABLE order_item_reservation_status_types (
    id SMALLINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    description TEXT NOT NULL
);
