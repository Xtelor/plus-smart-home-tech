package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.store.PageProductDto;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.enums.ProductCategory;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    // Получение товара по ID
    ProductDto getProduct(UUID productId);

    // Получение списка товаров
    PageProductDto getProducts(ProductCategory category, int page, int size, List<String> sort);

    // Добавление нового товара
    ProductDto createNewProduct(ProductDto dto);

    // Обновление товара
    ProductDto updateProduct(ProductDto productDto);

    // Обновление количества товаров
    boolean setProductQuantityState(SetProductQuantityStateRequest request);

    // Удаление товара из магазина
    boolean removeProductFromStore(UUID productId);


}
