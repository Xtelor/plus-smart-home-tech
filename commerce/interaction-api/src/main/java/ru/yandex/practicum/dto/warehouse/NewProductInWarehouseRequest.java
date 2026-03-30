package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewProductInWarehouseRequest {

    // Идентификатор товара в БД
    @NotNull
    private UUID productId;

    // Признак хрупкости
    private Boolean fragile;

    // Размеры товара
    @NotNull
    private DimensionDto dimension;

    // Вес товара
    @NotNull
    @Min(1)
    private Double weight;
}
