-- Seeds permissions and grants them to MANAGER and CUSTOMER roles (without ADMIN assignments).

INSERT INTO permissions (code, label, description)
VALUES
    ('CATALOG_PRODUCT_CREATE', 'Создание товаров', 'Разрешает создание товаров в каталоге.'),
    ('CATALOG_PRODUCT_READ_LIST', 'Просмотр списка товаров', 'Разрешает просмотр списка товаров.'),
    ('CATALOG_PRODUCT_READ_STATUS_TYPES', 'Просмотр статусов товаров', 'Разрешает просмотр справочника статусов товаров.'),
    ('CATALOG_PRODUCT_UPDATE', 'Обновление товаров', 'Разрешает обновление полей товара.'),
    ('CATALOG_PRODUCT_UPDATE_PRICE_RUB', 'Обновление цены товара', 'Разрешает изменение цены товара в рублях.'),
    ('CATALOG_CATEGORY_CREATE', 'Создание категорий', 'Разрешает создание категорий каталога.'),
    ('CATALOG_CATEGORY_UPDATE', 'Обновление категорий', 'Разрешает изменение категорий каталога.'),
    ('CATALOG_CATEGORY_READ_LIST', 'Просмотр списка категорий', 'Разрешает просмотр списка категорий.'),
    ('CATALOG_CATEGORY_READ_TREE', 'Просмотр дерева категорий', 'Разрешает просмотр дерева категорий.'),
    ('CATALOG_CATEGORY_READ_CONCRETE', 'Просмотр категории', 'Разрешает просмотр конкретной категории.')
ON CONFLICT (code) DO UPDATE
SET label = EXCLUDED.label,
    description = EXCLUDED.description;

-- MANAGER receives full catalog permissions.
INSERT INTO role_permissions (permission_id, role_id)
SELECT p.id, r.id
FROM permissions p
JOIN roles r ON r.code = 'MANAGER'
WHERE p.code IN (
    'CATALOG_PRODUCT_CREATE',
    'CATALOG_PRODUCT_READ_LIST',
    'CATALOG_PRODUCT_READ_STATUS_TYPES',
    'CATALOG_PRODUCT_UPDATE',
    'CATALOG_PRODUCT_UPDATE_PRICE_RUB',
    'CATALOG_CATEGORY_CREATE',
    'CATALOG_CATEGORY_UPDATE',
    'CATALOG_CATEGORY_READ_LIST',
    'CATALOG_CATEGORY_READ_TREE',
    'CATALOG_CATEGORY_READ_CONCRETE'
)
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- CUSTOMER (user) receives read-only catalog permissions.
INSERT INTO role_permissions (permission_id, role_id)
SELECT p.id, r.id
FROM permissions p
JOIN roles r ON r.code = 'CUSTOMER'
WHERE p.code IN (
    'CATALOG_PRODUCT_READ_LIST',
    'CATALOG_PRODUCT_READ_STATUS_TYPES',
    'CATALOG_CATEGORY_READ_LIST',
    'CATALOG_CATEGORY_READ_TREE',
    'CATALOG_CATEGORY_READ_CONCRETE'
)
ON CONFLICT (role_id, permission_id) DO NOTHING;
