package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "warehouse_products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseProduct {

    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "product_id", nullable = false, length = 36)
    private UUID productId;

    @Column(name = "fragile")
    private Boolean fragile;

    @Column(name = "product_width", nullable = false)
    private Double width;

    @Column(name = "product_height", nullable = false)
    private Double height;

    @Column(name = "product_depth", nullable = false)
    private Double depth;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "quantity", nullable = false)
    private Long quantity;
}
