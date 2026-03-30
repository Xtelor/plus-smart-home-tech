-- таблица склада
CREATE TABLE IF NOT EXISTS warehouse_products (
    product_id CHAR(36) PRIMARY KEY,
    fragile BOOLEAN,
    product_width DOUBLE PRECISION NOT NULL CHECK (product_width >= 1.0),
    product_height DOUBLE PRECISION NOT NULL CHECK (product_height >= 1.0),
    product_depth DOUBLE PRECISION NOT NULL CHECK (product_depth >= 1.0),
    weight DOUBLE PRECISION NOT NULL CHECK (weight >= 1.0),
    quantity BIGINT NOT NULL DEFAULT 0 CHECK (quantity >= 1)
);