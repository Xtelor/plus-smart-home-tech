package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.enums.ProductState;
import ru.yandex.practicum.enums.QuantityState;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @Column(name = "product_id", columnDefinition = "CHAR(36)")
    private UUID productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @Column(name = "image_src", length = 500)
    private String imageSrc;

    @Enumerated(EnumType.STRING)
    @Column(name = "quantity_state", nullable = false, length = 20)
    private QuantityState quantityState;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_state", nullable = false, length = 20)
    private ProductState productState;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_category", nullable = false, length = 20)
    private ProductCategory productCategory;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
}