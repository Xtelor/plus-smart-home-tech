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
@Table(name = "shopping_carts_products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(ShoppingCartProductId.class)
public class ShoppingCartProduct {

    // ID корзины
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "shopping_cart_id", nullable = false, length = 36)
    private UUID shoppingCartId;

    // ID товара
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "product_id", nullable = false, length = 36)
    private UUID productId;

    // Количество товара
    @Column(name = "quantity", nullable = false)
    private Long quantity;
}
