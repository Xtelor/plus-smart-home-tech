package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "orders_products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(OrderProductId.class)
public class OrderProduct {

    // ID заказа
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "order_id", nullable = false, length = 36)
    private UUID orderId;

    // ID товара
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "product_id", nullable = false, length = 36)
    private UUID productId;

    // Количество товара
    @Column(name = "quantity", nullable = false)
    private Long quantity;
}
