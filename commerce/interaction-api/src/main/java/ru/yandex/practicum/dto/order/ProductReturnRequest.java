package ru.yandex.practicum.dto.order;

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
public class ProductReturnRequest {

    // ID заказа
    private UUID orderId;

    // Отображение идентификатора товара на отобранное количество
    @NotNull
    private Map<UUID, Long> products;
}
