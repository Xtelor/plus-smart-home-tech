package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.enums.PaymentState;
import ru.yandex.practicum.exceptions.NoOrderFoundException;
import ru.yandex.practicum.exceptions.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.feign.OrderClient;
import ru.yandex.practicum.feign.ShoppingStoreClient;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderClient orderClient;
    private final ShoppingStoreClient shoppingStoreClient;

    // Формирование оплаты для заказа
    @Override
    @Transactional
    public PaymentDto payment(OrderDto dto) {

        validateOrder(dto);

        BigDecimal productCost = productCost(dto);
        BigDecimal fee = productCost.multiply(BigDecimal.valueOf(0.1));
        BigDecimal totalCost = productCost.add(fee).add(dto.getDeliveryPrice());

        Payment payment = Payment.builder()
                .paymentId(UUID.randomUUID())
                .totalPayment(totalCost)
                .deliveryTotal(dto.getDeliveryPrice())
                .feeTotal(fee)
                .paymentState(PaymentState.PENDING)
                .build();

        paymentRepository.save(payment);

        return paymentMapper.toDto(payment);
    }

    // Расчёт полной стоимости заказа
    @Override
    public BigDecimal getTotalCost(OrderDto dto) {

        validateOrder(dto);

        BigDecimal productCost = productCost(dto);
        BigDecimal fee = productCost.multiply(BigDecimal.valueOf(0.1));

        return productCost.add(fee).add(dto.getDeliveryPrice());
    }

    // Метод для эмуляции успешной оплаты в платежном шлюзе
    @Override
    public void paymentSuccess(UUID paymentId) {

        Payment payment = findPayment(paymentId);

        payment.setPaymentState(PaymentState.SUCCESS);
        paymentRepository.save(payment);

        orderClient.payment(paymentId);
    }

    // Расчёт стоимости товаров в заказе
    @Override
    public BigDecimal productCost(OrderDto dto) {

        if (dto.getProducts() == null || dto.getProducts().isEmpty()) {
            throw new NotEnoughInfoInOrderToCalculateException(
                    "Нет товаров в заказе для расчёта.");
        }

        BigDecimal totalProductCost = BigDecimal.ZERO;

        for (Map.Entry<UUID, Long> entry : dto.getProducts().entrySet()) {

            ProductDto product = shoppingStoreClient.getProduct(entry.getKey());

            BigDecimal productPrice = product.getPrice()
                    .multiply(BigDecimal.valueOf(entry.getValue()));

            totalProductCost = totalProductCost.add(productPrice);
        }

        return totalProductCost;
    }

    // Метод для эмуляции отказа в оплате платежного шлюза
    @Override
    @Transactional
    public void paymentFailed(UUID paymentId) {

        Payment payment = findPayment(paymentId);

        payment.setPaymentState(PaymentState.FAILED);
        paymentRepository.save(payment);

        orderClient.paymentFailed(paymentId);
    }

    // Метод валидации заказа
    private void validateOrder(OrderDto dto) {
        if (dto.getProducts() == null || dto.getProducts().isEmpty()) {
            throw new NotEnoughInfoInOrderToCalculateException(
                    "Нет товаров в заказе для расчёта.");
        }
        if (dto.getDeliveryPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException(
                    "Нет стоимости доставки для расчёта.");
        }
    }

    // Метод поиска и проверка оплаты
    private Payment findPayment(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoOrderFoundException(
                        "Оплата не найдена: " + paymentId));
    }
}
