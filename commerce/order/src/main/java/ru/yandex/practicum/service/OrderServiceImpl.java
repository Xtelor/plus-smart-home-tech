package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.order.ProductReturnRequest;
import ru.yandex.practicum.dto.payment.PaymentDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.enums.DeliveryState;
import ru.yandex.practicum.enums.OrderState;
import ru.yandex.practicum.exceptions.NoOrderFoundException;
import ru.yandex.practicum.exceptions.NotAuthorizedUserException;
import ru.yandex.practicum.feign.DeliveryClient;
import ru.yandex.practicum.feign.PaymentClient;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.model.OrderProduct;
import ru.yandex.practicum.repository.OrderProductRepository;
import ru.yandex.practicum.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderMapper orderMapper;
    private final WarehouseClient warehouseClient;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;


    // Получить заказы пользователя
    @Override
    public List<OrderDto> getClientOrders(String username) {

        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым.");
        }

        return orderRepository.findByUsername(username).stream()
                .map(order -> orderMapper.toDto(order,
                        orderProductRepository.findByOrderId(order.getOrderId())))
                .collect(Collectors.toList());
    }

    // Создать новый заказ
    @Override
    @Transactional
    public OrderDto createNewOrder(CreateNewOrderRequest request, String username) {

        BookedProductsDto bookedProducts = warehouseClient.checkProducts(
                request.getShoppingCart());

        Order order = Order.builder()
                .orderId(UUID.randomUUID())
                .username(username)
                .shoppingCartId(request.getShoppingCart().getShoppingCartId())
                .orderState(OrderState.NEW)
                .deliveryWeight(bookedProducts.getDeliveryWeight())
                .deliveryVolume(bookedProducts.getDeliveryVolume())
                .fragile(bookedProducts.getFragile())
                .build();

        orderRepository.save(order);

        List<OrderProduct> orderProducts = request.getShoppingCart().getProducts()
                .entrySet().stream()
                .map(entry -> OrderProduct.builder()
                        .orderId(order.getOrderId())
                        .productId(entry.getKey())
                        .quantity(entry.getValue())
                        .build())
                .collect(Collectors.toList());

        orderProductRepository.saveAll(orderProducts);

        DeliveryDto deliveryDto = deliveryClient.planDelivery(DeliveryDto.builder()
                .deliveryId(UUID.randomUUID())
                .orderId(order.getOrderId())
                .fromAddress(warehouseClient.getWarehouseAddress())
                .toAddress(request.getDeliveryAddress())
                .deliveryState(DeliveryState.CREATED)
                .build());

        order.setDeliveryId(deliveryDto.getDeliveryId());
        orderRepository.save(order);

        return orderMapper.toDto(order, orderProducts);
    }

    // Возврат заказа
    @Override
    @Transactional
    public OrderDto productReturn(ProductReturnRequest request) {

        Order order = validateOrder(request.getOrderId());

        warehouseClient.acceptReturn(request.getProducts());

        order.setOrderState(OrderState.PRODUCT_RETURNED);
        orderRepository.save(order);

        return buildOrderDto(order);
    }

    // Оплата заказа
    @Override
    @Transactional
    public OrderDto payment(UUID orderId) {

        Order order = orderRepository.findByPaymentId(orderId)
                .orElseGet(() -> validateOrder(orderId));

        if (order.getOrderState() == OrderState.ON_PAYMENT) {
            order.setOrderState(OrderState.PAID);
            orderRepository.save(order);
            return buildOrderDto(order);
        }

        OrderDto orderDto = buildOrderDto(order);
        PaymentDto paymentDto = paymentClient.payment(orderDto);

        order.setPaymentId(paymentDto.getPaymentId());
        order.setOrderState(OrderState.ON_PAYMENT);
        orderRepository.save(order);

        return buildOrderDto(order);
    }

    // Ошибка оплаты
    @Override
    @Transactional
    public OrderDto paymentFailed(UUID orderId) {

        Order order = orderRepository.findByPaymentId(orderId)
                .orElseGet(() -> validateOrder(orderId));

        order.setOrderState(OrderState.PAYMENT_FAILED);
        orderRepository.save(order);

        return buildOrderDto(order);
    }

    // Доставка заказа
    @Override
    @Transactional
    public OrderDto delivery(UUID orderId) {

        Order order = validateOrder(orderId);

        order.setOrderState(OrderState.DELIVERED);
        orderRepository.save(order);

        return buildOrderDto(order);
    }

    // Ошибка доставки
    @Override
    @Transactional
    public OrderDto deliveryFailed(UUID orderId) {

        Order order = validateOrder(orderId);

        order.setOrderState(OrderState.DELIVERY_FAILED);
        orderRepository.save(order);

        return buildOrderDto(order);
    }

    // Завершение заказа
    @Override
    @Transactional
    public OrderDto complete(UUID orderId) {

        Order order = validateOrder(orderId);

        order.setOrderState(OrderState.COMPLETED);
        orderRepository.save(order);

        return buildOrderDto(order);
    }

    // Расчёт итоговой стоимости
    @Override
    @Transactional
    public OrderDto calculateTotalCost(UUID orderId) {

        Order order = validateOrder(orderId);
        OrderDto orderDto = buildOrderDto(order);

        BigDecimal productPrice = paymentClient.productCost(orderDto);
        BigDecimal totalPrice = paymentClient.getTotalCost(orderDto);

        order.setProductPrice(productPrice);
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        return buildOrderDto(order);
    }

    // Расчёт стоимости доставки
    @Override
    @Transactional
    public OrderDto calculateDeliveryCost(UUID orderId) {

        Order order = validateOrder(orderId);

        OrderDto orderDto = buildOrderDto(order);

        order.setDeliveryPrice(deliveryClient.deliveryCost(orderDto));
        orderRepository.save(order);

        return buildOrderDto(order);
    }

    // Сборка заказа
    @Override
    @Transactional
    public OrderDto assembly(UUID orderId) {


        Order order = validateOrder(orderId);

        order.setOrderState(OrderState.ASSEMBLED);
        orderRepository.save(order);

        return buildOrderDto(order);
    }

    // Ошибка сборки
    @Override
    @Transactional
    public OrderDto assemblyFailed(UUID orderId) {

        Order order = validateOrder(orderId);
        order.setOrderState(OrderState.ASSEMBLY_FAILED);
        orderRepository.save(order);

        return buildOrderDto(order);
    }

    // Валидация и получение заказа
    private Order validateOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new NoOrderFoundException("Заказ не найден: " + orderId));
    }

    private OrderDto buildOrderDto(Order order) {
        List<OrderProduct> products = orderProductRepository.findByOrderId(order.getOrderId());
        return orderMapper.toDto(order, products);
    }
}
