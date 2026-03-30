package ru.yandex.practicum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.enums.ProductCategory;

import java.util.UUID;

public interface ProductService {

    // Получение товара по ID
    ProductDto getProduct(UUID productId);

    // Получение списка товаров
    Page<ProductDto> getProducts(ProductCategory category, Pageable pageable);

    // Добавление нового товара
    ProductDto createNewProduct(ProductDto dto);

    // Обновление товара
    public ProductDto updateProduct(ProductDto productDto);

    // Обновление количества товаров
    public boolean setProductQuantityState(SetProductQuantityStateRequest request);

    // Удаление товара из магазина
    public boolean removeProductFromStore(UUID productId);


}
