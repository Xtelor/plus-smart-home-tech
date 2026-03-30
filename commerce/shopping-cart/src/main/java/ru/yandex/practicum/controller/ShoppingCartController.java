package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.feign.ShoppingCartClient;
import ru.yandex.practicum.service.ShoppingCartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController implements ShoppingCartClient {

    private final ShoppingCartService shoppingCartService;

    // Получение актуальной корзины для авторизованного пользователя
    @Override
    @GetMapping
    public ShoppingCartDto getShoppingCart(@RequestParam String username) {
        return shoppingCartService.getShoppingCart(username);
    }

    // Добавление товара в корзину
    @Override
    @PutMapping
    public ShoppingCartDto addProductToShoppingCart(@RequestParam String username,
                                                    @RequestBody Map<UUID, Long> products) {
        return shoppingCartService.addProductToShoppingCart(username, products);
    }

    // Деактивация корзины товаров для пользователя
    @Override
    @DeleteMapping
    public void deactivateCurrentShoppingCart(@RequestParam String username) {
        shoppingCartService.deactivateCurrentShoppingCart(username);
    }

    // Удаление указанных товаров из корзины пользователя
    @Override
    @PostMapping("/remove")
    public ShoppingCartDto removeFromShoppingCart(@RequestParam String username,
                                                  @RequestBody List<UUID> productIds) {
        return shoppingCartService.removeFromShoppingCart(username, productIds);
    }

    // Изменение количества товаров в корзине
    @Override
    @PostMapping("/change-quantity")
    public ShoppingCartDto changeProductQuantity(@RequestParam String username,
                                                 @RequestBody ChangeProductQuantityRequest request) {
        return shoppingCartService.changeProductQuantity(username, request);
    }
}