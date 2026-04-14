package ru.yandex.practicum.feign;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.*;

import java.util.Map;
import java.util.UUID;

@Component
public class WarehouseClientFallback implements WarehouseClient {

    @Override
    public void addNewProduct(NewProductInWarehouseRequest request) {
        throw new RuntimeException("Сервис склада недоступен.");
    }

    @Override
    public void shippedToDelivery(ShippedToDeliveryRequest request) {
        throw new RuntimeException("Сервис склада недоступен.");
    }

    @Override
    public void acceptReturn(Map<UUID, Long> products) {
        throw new RuntimeException("Сервис склада недоступен. Попробуйте позже.");
    }

    @Override
    public BookedProductsDto checkProducts(ShoppingCartDto dto) {
        throw new RuntimeException("Сервис склада недоступен. Попробуйте позже.");
    }

    @Override
    public BookedProductsDto assemblyProductsForOrder(AssemblyProductsForOrderRequest request) {
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
