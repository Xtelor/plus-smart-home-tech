-- таблица оплат
CREATE TABLE IF NOT EXISTS payments (
    payment_id CHAR(36) NOT NULL PRIMARY KEY,
    payment_state VARCHAR(30),
    total_payment DECIMAL(12,2) DEFAULT 0.0 CHECK (total_payment >= 0.0),
    delivery_total DECIMAL(12,2) DEFAULT 0.0 CHECK (delivery_total >= 0.0),
    fee_total DECIMAL(12,2) DEFAULT 0.0 CHECK (fee_total >= 0.0)
);