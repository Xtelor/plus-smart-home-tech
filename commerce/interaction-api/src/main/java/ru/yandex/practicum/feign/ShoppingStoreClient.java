package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.enums.ProductCategory;

import java.util.UUID;

@FeignClient(name = "shopping-store")
public interface ShoppingStoreClient {

    @GetMapping("/api/v1/shopping-store/{productId}")
    ProductDto getProduct(@PathVariable UUID productId);

    @GetMapping("/api/v1/shopping-store")
    Page<ProductDto> getProducts(@RequestParam ProductCategory category, Pageable pageable);

    @PutMapping("/api/v1/shopping-store")
    ProductDto createNewProduct(@RequestBody ProductDto productDto);

    @PostMapping("/api/v1/shopping-store")
    ProductDto updateProduct(@RequestBody ProductDto productDto);

    @PostMapping("/api/v1/shopping-store/quantityState")
    boolean setProductQuantityState(@RequestBody SetProductQuantityStateRequest request);

    @PostMapping("/api/v1/shopping-store/removeProductFromStore")
    boolean removeProductFromStore(@RequestBody UUID productId);
}