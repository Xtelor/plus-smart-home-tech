package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.enums.DeliveryState;
import ru.yandex.practicum.exceptions.NoDeliveryFoundException;
import ru.yandex.practicum.feign.OrderClient;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.repository.DeliveryRepository;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    // Создать новую доставку в БД
    @Override
    @Transactional
    public DeliveryDto planDelivery(DeliveryDto dto) {

        Delivery delivery = deliveryMapper.toEntity(dto);

        delivery.setDeliveryId(UUID.randomUUID());
        delivery.setDeliveryState(DeliveryState.CREATED);

        deliveryRepository.save(delivery);

        return deliveryMapper.toDto(delivery);
    }

    // Эмуляция успешной доставки товара
    @Override
    @Transactional
    public void deliverySuccessful(UUID orderId) {

        Delivery delivery = findByOrderId(orderId);

        delivery.setDeliveryState(DeliveryState.DELIVERED);
        deliveryRepository.save(delivery);

        orderClient.delivery(orderId);
    }

    // Эмуляция получения товара в доставку
    @Override
    @Transactional
    public void deliveryPicked(UUID orderId) {

        Delivery delivery = findByOrderId(orderId);

        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        deliveryRepository.save(delivery);

        orderClient.assembly(orderId);

        warehouseClient.shippedToDelivery(ShippedToDeliveryRequest.builder()
                .orderId(orderId)
                .deliveryId(delivery.getDeliveryId())
                .build());
    }

    // Эмуляция неудачного вручения товара
    @Override
    @Transactional
    public void deliveryFailed(UUID orderId) {

        Delivery delivery = findByOrderId(orderId);

        delivery.setDeliveryState(DeliveryState.FAILED);
        deliveryRepository.save(delivery);

        orderClient.deliveryFailed(orderId);
    }

    // Расчёт полной стоимости доставки заказа
    @Override
    public BigDecimal deliveryCost(OrderDto orderDto) {

        AddressDto warehouseAddress = warehouseClient.getWarehouseAddress();

        double baseCost = 5.0;

        if (warehouseAddress.getCountry().contains("ADDRESS_2")) {
            baseCost *= 2;
        }

        baseCost += 5.0;

        if (Objects.equals(Boolean.TRUE, orderDto.getFragile())) {
            baseCost += baseCost * 0.2;
        }

        baseCost += orderDto.getDeliveryWeight() * 0.3;

        baseCost += orderDto.getDeliveryVolume() * 0.2;

        AddressDto toAddress = findByOrderId(orderDto.getOrderId()).getToAddress();

        if (!Objects.equals(toAddress.getStreet(), warehouseAddress.getStreet())) {
            baseCost += baseCost * 0.2;
        }

        return BigDecimal.valueOf(baseCost);
    }

    // Поиск и проверка доставки по номеру заказа
    private Delivery findByOrderId(UUID orderId) {
        return deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException(
                        "Доставка не найдена для заказа: " + orderId));
    }
}
