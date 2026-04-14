package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "delivery")
public interface DeliveryClient {

    // Создание новой доставки в БД
    @PutMapping("/api/v1/delivery")
    DeliveryDto planDelivery(@RequestBody DeliveryDto dto);

    // Эмуляция успешной доставки товара
    @PostMapping("/api/v1/delivery/successful")
    void deliverySuccessful(@RequestBody UUID deliveryId);

    // Эмуляция получения товара в доставку
    @PostMapping("/api/v1/delivery/picked")
    void deliveryPicked(@RequestBody UUID deliveryId);

    // Эмуляция неудачного вручения товара
    @PostMapping("/api/v1/delivery/failed")
    void deliveryFailed(@RequestBody UUID deliveryId);

    // Расчёт полной стоимости доставки заказа
    @PostMapping("/api/v1/delivery/cost")
    BigDecimal deliveryCost(@RequestBody OrderDto dto);
}
