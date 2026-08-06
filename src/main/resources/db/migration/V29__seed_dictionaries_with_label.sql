-- Seeds and synchronizes dictionary values using code as business key.

INSERT INTO product_status_types (code, label, description)
VALUES
    ('ACTIVE', 'Active', 'Product is active and available for sale.'),
    ('UNACTIVE', 'Inactive', 'Product is inactive and hidden from sale.')
ON CONFLICT (code) DO UPDATE
SET label = EXCLUDED.label,
    description = EXCLUDED.description;

INSERT INTO user_status_types (code, label, description)
VALUES
    ('ACTIVE', 'Active', 'User account is active and allowed to sign in.'),
    ('UNACTIVE', 'Inactive', 'User account is inactive and access is restricted.')
ON CONFLICT (code) DO UPDATE
SET label = EXCLUDED.label,
    description = EXCLUDED.description;

INSERT INTO customer_order_status_types (code, label, description)
VALUES
    ('CREATED', 'Created', 'Order was created and is awaiting further processing.'),
    ('PENDING_PAYMENT', 'Pending Payment', 'Order is waiting for a payment transaction.'),
    ('PAID', 'Paid', 'Payment was received successfully for the order.'),
    ('COMPLETED', 'Completed', 'Order lifecycle is completed and closed.'),
    ('CANCELLED', 'Cancelled', 'Order was cancelled before completion.')
ON CONFLICT (code) DO UPDATE
SET label = EXCLUDED.label,
    description = EXCLUDED.description;

INSERT INTO order_item_reservation_status_types (code, label, description)
VALUES
    ('ACTIVE', 'Active', 'Reservation is active and blocks stock for the order item.'),
    ('CANCELLED', 'Cancelled', 'Reservation was cancelled and stock was released.'),
    ('FINISHED', 'Finished', 'Reservation was consumed during fulfillment and is finished.')
ON CONFLICT (code) DO UPDATE
SET label = EXCLUDED.label,
    description = EXCLUDED.description;

INSERT INTO c_wallt_transaction_purpose_types (code, label, description)
VALUES
    ('WITHDRAWAL', 'Withdrawal', 'Funds are withdrawn from the customer wallet.'),
    ('TOP_UP', 'Top-up', 'Funds are added to the customer wallet.')
ON CONFLICT (code) DO UPDATE
SET label = EXCLUDED.label,
    description = EXCLUDED.description;

INSERT INTO wh_st_transaction_purpose_types (code, label, description)
VALUES
    ('SALE', 'Sale', 'Stock was decreased due to customer sale.'),
    ('PURCHASE', 'Purchase', 'Stock was increased due to procurement.'),
    ('INVENTORY_ADJUSTMENT', 'Inventory Adjustment', 'Stock was corrected by manual inventory adjustment.')
ON CONFLICT (code) DO UPDATE
SET label = EXCLUDED.label,
    description = EXCLUDED.description;

INSERT INTO roles (code, label, description)
VALUES
    ('ADMIN', 'Administrator', 'Platform administrator with full access rights.'),
    ('MANAGER', 'Manager', 'Operations manager with catalog and order management rights.'),
    ('CUSTOMER', 'Customer', 'End user role for shopping and order placement.')
ON CONFLICT (code) DO UPDATE
SET label = EXCLUDED.label,
    description = EXCLUDED.description;
