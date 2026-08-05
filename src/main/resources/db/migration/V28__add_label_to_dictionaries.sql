-- Adds label column to all dictionary tables.

ALTER TABLE product_status_types ADD COLUMN label VARCHAR(120);

ALTER TABLE user_status_types ADD COLUMN label VARCHAR(120);

ALTER TABLE customer_order_status_types ADD COLUMN label VARCHAR(120);

ALTER TABLE order_item_reservation_status_types ADD COLUMN label VARCHAR(120);

ALTER TABLE c_wallt_transaction_purpose_types ADD COLUMN label VARCHAR(120);

ALTER TABLE wh_st_transaction_purpose_types ADD COLUMN label VARCHAR(120);

ALTER TABLE roles ADD COLUMN label VARCHAR(120);

ALTER TABLE permissions ADD COLUMN label VARCHAR(120);
