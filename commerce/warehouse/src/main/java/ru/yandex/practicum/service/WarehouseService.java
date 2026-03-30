package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;

public interface WarehouseService {

    // Добавление нового товара на склад
    void addNewProduct(NewProductInWarehouseRequest request);

    // Принятие товара на склад
    void addToWarehouse(AddProductToWarehouseRequest request);

    // Предварительная проверка достаточности количества товаров на складе для данной корзины
    BookedProductsDto checkProducts(ShoppingCartDto dto);

    // Получение адреса склада для расчёта доставки
    AddressDto getWarehouseAddress();
}
