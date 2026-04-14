package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.order.ProductReturnRequest;
import ru.yandex.practicum.feign.OrderClient;
import ru.yandex.practicum.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController implements OrderClient {

    private final OrderService orderService;

    @Override
    @GetMapping
    public List<OrderDto> getClientOrders(@RequestParam("username") String username) {

        return orderService.getClientOrders(username);
    }

    @Override
    @PutMapping
    public OrderDto createNewOrder(@RequestBody CreateNewOrderRequest request,
                                   @RequestParam("username") String username) {

        return orderService.createNewOrder(request, username);
    }

    @Override
    @PostMapping("/return")
    public OrderDto productReturn(@RequestBody ProductReturnRequest request) {

        return orderService.productReturn(request);
    }

    @Override
    @PostMapping("/payment")
    public OrderDto payment(@RequestBody UUID orderId) {

        return orderService.payment(orderId);
    }

    @Override
    @PostMapping("/payment/failed")
    public OrderDto paymentFailed(@RequestBody UUID orderId) {

        return orderService.paymentFailed(orderId);
    }

    @Override
    @PostMapping("/delivery")
    public OrderDto delivery(@RequestBody UUID orderId) {

        return orderService.delivery(orderId);
    }

    @Override
    @PostMapping("/delivery/failed")
    public OrderDto deliveryFailed(@RequestBody UUID orderId) {

        return orderService.deliveryFailed(orderId);
    }

    @Override
    @PostMapping("/completed")
    public OrderDto complete(@RequestBody UUID orderId) {

        return orderService.complete(orderId);
    }

    @Override
    @PostMapping("/calculate/total")
    public OrderDto calculateTotalCost(@RequestBody UUID orderId) {

        return orderService.calculateTotalCost(orderId);
    }

    @Override
    @PostMapping("/calculate/delivery")
    public OrderDto calculateDeliveryCost(@RequestBody UUID orderId) {

        return orderService.calculateDeliveryCost(orderId);
    }

    @Override
    @PostMapping("/assembly")
    public OrderDto assembly(@RequestBody UUID orderId) {

        return orderService.assembly(orderId);
    }

    @Override
    @PostMapping("/assembly/failed")
    public OrderDto assemblyFailed(@RequestBody UUID orderId) {

        return orderService.assemblyFailed(orderId);
    }
}
