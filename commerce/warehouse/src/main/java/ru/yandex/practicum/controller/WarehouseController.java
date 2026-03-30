package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.service.WarehouseService;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController implements WarehouseClient {

    private final WarehouseService warehouseService;

    // Добавление нового товара на склад
    @Override
    @PutMapping
    public void addNewProduct(@RequestBody NewProductInWarehouseRequest request) {
        warehouseService.addNewProduct(request);
    }

    // Принятие товара на склад
    @Override
    @PostMapping("/check")
    public BookedProductsDto checkProducts(@RequestBody ShoppingCartDto dto) {
        return warehouseService.checkProducts(dto);
    }

    // Предварительная проверка достаточности количества товаров на складе для данной корзины
    @Override
    @PostMapping("/add")
    public void addToWarehouse(@RequestBody AddProductToWarehouseRequest request) {
        warehouseService.addToWarehouse(request);
    }

    // Получение адреса склада для расчёта доставки
    @Override
    @GetMapping("/address")
    public AddressDto getWarehouseAddress() {
        return warehouseService.getWarehouseAddress();
    }
}
