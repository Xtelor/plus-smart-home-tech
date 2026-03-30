package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {

    // Получение актуальной корзины для авторизованного пользователя
    ShoppingCartDto getShoppingCart(String username);

    // Добавление товара в корзину
    ShoppingCartDto addProductToShoppingCart(String username, Map<UUID, Long> products);

    // Деактивация корзины товаров для пользователя
    void deactivateCurrentShoppingCart(String username);

    // Удаление указанных товаров из корзины пользователя
    ShoppingCartDto removeFromShoppingCart(String username, List<UUID> productIds);

    // Изменение количества товаров в корзине
    ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request);
}
