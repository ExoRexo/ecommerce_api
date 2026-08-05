CREATE TABLE order_item_wh_reservations (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    order_item_id BIGINT NOT NULL REFERENCES order_items(id) ON DELETE CASCADE,
    reserved_quantity INT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    warehouse_id BIGINT NOT NULL REFERENCES warehouses(id),
    status_type_id SMALLINT NOT NULL REFERENCES order_item_reservation_status_types(id)
);

CREATE INDEX idx_reservations_order_item_id ON order_item_wh_reservations(order_item_id);
CREATE INDEX idx_reservations_warehouse_id ON order_item_wh_reservations(warehouse_id);
CREATE INDEX idx_reservations_status_type_id ON order_item_wh_reservations(status_type_id);
