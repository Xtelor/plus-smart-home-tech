package ru.yandex.practicum.dto.warehouse;

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
public class ShippedToDeliveryRequest {

    // Идентификатор заказа в БД
    @NotNull
    private UUID orderId;

    // Идентификатор доставки в БД
    @NotNull
    private UUID deliveryId;
}
