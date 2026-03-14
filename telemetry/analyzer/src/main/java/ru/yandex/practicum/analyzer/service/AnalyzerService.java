package ru.yandex.practicum.analyzer.service;

import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.analyzer.model.*;
import ru.yandex.practicum.shared.model.hub.OperationType;
import ru.yandex.practicum.shared.model.hub.ConditionType;
import ru.yandex.practicum.analyzer.repository.*;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequestProto;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class AnalyzerService {

    private final ScenarioRepository scenarioRepository;
    private final ConditionRepository conditionRepository;
    private final ActionRepository actionRepository;
    private final ScenarioConditionRepository scenarioConditionRepository;
    private final ScenarioActionRepository scenarioActionRepository;
    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    public AnalyzerService(ScenarioRepository scenarioRepository,
                           ConditionRepository conditionRepository,
                           ActionRepository actionRepository,
                           ScenarioConditionRepository scenarioConditionRepository,
                           ScenarioActionRepository scenarioActionRepository,
                           @GrpcClient("hub-router") HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient) {
        this.scenarioRepository = scenarioRepository;
        this.conditionRepository = conditionRepository;
        this.actionRepository = actionRepository;
        this.scenarioConditionRepository = scenarioConditionRepository;
        this.scenarioActionRepository = scenarioActionRepository;
        this.hubRouterClient = hubRouterClient;
    }

    public void analyze(SensorsSnapshotAvro snapshot) {
        String hubId = snapshot.getHubId();
        Map<String, SensorStateAvro> sensorsState = snapshot.getSensorsState();

        scenarioRepository.findByHubId(hubId).stream()
                .filter(scenario -> checkConditions(scenario, sensorsState))
                .forEach(scenario -> executeActions(hubId, scenario));
    }

    private boolean checkConditions(Scenario scenario, Map<String, SensorStateAvro> sensorsState) {
        return scenarioConditionRepository.findByScenarioId(scenario.getId()).stream()
                .allMatch(sc -> checkCondition(sc, sensorsState));
    }

    private boolean checkCondition(ScenarioCondition sc, Map<String, SensorStateAvro> sensorsState) {
        SensorStateAvro sensorState = sensorsState.get(sc.getSensorId());
        if (sensorState == null) return false;

        return conditionRepository.findById(sc.getConditionId())
                .map(c -> evaluate(sensorState, c))
                .orElse(false);
    }

    private boolean evaluate(SensorStateAvro sensorState, Condition condition) {
        Integer value = extractValue(sensorState, ConditionType.valueOf(condition.getType()));
        if (value == null) return false;

        return switch (OperationType.valueOf(condition.getOperation())) {
            case EQUALS -> value.equals(condition.getValue());
            case GREATER_THAN -> value > condition.getValue();
            case LOWER_THAN -> value < condition.getValue();
        };
    }

    private Integer extractValue(SensorStateAvro sensorState, ConditionType type) {
        Object data = sensorState.getData();
        return switch (type) {
            case TEMPERATURE -> {
                if (data instanceof TemperatureSensorAvro t) yield t.getTemperatureC();
                if (data instanceof ClimateSensorAvro c) yield c.getTemperatureC();
                yield null;
            }
            case HUMIDITY -> data instanceof ClimateSensorAvro c ? c.getHumidity() : null;
            case CO2LEVEL -> data instanceof ClimateSensorAvro c ? c.getCo2Level() : null;
            case LUMINOSITY -> data instanceof LightSensorAvro l ? l.getLuminosity() : null;
            case MOTION -> data instanceof MotionSensorAvro m ? (m.getMotion() ? 1 : 0) : null;
            case SWITCH -> data instanceof SwitchSensorAvro s ? (s.getState() ? 1 : 0) : null;
        };
    }

    private void executeActions(String hubId, Scenario scenario) {
        Instant now = Instant.now();

        List<ScenarioAction> scenarioActions =
                scenarioActionRepository.findByScenarioId(scenario.getId());

        for (ru.yandex.practicum.analyzer.model.ScenarioAction sa : scenarioActions) {

            Optional<Action> actionOpt = actionRepository.findById(sa.getActionId());

            if (actionOpt.isPresent()) {
                Action action = actionOpt.get();

                DeviceActionRequestProto request = DeviceActionRequestProto.newBuilder()
                        .setHubId(hubId)
                        .setScenarioName(scenario.getName())
                        .setAction(DeviceActionProto.newBuilder()
                                .setSensorId(sa.getSensorId())
                                .setType(ActionTypeProto.valueOf(action.getType()))
                                .setValue(Optional.ofNullable(action.getValue()).orElse(0))
                                .build())
                        .setTimestamp(Timestamp.newBuilder()
                                .setSeconds(now.getEpochSecond())
                                .setNanos(now.getNano())
                                .build())
                        .build();

                hubRouterClient.handleDeviceAction(request);
            }
        }
    }
}