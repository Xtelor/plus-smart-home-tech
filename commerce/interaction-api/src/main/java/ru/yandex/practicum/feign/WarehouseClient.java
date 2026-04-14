package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.*;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "warehouse", fallback = WarehouseClientFallback.class)
public interface WarehouseClient {

    // Добавить новый товар на склад
    @PutMapping("/api/v1/warehouse")
    void addNewProduct(@RequestBody NewProductInWarehouseRequest request);

    // Передать товары в доставку
    @PostMapping("/api/v1/warehouse/shipped")
    void shippedToDelivery(@RequestBody ShippedToDeliveryRequest request);

    // Принять возврат товаров на склад
    @PostMapping("/api/v1/warehouse/return")
    void acceptReturn(@RequestBody Map<UUID, Long> products);

    // Предварительно проверить, что количество товаров на складе достаточно для данной корзины продуктов
    @PostMapping("/api/v1/warehouse/check")
    BookedProductsDto checkProducts(@RequestBody ShoppingCartDto dto);

    // Собрать товары к заказу для подготовки к отправке
    @PostMapping("/api/v1/warehouse/assembly")
    BookedProductsDto assemblyProductsForOrder(@RequestBody AssemblyProductsForOrderRequest request);

    // Принять товар на склад
    @PostMapping("/api/v1/warehouse/add")
    void addToWarehouse(@RequestBody AddProductToWarehouseRequest request);

    // Предоставить адрес склада для расчёта доставки
    @GetMapping("/api/v1/warehouse/address")
    AddressDto getWarehouseAddress();
}
