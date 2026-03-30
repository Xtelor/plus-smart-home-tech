package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "shopping_carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCart {

    // ID корзины
    @Id
    @Column(name = "shopping_cart_id", columnDefinition = "CHAR(36)")
    private UUID shoppingCartId;

    // Имя пользователя
    @Column(name = "username", nullable = false)
    private String username;

    // Состояние корзины
    @Column(name = "active", nullable = false)
    private Boolean active;
}
