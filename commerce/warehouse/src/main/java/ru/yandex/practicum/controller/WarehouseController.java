package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.*;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController implements WarehouseClient {

    private final WarehouseService warehouseService;

    // Добавление нового товара на склад
    @Override
    @PutMapping
    public void addNewProduct(@Valid @RequestBody NewProductInWarehouseRequest request) {

        warehouseService.addNewProduct(request);
    }

    // Передача товаров в доставку
    @Override
    @PostMapping("/shipped")
    public void shippedToDelivery(@RequestBody ShippedToDeliveryRequest request) {

        warehouseService.shippedToDelivery(request);
    }

    // Принятие возврата товара на склад
    @Override
    @PostMapping("/return")
    public void acceptReturn(@RequestBody Map<UUID, Long> products) {

        warehouseService.acceptReturn(products);
    }

    // Принятие товара на склад
    @Override
    @PostMapping("/check")
    public BookedProductsDto checkProducts(@Valid @RequestBody ShoppingCartDto dto) {

        return warehouseService.checkProducts(dto);
    }

    // Сборка товаров к заказу для подготовки к отправке
    @Override
    @PostMapping("/assembly")
    public BookedProductsDto assemblyProductsForOrder(@RequestBody AssemblyProductsForOrderRequest request) {

        return warehouseService.assemblyProductsForOrder(request);
    }

    // Предварительная проверка достаточности количества товаров на складе для данной корзины
    @Override
    @PostMapping("/add")
    public void addToWarehouse(@Valid @RequestBody AddProductToWarehouseRequest request) {

        warehouseService.addToWarehouse(request);
    }

    // Получение адреса склада для расчёта доставки
    @Override
    @GetMapping("/address")
    public AddressDto getWarehouseAddress() {


        return warehouseService.getWarehouseAddress();
    }
}
