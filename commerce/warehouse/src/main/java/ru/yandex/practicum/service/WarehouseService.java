package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {

    // Добавление нового товара на склад
    void addNewProduct(NewProductInWarehouseRequest request);

    // Принятие товара на склад
    void addToWarehouse(AddProductToWarehouseRequest request);

    // Предварительная проверка достаточности количества товаров на складе для данной корзины
    BookedProductsDto checkProducts(ShoppingCartDto dto);

    // Получение адреса склада для расчёта доставки
    AddressDto getWarehouseAddress();

    // Передача товаров в доставку
    void shippedToDelivery(ShippedToDeliveryRequest request);

    // Принятие возврата товаров на склад
    void acceptReturn(Map<UUID, Long> products);

    // Сборка товаров к заказу для подготовки к отправке
    BookedProductsDto assemblyProductsForOrder(AssemblyProductsForOrderRequest request);
}
