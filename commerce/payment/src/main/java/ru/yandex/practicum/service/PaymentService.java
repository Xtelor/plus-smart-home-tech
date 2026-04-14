package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {

    // Формирование оплаты для заказа (переход в платежный шлюз)
    PaymentDto payment(OrderDto dto);

    // Расчёт полной стоимости заказа
    BigDecimal getTotalCost(OrderDto dto);

    // Метод для эмуляции успешной оплаты в платежном шлюзе
    void paymentSuccess(UUID paymentId);

    // Расчёт стоимости товаров в заказе
    BigDecimal productCost(OrderDto dto);

    // Метод для эмуляции отказа в оплате платежного шлюза
    void paymentFailed(UUID paymentId);
}
