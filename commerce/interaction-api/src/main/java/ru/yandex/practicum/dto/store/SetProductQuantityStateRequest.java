package ru.yandex.practicum.dto.store;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.enums.QuantityState;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetProductQuantityStateRequest {
    @NotNull
    private UUID productId;

    @NotNull
    private QuantityState quantityState;
}
