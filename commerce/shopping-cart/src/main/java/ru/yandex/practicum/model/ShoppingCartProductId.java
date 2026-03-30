package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartProductId implements Serializable {

    private UUID shoppingCartId;
    private UUID productId;
}
