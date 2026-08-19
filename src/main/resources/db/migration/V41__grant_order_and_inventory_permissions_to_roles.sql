-- Adds order and inventory permissions and grants them to business roles.

INSERT INTO permissions (code, label, description)
VALUES
    ('CUSTOMER_ORDER_CANCEL', 'Отмена заказа', 'Разрешает отмену заказа.'),
    ('CUSTOMER_ORDER_COMPLETE', 'Завершение заказа', 'Разрешает завершение заказа.'),
    ('INVENTORY_PRODUCT_STOCK_MANAGEMENT_UPDATE_WAREHOUSE_STOCK', 'Изменение складских остатков', 'Разрешает изменение остатков товара на складе.'),
    ('INVENTORY_PRODUCT_STOCK_MANAGEMENT_READ_PRODUCT_WH_STOCKS_LIST', 'Просмотр остатков по складам', 'Разрешает просмотр списка остатков товара по складам.'),
    ('INVENTORY_PRODUCT_STOCK_MANAGEMENT_READ_WH_TRANSACTIONS_LIST', 'Просмотр складских транзакций', 'Разрешает просмотр списка складских транзакций.'),
    ('INVENTORY_WAREHOUSE_CREATE', 'Создание склада', 'Разрешает создание складов.'),
    ('INVENTORY_WAREHOUSE_UPDATE', 'Обновление склада', 'Разрешает обновление данных склада.'),
    ('INVENTORY_WAREHOUSE_READ_LIST', 'Просмотр списка складов', 'Разрешает просмотр списка складов.')
ON CONFLICT (code) DO UPDATE
SET label = EXCLUDED.label,
    description = EXCLUDED.description;

-- MANAGER receives order and inventory operational permissions.
INSERT INTO role_permissions (permission_id, role_id)
SELECT p.id, r.id
FROM permissions p
JOIN roles r ON r.code = 'MANAGER'
WHERE p.code IN (
    'CUSTOMER_ORDER_CANCEL',
    'CUSTOMER_ORDER_COMPLETE',
    'INVENTORY_PRODUCT_STOCK_MANAGEMENT_UPDATE_WAREHOUSE_STOCK',
    'INVENTORY_PRODUCT_STOCK_MANAGEMENT_READ_PRODUCT_WH_STOCKS_LIST',
    'INVENTORY_PRODUCT_STOCK_MANAGEMENT_READ_WH_TRANSACTIONS_LIST',
    'INVENTORY_WAREHOUSE_CREATE',
    'INVENTORY_WAREHOUSE_UPDATE',
    'INVENTORY_WAREHOUSE_READ_LIST'
)
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- CUSTOMER receives own order lifecycle permissions.
INSERT INTO role_permissions (permission_id, role_id)
SELECT p.id, r.id
FROM permissions p
JOIN roles r ON r.code = 'CUSTOMER'
WHERE p.code = 'CUSTOMER_ORDER_CANCEL'
ON CONFLICT (role_id, permission_id) DO NOTHING;
