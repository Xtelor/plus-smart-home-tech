package ru.yandex.practicum.shared.model.hub;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class DeviceAction {
    // Идентификатор датчика, связанного с действием
    private String sensorId;

    // Тип действия
    private ActionType type;

    // Необязательное значение, связанное с действием
    @Builder.Default
    private Integer value = null;
}
