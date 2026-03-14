package ru.yandex.practicum.shared.model.hub;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioAddedEvent extends HubEvent {
    // Название добавленного сценария (не менее 3 символов)
    @Size(min = 3)
    private String name;

    // Список условий, которые связаны со сценарием (не может быть пустым)
    @NotEmpty
    private List<ScenarioCondition> conditions;

    // Список действий, которые должны быть выполнены в рамках сценария (не может быть пустым)
    @NotEmpty
    private List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
