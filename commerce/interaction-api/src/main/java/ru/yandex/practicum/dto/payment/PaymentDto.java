package ru.yandex.practicum.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {

    // Идентификатор оплаты
    private UUID paymentId;

    // Общая стоимость
    private BigDecimal totalPayment;

    // Стоимость доставки
    private BigDecimal deliveryTotal;

    // Стоимость налога
    private BigDecimal feeTotal;
}
