package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.yandex.practicum.enums.OrderState;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    // Идентификатор заказа
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "order_id", nullable = false, length = 36)
    private UUID orderId;

    // Имя пользователя
    @Column(name = "username", nullable = false)
    private String username;

    // Идентификатор корзины
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "shopping_cart_id", length = 36)
    private UUID shoppingCartId;

    // Идентификатор оплаты
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "payment_id", length = 36)
    private UUID paymentId;

    // Идентификатор доставки
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "delivery_id", length = 36)
    private UUID deliveryId;

    // Статус заказа
    @Enumerated(EnumType.STRING)
    @Column(name = "order_state")
    private OrderState orderState;

    // Общий вес доставки
    @Column(name = "delivery_weight")
    private Double deliveryWeight;

    // Общий объём доставки
    @Column(name = "delivery_volume")
    private Double deliveryVolume;

    // Признак хрупкости заказа
    @Column(name = "fragile")
    private Boolean fragile;

    // Общая стоимость
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    // Стоимость доставки
    @Column(name = "delivery_price")
    private BigDecimal deliveryPrice;

    // Стоимость товаров в заказе
    @Column(name = "product_price")
    private BigDecimal productPrice;

}
