package ru.yandex.practicum.collector.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.model.hub.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;

@Component
public class HubEventMapper {
    public HubEventAvro toHubEventAvro(HubEvent event) {
        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(toPayloadAvro(event))
                .build();
    }

    private Object toPayloadAvro(HubEvent event) {
        return switch (event.getType()) {
            case DEVICE_ADDED -> toDeviceAddedEventAvro((DeviceAddedEvent) event);
            case DEVICE_REMOVED -> toDeviceRemovedEventAvro((DeviceRemovedEvent) event);
            case SCENARIO_ADDED -> toScenarioAddedEventAvro((ScenarioAddedEvent) event);
            case SCENARIO_REMOVED -> toScenarioRemovedEventAvro((ScenarioRemovedEvent) event);
        };
    }

    private DeviceAddedEventAvro toDeviceAddedEventAvro(DeviceAddedEvent event) {
        return DeviceAddedEventAvro.newBuilder()
                .setId(event.getId())
                .setType(DeviceTypeAvro.valueOf(event.getDeviceType().name()))
                .build();
    }

    private DeviceRemovedEventAvro toDeviceRemovedEventAvro(DeviceRemovedEvent event) {
        return DeviceRemovedEventAvro.newBuilder()
                .setId(event.getId())
                .build();
    }

    private ScenarioAddedEventAvro toScenarioAddedEventAvro(ScenarioAddedEvent event) {
        return ScenarioAddedEventAvro.newBuilder()
                .setName(event.getName())
                .setConditions(toConditionsAvro(event.getConditions()))
                .setActions(toActionsAvro(event.getActions()))
                .build();
    }

    private ScenarioRemovedEventAvro toScenarioRemovedEventAvro(ScenarioRemovedEvent event) {
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(event.getName())
                .build();
    }

    private List<ScenarioConditionAvro> toConditionsAvro(List<ScenarioCondition> conditions) {
        return conditions.stream()
                .map(this::toConditionAvro)
                .toList();
    }

    private ScenarioConditionAvro toConditionAvro(ScenarioCondition condition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(condition.getSensorId())
                .setType(ConditionTypeAvro.valueOf(condition.getType().name()))
                .setOperation(ConditionOperationAvro.valueOf(condition.getOperation().name()))
                .setValue(condition.getValue())
                .build();
    }

    private List<DeviceActionAvro> toActionsAvro(List<DeviceAction> actions) {
        return actions.stream()
                .map(this::toActionAvro)
                .toList();
    }

    private DeviceActionAvro toActionAvro(DeviceAction action) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setType(ActionTypeAvro.valueOf(action.getType().name()))
                .setValue(action.getValue())
                .build();
    }
}
