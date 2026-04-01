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
    @Override
    public ShoppingCartDto getShoppingCart(String username) {
        validateUsername(username);
        ShoppingCart cart = getOrCreateCart(username);
        return buildCartDto(cart);
    }

    // Добавление товара в корзину
    @Override
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
            ShoppingCartProductId id = new ShoppingCartProductId(cart.getShoppingCartId(), productId);

            ShoppingCartProduct product = shoppingCartProductRepository.findById(id)
                    .orElse(ShoppingCartProduct.builder()
                            .shoppingCartId(cart.getShoppingCartId())
                            .productId(productId)
                            .quantity(0L)
                            .build());

            product.setQuantity(product.getQuantity() + quantity);
            shoppingCartProductRepository.save(product);
        });

        return buildCartDto(cart);
    }

    // Деактивация корзины товаров для пользователя
    @Override
    @Transactional
    public void deactivateCurrentShoppingCart(String username) {
        validateUsername(username);
        shoppingCartRepository.findByUsernameAndActiveTrue(username)
                .ifPresent(cart -> {
                    cart.setActive(false);
                    shoppingCartRepository.save(cart);
                });
    }

    // Удаление указанных товаров из корзины пользователя
    @Override
    @Transactional
    public ShoppingCartDto removeFromShoppingCart(String username, List<UUID> productIds) {
        validateUsername(username);
        ShoppingCart cart = getOrCreateCart(username);

        if (productIds == null || productIds.isEmpty()) {
            throw new NoProductsInShoppingCartException("Список товаров для удаления пуст.");
        }

        List<UUID> existingProductIds = getProductIds(cart.getShoppingCartId());
        boolean anyExists = productIds.stream().anyMatch(existingProductIds::contains);

        if (!anyExists) {
            throw new NoProductsInShoppingCartException("Данные товары не найдены в корзине.");
        }

        shoppingCartProductRepository.deleteByShoppingCartIdAndProductIdIn(cart.getShoppingCartId(), productIds);
        return buildCartDto(cart);
    }

    // Изменение количества товаров в корзине
    @Override
    @Transactional
    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        validateUsername(username);

        if (request == null) {
            throw new NoProductsInShoppingCartException("Запрос на изменение количества пуст.");
        }

        ShoppingCart cart = getOrCreateCart(username);

        ShoppingCartProduct product = findCartProduct(cart.getShoppingCartId(), request.getProductId());

        ShoppingCartDto tempDto = ShoppingCartDto.builder()
                .shoppingCartId(cart.getShoppingCartId())
                .products(Map.of(request.getProductId(), request.getNewQuantity()))
                .build();

        warehouseClient.checkProducts(tempDto);

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
