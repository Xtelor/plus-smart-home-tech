package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.exceptions.NoAuthorizedUserException;
import ru.yandex.practicum.exceptions.NoProductsInShoppingCartException;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.mapper.ShoppingCartMapper;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.model.ShoppingCartProduct;
import ru.yandex.practicum.model.ShoppingCartProductId;
import ru.yandex.practicum.repository.ShoppingCartProductRepository;
import ru.yandex.practicum.repository.ShoppingCartRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartProductRepository shoppingCartProductRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final WarehouseClient warehouseClient;

    // Получение актуальной корзины для авторизованного пользователя
    public ShoppingCartDto getShoppingCart(String username) {
        validateUsername(username);
        ShoppingCart cart = getOrCreateCart(username);
        return buildCartDto(cart);
    }

    // Добавление товара в корзину
    @Transactional
    public ShoppingCartDto addProductToShoppingCart(String username, Map<UUID, Long> products) {
        validateUsername(username);
        ShoppingCart cart = getOrCreateCart(username);

        ShoppingCartDto tempDto = ShoppingCartDto.builder()
                .shoppingCartId(cart.getShoppingCartId())
                .products(products)
                .build();
        warehouseClient.checkProducts(tempDto);

        products.forEach((productId, quantity) -> {
            ShoppingCartProduct product = ShoppingCartProduct.builder()
                    .shoppingCartId(cart.getShoppingCartId())
                    .productId(productId)
                    .quantity(quantity)
                    .build();
            shoppingCartProductRepository.save(product);
        });

        return buildCartDto(cart);
    }

    // Деактивация корзины товаров для пользователя
    @Transactional
    public void deactivateCurrentShoppingCart(String username) {
        validateUsername(username);
        ShoppingCart cart = findActiveCart(username);
        cart.setActive(false);
        shoppingCartRepository.save(cart);
    }

    // Удаление указанных товаров из корзины пользователя
    @Transactional
    public ShoppingCartDto removeFromShoppingCart(String username, List<UUID> productIds) {
        validateUsername(username);
        ShoppingCart cart = findActiveCart(username);

        List<UUID> existingProductIds = getProductIds(cart.getShoppingCartId());
        boolean anyExists = productIds.stream().anyMatch(existingProductIds::contains);

        if (!anyExists) {
            throw new NoProductsInShoppingCartException("Данные товары не найдены в корзине.");
        }

        shoppingCartProductRepository.deleteByShoppingCartIdAndProductIdIn(cart.getShoppingCartId(), productIds);
        return buildCartDto(cart);
    }

    // Изменение количества товаров в корзине
    @Transactional
    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        validateUsername(username);
        ShoppingCart cart = findActiveCart(username);

        ShoppingCartProduct product = findCartProduct(cart.getShoppingCartId(), request.getProductId());
        product.setQuantity(request.getNewQuantity());
        shoppingCartProductRepository.save(product);

        return buildCartDto(cart);
    }

    // Валидация имени пользователя
    private void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new NoAuthorizedUserException("Имя пользователя не может быть пустым.");
        }
    }

    // Метод поиска активной корзины
    private ShoppingCart findActiveCart(String username) {
        return shoppingCartRepository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new NoProductsInShoppingCartException("Не найдено активной корзины покупателя."));
    }

    // Получение или создание корзины
    @Transactional
    private ShoppingCart getOrCreateCart(String username) {
        return shoppingCartRepository.findByUsernameAndActiveTrue(username)
                .orElseGet(() -> {
                    ShoppingCart newCart = ShoppingCart.builder()
                            .shoppingCartId(UUID.randomUUID())
                            .username(username)
                            .active(true)
                            .build();
                    return shoppingCartRepository.save(newCart);
                });
    }

    private ShoppingCartDto buildCartDto(ShoppingCart cart) {
        List<ShoppingCartProduct> products = shoppingCartProductRepository
                .findByShoppingCartId(cart.getShoppingCartId());
        return shoppingCartMapper.toDto(cart, products);
    }

    // Получение списка ID товаров
    private List<UUID> getProductIds(UUID shoppingCartId) {
        return shoppingCartProductRepository.findByShoppingCartId(shoppingCartId).stream()
                .map(ShoppingCartProduct::getProductId)
                .toList();
    }

    // Поиск товара в корзине
    private ShoppingCartProduct findCartProduct(UUID shoppingCartId, UUID productId) {
        return shoppingCartProductRepository.findById(new ShoppingCartProductId(shoppingCartId, productId))
                .orElseThrow(() -> new NoProductsInShoppingCartException("Товар не найден в корзине покупателя."));
    }
}
