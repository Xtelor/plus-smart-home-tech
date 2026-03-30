package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.store.PageProductDto;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.enums.QuantityState;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "shopping-store")
public interface ShoppingStoreClient {

    @GetMapping("/api/v1/shopping-store/{productId}")
    ProductDto getProduct(@PathVariable("productId") UUID productId);

    @GetMapping("/api/v1/shopping-store")
    PageProductDto getProducts(@RequestParam("category") ProductCategory category,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "20") int size,
                               @RequestParam(value = "sort", required = false) List<String> sort);

    @PutMapping("/api/v1/shopping-store")
    ProductDto createNewProduct(@RequestBody ProductDto productDto);

    @PostMapping("/api/v1/shopping-store")
    ProductDto updateProduct(@RequestBody ProductDto productDto);

    @PostMapping("/api/v1/shopping-store/quantityState")
    boolean setProductQuantityState(@RequestParam("productId") UUID productId,
                                    @RequestParam("quantityState") QuantityState quantityState);

    @PostMapping("/api/v1/shopping-store/removeProductFromStore")
    boolean removeProductFromStore(@RequestBody UUID productId);
}