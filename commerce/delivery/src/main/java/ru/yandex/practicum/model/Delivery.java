package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.enums.DeliveryState;

import java.util.UUID;

@Entity
@Table(name = "deliveries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {

    // Идентификатор доставки
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "delivery_id", nullable = false, length = 36)
    private UUID deliveryId;

    // Адрес отправителя
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "from_address", nullable = false)
    private AddressDto fromAddress;

    // Адрес получателя
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "to_address", nullable = false)
    private AddressDto toAddress;

    // Идентификатор заказа
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "order_id", nullable = false, length = 36)
    private UUID orderId;

    // Статус доставки
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_state", nullable = false)
    private DeliveryState deliveryState;
}
