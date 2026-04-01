package ru.yandex.practicum.feign;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;

@Component
public class WarehouseClientFallback implements WarehouseClient {

    @Override
    public void addNewProduct(NewProductInWarehouseRequest request) {
        throw new RuntimeException("Сервис склада недоступен.");
    }

    @Override
    public BookedProductsDto checkProducts(ShoppingCartDto dto) {
        throw new RuntimeException("Сервис склада недоступен. Попробуйте позже.");
    }

    @Override
    public void addToWarehouse(AddProductToWarehouseRequest request) {
        throw new RuntimeException("Сервис склада недоступен.");
    }

    @Override
    public AddressDto getWarehouseAddress() {
        throw new RuntimeException("Сервис склада недоступен.");
    }
}
