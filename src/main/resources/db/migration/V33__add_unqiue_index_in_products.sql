ALTER TABLE products
ADD CONSTRAINT uq_products_name_category_id UNIQUE (name, category_id);
