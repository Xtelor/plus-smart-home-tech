package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssemblyProductsForOrderRequest {

    // Товары для заказа
    @NotNull
    private Map<UUID, Long> products;

    // Идентификатор заказа в БД
    @NotNull
    private UUID orderId;
}
