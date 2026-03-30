CREATE TABLE IF NOT EXISTS shopping_carts (
    shopping_cart_id CHAR(36) PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS shopping_carts_products (
    shopping_cart_id CHAR(36) NOT NULL,
    product_id CHAR(36) NOT NULL,
    quantity BIGINT NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (shopping_cart_id, product_id),
    FOREIGN KEY (shopping_cart_id) REFERENCES shopping_carts(shopping_cart_id) ON DELETE CASCADE
);