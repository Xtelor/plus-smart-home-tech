package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DimensionDto {

    // Ширина
    @NotNull
    @DecimalMin("1.0")
    private Double width;

    // Высота
    @NotNull
    @DecimalMin("1.0")
    private Double height;

    // Глубина
    @NotNull
    @DecimalMin("1.0")
    private Double depth;
}
