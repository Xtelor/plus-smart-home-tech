package ru.yandex.practicum.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.enums.OrderState;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

    // Идентификатор заказа
    @NotNull
    private UUID orderId;

    // Идентификатор корзины
    private UUID shoppingCartId;

    // Отображение идентификатора товара на отобранное количество
    @NotNull
    private Map<UUID, Long> products;

    // Идентификатор оплаты
    private UUID paymentId;

    // Идентификатор доставки
    private UUID deliveryId;

    // Статус заказа
    private OrderState state;

    // Общий вес доставки
    private Double deliveryWeight;

    // Общий объем доставки
    private Double deliveryVolume;

    // Признак хрупкости заказа
    private Boolean fragile;

    // Общая стоимость
    private BigDecimal totalPrice;

    // Стоимость доставки
    private BigDecimal deliveryPrice;

    // Стоимость товаров в заказе
    private BigDecimal productPrice;
}
