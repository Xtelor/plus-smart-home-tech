package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.model.Product;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        return ProductDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .imageSrc(product.getImageSrc())
                .quantityState(product.getQuantityState())
                .productState(product.getProductState())
                .productCategory(product.getProductCategory())
                .price(product.getPrice())
                .build();
    }

    public Product toEntity(ProductDto dto) {
        return Product.builder()
                .productId(dto.getProductId())
                .productName(dto.getProductName())
                .description(dto.getDescription())
                .imageSrc(dto.getImageSrc())
                .quantityState(dto.getQuantityState())
                .productState(dto.getProductState())
                .productCategory(dto.getProductCategory())
                .price(dto.getPrice())
                .build();
    }

    public void updateEntity(Product product, ProductDto dto) {
        product.setProductName(dto.getProductName());
        product.setDescription(dto.getDescription());
        product.setImageSrc(dto.getImageSrc());
        product.setQuantityState(dto.getQuantityState());
        product.setProductState(dto.getProductState());
        product.setProductCategory(dto.getProductCategory());
        product.setPrice(dto.getPrice());
    }
}
