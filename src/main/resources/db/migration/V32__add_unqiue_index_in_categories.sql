ALTER TABLE categories
ADD CONSTRAINT uq_categories_name_parent_id UNIQUE (name, parent_id);