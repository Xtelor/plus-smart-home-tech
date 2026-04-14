package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "payment")
public interface PaymentClient {

    // Формирование оплаты для заказа (переход в платежный шлюз)
    @PostMapping("/api/v1/payment")
    PaymentDto payment(@RequestBody OrderDto dto);

    // Расчёт полной стоимости заказа
    @PostMapping("/api/v1/payment/totalCost")
    BigDecimal  getTotalCost(@RequestBody OrderDto dto);

    // Метод для эмуляции успешной оплаты в платежном шлюзе
    @PostMapping("/api/v1/payment/refund")
    void paymentSuccess(@RequestBody UUID paymentId);

    // Расчёт стоимости товаров в заказе
    @PostMapping("/api/v1/payment/productCost")
    BigDecimal productCost(@RequestBody OrderDto dto);

    // Метод для эмуляции отказа в оплате платежного шлюза
    @PostMapping("/api/v1/payment/failed")
    void paymentFailed(@RequestBody UUID paymentId);
}
