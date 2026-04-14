-- Таблица доставки
CREATE TABLE IF NOT EXISTS deliveries (
    delivery_id CHAR(36) NOT NULL PRIMARY KEY,
    from_address JSONB NOT NULL,
    to_address JSONB NOT NULL,
    order_id CHAR(36) NOT NULL,
    delivery_state VARCHAR(30) NOT NULL
);