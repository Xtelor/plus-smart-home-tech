package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.yandex.practicum.enums.PaymentState;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "payment_id", nullable = false, length = 36)
    private UUID paymentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_state")
    private PaymentState paymentState;

    @Column(name = "total_payment")
    private BigDecimal totalPayment;

    @Column(name = "delivery_total")
    private BigDecimal deliveryTotal;

    @Column(name = "fee_total")
    private BigDecimal feeTotal;
}
