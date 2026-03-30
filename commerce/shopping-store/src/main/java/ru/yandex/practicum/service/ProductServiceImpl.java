package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.exceptions.ProductNotFoundException;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.enums.ProductState;
import ru.yandex.practicum.repository.ProductRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    // Получение товара по ID
    public ProductDto getProduct(UUID productId) {
        return productMapper.toDto(findProductById(productId));
    }

    // Получение списка товаров
    public Page<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        return productRepository.findByProductCategory(category, pageable)
                .map(productMapper::toDto);
    }

    // Добавление нового товара
    @Transactional
    public ProductDto createNewProduct(ProductDto dto) {
        Product product = productMapper.toEntity(dto);

        product.setProductId(UUID.randomUUID());
        product.setProductState(ProductState.ACTIVE);

        return productMapper.toDto(productRepository.save(product));
    }

    // Обновление товара
    @Transactional
    public ProductDto updateProduct(ProductDto productDto) {
        if (productDto.getProductId() == null) {
            throw new ProductNotFoundException("Для обновления товара требуется его ID.");
        }

        Product product = findProductById(productDto.getProductId());
        productMapper.updateEntity(product, productDto);

        return productMapper.toDto(productRepository.save(product));
    }

    // Обновление количества товаров
    @Transactional
    public boolean setProductQuantityState(SetProductQuantityStateRequest request) {
        Product product = findProductById(request.getProductId());

        product.setQuantityState(request.getQuantityState());
        productRepository.save(product);

        return true;
    }

    // Удаление товара из магазина
    @Transactional
    public boolean removeProductFromStore(UUID productId) {
        Product product = findProductById(productId);

        product.setProductState(ProductState.DEACTIVATE);
        productRepository.save(product);

        return true;
    }

    // Поиск товара по ID
    private Product findProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар не найден: " + productId));
    }
}
