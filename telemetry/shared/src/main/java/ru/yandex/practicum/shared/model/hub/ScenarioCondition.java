package ru.yandex.practicum.shared.model.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ScenarioCondition {
    // Идентификатор датчика, связанного с условием
    private String sensorId;

    // Тип условия
    private ConditionType type;

    // Операция, которая может быть использована
    private OperationType operation;

    // Значение, используемое в условии
    private Integer value;
}
