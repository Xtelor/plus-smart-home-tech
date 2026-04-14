package ru.yandex.practicum.dto.delivery;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.enums.DeliveryState;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryDto {

    // Идентификатор доставки
    @NotNull
    private UUID deliveryId;

    // Адрес отправителя
    @NotNull
    private AddressDto fromAddress;

    // Адрес получателя
    @NotNull
    private AddressDto toAddress;

    // Идентификатор заказа
    @NotNull
    private UUID orderId;

    // Статус доставки
    @NotNull
    private DeliveryState deliveryState;
}
