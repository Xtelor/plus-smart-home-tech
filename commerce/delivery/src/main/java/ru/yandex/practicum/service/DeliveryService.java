package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {

    // Создание новой доставки в БД
    DeliveryDto planDelivery(DeliveryDto dto);

    // Эмуляция успешной доставки товара
    void deliverySuccessful(UUID deliveryId);

    // Эмуляция получения товара в доставку
    void deliveryPicked(UUID deliveryId);

    // Эмуляция неудачного вручения товара
    void deliveryFailed(UUID deliveryId);

    // Расчёт полной стоимости доставки заказа
    BigDecimal deliveryCost(OrderDto dto);
}
