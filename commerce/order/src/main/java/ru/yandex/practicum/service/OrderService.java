package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.order.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    // Получить заказы пользователя
    List<OrderDto> getClientOrders(String username);

    // Создать новый заказ в системе
    OrderDto createNewOrder(CreateNewOrderRequest request, String username);

    // Возврат заказа
    OrderDto productReturn(ProductReturnRequest request);

    // Оплата заказа
    OrderDto payment(UUID orderId);

    // Оплата заказа прошла с ошибкой
    OrderDto paymentFailed(UUID orderId);

    // Доставка заказа
    OrderDto delivery(UUID orderId);

    // Доставка заказа произошла с ошибкой
    OrderDto deliveryFailed(UUID orderId);

    // Завершение заказа
    OrderDto complete(UUID orderId);

    // Расчёт стоимости заказа
    OrderDto calculateTotalCost(UUID orderId);

    // Расчёт стоимости заказа
    OrderDto calculateDeliveryCost(UUID orderId);

    // Сборка заказа
    OrderDto assembly(UUID orderId);

    // Сборка заказа произошла с ошибкой
    OrderDto assemblyFailed(UUID orderId);
}
