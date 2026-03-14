package ru.yandex.practicum.shared.model.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class MotionSensorEvent extends SensorEvent {

    // Качество связи
    @NotNull
    private Integer linkQuality;

    // Наличие/отсутствие движения
    @NotNull
    private Boolean motion;

    // Напряжение
    @NotNull
    private Integer voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
