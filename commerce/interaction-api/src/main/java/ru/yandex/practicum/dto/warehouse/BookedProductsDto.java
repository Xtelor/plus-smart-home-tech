package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookedProductsDto {

    // Общий вес доставки
    @NotNull
    private Double deliveryWeight;

    // Общий объем доставки
    @NotNull
    private Double deliveryVolume;

    // Наличие хрупких вещей в доставке
    @NotNull
    private Boolean fragile;
}
