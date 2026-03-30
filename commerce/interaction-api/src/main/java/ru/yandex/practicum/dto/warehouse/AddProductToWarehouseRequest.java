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
public class AddProductToWarehouseRequest {

    // Идентификатор товара в БД
    private UUID productId;

    // Количество единиц товара для добавления на склад
    @NotNull
    @Min(1)
    private Long quantity;
}
