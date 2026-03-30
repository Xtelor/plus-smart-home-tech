package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;

@FeignClient(name = "warehouse", fallback = WarehouseClientFallback.class)
public interface WarehouseClient {

    @PutMapping("/api/v1/warehouse")
    void addNewProduct(@RequestBody NewProductInWarehouseRequest request);

    @PostMapping("/api/v1/warehouse/check")
    BookedProductsDto checkProducts(@RequestBody ShoppingCartDto dto);

    @PostMapping("/api/v1/warehouse/add")
    void addToWarehouse(@RequestBody AddProductToWarehouseRequest request);

    @GetMapping("/api/v1/warehouse/address")
    AddressDto getWarehouseAddress();
}
