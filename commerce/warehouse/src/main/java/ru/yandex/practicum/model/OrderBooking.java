package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "order_bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderBooking {

    // ID заказа
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "order_id", nullable = false, length = 36)
    private UUID orderId;

    // ID доставки
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "delivery_id", nullable = false, length = 36)
    private UUID deliveryId;
}
