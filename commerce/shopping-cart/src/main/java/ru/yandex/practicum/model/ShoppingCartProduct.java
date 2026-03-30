package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "shopping_cart_products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(ShoppingCartProductId.class)
public class ShoppingCartProduct {

    // ID корзины
    @Id
    @Column(name = "shopping_cart_id", columnDefinition = "CHAR(36)")
    private UUID shoppingCartId;

    // ID товара
    @Id
    @Column(name = "product_id", columnDefinition = "CHAR(36)")
    private UUID productId;

    // Количество товара
    @Column(name = "quantity", nullable = false)
    private Long quantity;
}
