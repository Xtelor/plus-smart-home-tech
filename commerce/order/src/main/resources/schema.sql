-- таблица заказов
CREATE TABLE IF NOT EXISTS orders (
    order_id CHAR(36) NOT NULL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    shopping_cart_id CHAR(36),
    payment_id CHAR(36),
    delivery_id CHAR(36),
    order_state VARCHAR(30),
    delivery_weight DOUBLE PRECISION,
    delivery_volume DOUBLE PRECISION,
    fragile BOOLEAN,
    total_price DECIMAL(12,2) DEFAULT 0.0 CHECK (total_price >= 0.0),
    delivery_price DECIMAL(12,2) DEFAULT 0.0 CHECK ( delivery_price >= 0.0),
    product_price DECIMAL(12,2) DEFAULT 0.0 CHECK (product_price >= 0.0)
);

CREATE TABLE IF NOT EXISTS orders_products (
    order_id CHAR(36) NOT NULL,
    product_id CHAR(36) NOT NULL,
    quantity BIGINT NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);