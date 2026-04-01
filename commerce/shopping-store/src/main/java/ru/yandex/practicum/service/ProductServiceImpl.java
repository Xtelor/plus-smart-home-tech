package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.store.PageProductDto;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.dto.store.SortObject;
import ru.yandex.practicum.exceptions.ProductNotFoundException;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.enums.ProductState;
import ru.yandex.practicum.repository.ProductRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    // Получение товара по ID
    @Override
    public ProductDto getProduct(UUID productId) {

        return productMapper.toDto(findProductById(productId));
    }

    // Получение списка товаров
    @Override
    public PageProductDto getProducts(ProductCategory category, int page, int size, List<String> sort) {
        List<ProductDto> products = productRepository.findAllByProductCategory(category).stream()
                .map(productMapper::toDto)
                .toList();

        String direction = "ASC";
        String property = "productName";

        if (sort != null && !sort.isEmpty()) {
            String sortValue = String.join(",", sort).trim();

            if ("productName,DESC".equalsIgnoreCase(sortValue)) {
                products = products.stream()
                        .sorted((a, b) -> b.getProductName()
                                .compareToIgnoreCase(a.getProductName()))
                        .toList();
                direction = "DESC";
            } else if ("productName,ASC".equalsIgnoreCase(sortValue)) {
                products = products.stream()
                        .sorted((a, b) -> a.getProductName()
                                .compareToIgnoreCase(b.getProductName()))
                        .toList();
                direction = "ASC";
            }
        }

        int start = page * size;
        int end = Math.min(start + size, products.size());

        List<ProductDto> pageContent = start >= products.size()
                ? List.of()
                : products.subList(start, end);

        int total = products.size();
        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) total / size);

        return PageProductDto.builder()
                .totalElements(total)
                .totalPages(totalPages)
                .first(page == 0)
                .last(end >= total)
                .size(size)
                .content(pageContent)
                .number(page)
                .sort(List.of(new SortObject(direction, property)))
                .numberOfElements(pageContent.size())
                .empty(pageContent.isEmpty())
                .build();
    }

    // Добавление нового товара
    @Override
    @Transactional
    public ProductDto createNewProduct(ProductDto dto) {

        Product product = productMapper.toEntity(dto);
        product.setProductId(UUID.randomUUID());
        return productMapper.toDto(productRepository.save(product));
    }

    // Обновление товара
    @Override
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
    @Override
    @Transactional
    public boolean setProductQuantityState(SetProductQuantityStateRequest request) {
        Product product = findProductById(request.getProductId());
        product.setQuantityState(request.getQuantityState());
        productRepository.save(product);
        return true;
    }

    // Удаление товара из магазина
    @Override
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
