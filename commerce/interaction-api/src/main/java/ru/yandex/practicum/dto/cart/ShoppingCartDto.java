package ru.yandex.practicum.dto.cart;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartDto {

    // Идентификатор корзины в БД
    @NotNull
    private UUID shoppingCartId;

    // Отображение идентификатора товара на отобранное количество
    @NotNull
    private Map<UUID, Long> products;
}
