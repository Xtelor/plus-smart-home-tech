package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.ShoppingCartProduct;
import ru.yandex.practicum.model.ShoppingCartProductId;

import java.util.List;
import java.util.UUID;

public interface ShoppingCartProductRepository extends JpaRepository<ShoppingCartProduct, ShoppingCartProductId> {
    List<ShoppingCartProduct> findByShoppingCartId(UUID shoppingCartId);
    void deleteByShoppingCartIdAndProductIdIn(UUID shoppingCartId, List<UUID> productIds);
}