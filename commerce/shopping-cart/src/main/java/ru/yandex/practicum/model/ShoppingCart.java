package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "shopping_cart_id", nullable = false, length = 36)
    private UUID shoppingCartId;

    // Имя пользователя
    @Column(name = "username", nullable = false)
    private String username;

    // Состояние корзины
    @Column(name = "active", nullable = false)
    private Boolean active;
}
