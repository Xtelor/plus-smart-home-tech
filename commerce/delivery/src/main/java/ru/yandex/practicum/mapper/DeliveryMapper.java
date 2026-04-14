package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.model.Delivery;

@Component
public class DeliveryMapper {

    public DeliveryDto toDto(Delivery delivery) {
        return DeliveryDto.builder()
                .deliveryId(delivery.getDeliveryId())
                .fromAddress(delivery.getFromAddress())
                .toAddress(delivery.getToAddress())
                .orderId(delivery.getOrderId())
                .deliveryState(delivery.getDeliveryState())
                .build();
    }

    public Delivery toEntity(DeliveryDto dto) {
        return Delivery.builder()
                .deliveryId(dto.getDeliveryId())
                .fromAddress(dto.getFromAddress())
                .toAddress(dto.getToAddress())
                .orderId(dto.getOrderId())
                .deliveryState(dto.getDeliveryState())
                .build();
    }
}
