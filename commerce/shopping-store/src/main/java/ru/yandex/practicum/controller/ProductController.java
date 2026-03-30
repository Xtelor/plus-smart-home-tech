package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.store.PageProductDto;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.enums.QuantityState;
import ru.yandex.practicum.feign.ShoppingStoreClient;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.service.ProductService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ProductController implements ShoppingStoreClient {

    private final ProductService productService;

    // Получение товара по ID
    @Override
    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable("productId") UUID productId) {
        return productService.getProduct(productId);
    }

    // Получение списка товаров
    @Override
    @GetMapping
    public PageProductDto getProducts(@RequestParam("category") ProductCategory category,
                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "size", defaultValue = "20") int size,
                                      @RequestParam(value = "sort", required = false) List<String> sort) {
        return productService.getProducts(category, page, size, sort);
    }

    // Добавление нового товара
    @Override
    @PutMapping
    public ProductDto createNewProduct(@Valid @RequestBody ProductDto productDto) {
        return productService.createNewProduct(productDto);
    }

    // Обновление товара
    @Override
    @PostMapping
    public ProductDto updateProduct(@Valid @RequestBody ProductDto productDto) {
        return productService.updateProduct(productDto);
    }

    // Задание количества товаров
    @Override
    @PostMapping("/quantityState")
    public boolean setProductQuantityState(@RequestParam("productId") UUID productId,
                                           @RequestParam("quantityState") QuantityState quantityState) {
        SetProductQuantityStateRequest request = SetProductQuantityStateRequest.builder()
                .productId(productId)
                .quantityState(quantityState)
                .build();

        return productService.setProductQuantityState(request);
    }

    // Удаление товара из магазина
    @Override
    @PostMapping("/removeProductFromStore")
    public boolean removeProductFromStore(@RequestBody UUID productId) {
        return productService.removeProductFromStore(productId);
    }
}
