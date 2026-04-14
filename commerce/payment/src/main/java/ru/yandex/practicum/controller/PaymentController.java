package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;
import ru.yandex.practicum.feign.PaymentClient;
import ru.yandex.practicum.service.PaymentService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController implements PaymentClient {

    private final PaymentService paymentService;

    @Override
    @PostMapping
    public PaymentDto payment(@RequestBody OrderDto dto) {

        return paymentService.payment(dto);
    }

    @Override
    @PostMapping("/totalCost")
    public BigDecimal getTotalCost(@RequestBody OrderDto dto) {

        return paymentService.getTotalCost(dto);
    }

    @Override
    @PostMapping("/refund")
    public void paymentSuccess(@RequestBody UUID paymentId) {

        paymentService.paymentSuccess(paymentId);
    }

    @Override
    @PostMapping("/productCost")
    public BigDecimal productCost(@RequestBody OrderDto dto) {

        return paymentService.productCost(dto);
    }

    @Override
    @PostMapping("/failed")
    public void paymentFailed(@RequestBody UUID paymentId) {

        paymentService.paymentFailed(paymentId);
    }
}
