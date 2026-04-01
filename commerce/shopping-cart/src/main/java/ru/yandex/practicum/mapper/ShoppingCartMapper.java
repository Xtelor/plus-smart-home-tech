package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.model.ShoppingCartProduct;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ShoppingCartMapper {

    public ShoppingCartDto toDto(ShoppingCart cart, List<ShoppingCartProduct> products) {
        Map<UUID, Long> productsMap = products.stream()
                .collect(Collectors.toMap(
                        ShoppingCartProduct::getProductId,
                        ShoppingCartProduct::getQuantity
                ));

        return ShoppingCartDto.builder()
                .shoppingCartId(cart.getShoppingCartId())
                .products(productsMap)
                .build();
    }
}
