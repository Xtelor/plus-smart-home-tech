package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.feign.ShoppingStoreClient;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.service.ProductService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ProductController implements ShoppingStoreClient {

    private final ProductService productService;

    // Получение товара по ID
    @Override
    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable UUID productId) {
        return productService.getProduct(productId);
    }

    // Получение списка товаров
    @Override
    @GetMapping
    public Page<ProductDto> getProducts(@RequestParam ProductCategory category,
                                        Pageable pageable) {
        return productService.getProducts(category, pageable);
    }

    // Добавление нового товара
    @Override
    @PutMapping
    public ProductDto createNewProduct(@RequestBody ProductDto productDto) {
        return productService.createNewProduct(productDto);
    }

    // Обновление товара
    @Override
    @PostMapping
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        return productService.updateProduct(productDto);
    }

    // Обновление количества товаров
    @Override
    @PostMapping("/quantityState")
    public boolean setProductQuantityState(@RequestBody SetProductQuantityStateRequest request) {
        return productService.setProductQuantityState(request);
    }

    // Удаление товара из магазина
    @Override
    @PostMapping("/removeProductFromStore")
    public boolean removeProductFromStore(@RequestBody UUID productId) {
        return productService.removeProductFromStore(productId);
    }
}
