-- Localizes dictionary labels and descriptions to Russian.

INSERT INTO product_status_types (code, label, description)
VALUES
    ('ACTIVE', 'Активен', 'Товар активен и доступен для продажи.'),
    ('UNACTIVE', 'Неактивен', 'Товар неактивен и скрыт с витрины.')
ON CONFLICT (code) DO UPDATE
SET label = EXCLUDED.label,
    description = EXCLUDED.description;

INSERT INTO user_status_types (code, label, description)
VALUES
    ('ACTIVE', 'Активен', 'Учетная запись пользователя активна и может входить в систему.'),
    ('UNACTIVE', 'Неактивен', 'Учетная запись пользователя неактивна, доступ ограничен.')
ON CONFLICT (code) DO UPDATE
SET label = EXCLUDED.label,
    description = EXCLUDED.description;

INSERT INTO customer_order_status_types (code, label, description)
VALUES
    ('CREATED', 'Создан', 'Заказ создан и ожидает дальнейшей обработки.'),
    ('PENDING_PAYMENT', 'Ожидает оплаты', 'Заказ ожидает проведения оплаты.'),
    ('PAID', 'Оплачен', 'Оплата по заказу успешно получена.'),
    ('COMPLETED', 'Завершен', 'Жизненный цикл заказа завершен.'),
    ('CANCELLED', 'Отменен', 'Заказ отменен до завершения.')
ON CONFLICT (code) DO UPDATE
SET label = EXCLUDED.label,
    description = EXCLUDED.description;

INSERT INTO order_item_reservation_status_types (code, label, description)
VALUES
    ('ACTIVE', 'Активно', 'Резерв активен и удерживает складской остаток под позицию заказа.'),
    ('CANCELLED', 'Отменено', 'Резерв отменен, остаток возвращен в доступный.'),
    ('FINISHED', 'Завершено', 'Резерв использован при отгрузке и завершен.')
ON CONFLICT (code) DO UPDATE
SET label = EXCLUDED.label,
    description = EXCLUDED.description;

INSERT INTO c_wallt_transaction_purpose_types (code, label, description)
VALUES
    ('WITHDRAWAL', 'Списание', 'Средства списываются с кошелька клиента.'),
    ('TOP_UP', 'Пополнение', 'Средства зачисляются на кошелек клиента.')
ON CONFLICT (code) DO UPDATE
SET label = EXCLUDED.label,
    description = EXCLUDED.description;

INSERT INTO wh_st_transaction_purpose_types (code, label, description)
VALUES
    ('SALE', 'Продажа', 'Остаток уменьшен в результате продажи клиенту.'),
    ('PURCHASE', 'Закупка', 'Остаток увеличен в результате закупки.'),
    ('INVENTORY_ADJUSTMENT', 'Инвентаризационная корректировка', 'Остаток скорректирован вручную по итогам инвентаризации.')
ON CONFLICT (code) DO UPDATE
SET label = EXCLUDED.label,
    description = EXCLUDED.description;

INSERT INTO roles (code, label, description)
VALUES
    ('ADMIN', 'Администратор', 'Администратор платформы с полным доступом.'),
    ('MANAGER', 'Менеджер', 'Менеджер операций с доступом к управлению каталогом и заказами.'),
    ('CUSTOMER', 'Клиент', 'Конечный пользователь для покупок и оформления заказов.')
ON CONFLICT (code) DO UPDATE
SET label = EXCLUDED.label,
    description = EXCLUDED.description;
