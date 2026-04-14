package ru.yandex.practicum.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddressDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNewOrderRequest {

    // Корзина товаров в онлайн магазине
    @NotNull
    private ShoppingCartDto shoppingCart;

    // Представление адреса в системе
    @NotNull
    private AddressDto deliveryAddress;
}
