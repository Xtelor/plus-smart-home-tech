package ru.yandex.practicum.analyzer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.model.Action;
import ru.yandex.practicum.analyzer.model.Condition;
import ru.yandex.practicum.analyzer.model.Scenario;
import ru.yandex.practicum.analyzer.model.ScenarioAction;
import ru.yandex.practicum.analyzer.model.ScenarioCondition;
import ru.yandex.practicum.analyzer.model.Sensor;
import ru.yandex.practicum.analyzer.repository.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventProcessor implements Runnable {

    private final KafkaConsumer<String, HubEventAvro> hubEventConsumer;
    private final SensorRepository sensorRepository;
    private final ScenarioRepository scenarioRepository;
    private final ConditionRepository conditionRepository;
    private final ActionRepository actionRepository;
    private final ScenarioConditionRepository scenarioConditionRepository;
    private final ScenarioActionRepository scenarioActionRepository;

    @Override
    public void run() {
        try {
            hubEventConsumer.subscribe(List.of("telemetry.hubs.v1"));
            while (true) {
                hubEventConsumer.poll(Duration.ofMillis(100))
                        .forEach(r -> processEvent(r.value()));
                hubEventConsumer.commitSync();
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка обработки событий хаба", e);
        } finally {
            hubEventConsumer.close();
        }
    }

    public void stop() {
        hubEventConsumer.wakeup();
    }

    public void processEvent(HubEventAvro event) {
        String hubId = event.getHubId();
        switch (event.getPayload()) {
            case DeviceAddedEventAvro e -> addSensor(hubId, e.getId());
            case DeviceRemovedEventAvro e -> sensorRepository.deleteById(e.getId());
            case ScenarioAddedEventAvro e -> addScenario(hubId, e);
            case ScenarioRemovedEventAvro e -> removeScenario(hubId, e.getName());
            default -> {}
        }
    }

    private void addSensor(String hubId, String id) {
        if (!sensorRepository.existsById(id)) {
            sensorRepository.save(new Sensor(id, hubId));
        }
    }

    private void addScenario(String hubId, ScenarioAddedEventAvro event) {
        if (scenarioRepository.findByHubIdAndName(hubId, event.getName()).isPresent()) {
            return;
        }

        Scenario scenario = scenarioRepository.save(new Scenario(hubId, event.getName()));

        event.getConditions().forEach(cond -> {
            String type = cond.getType().name();
            String operation = cond.getOperation().name();

            Integer value;
            Object condValue = cond.getValue();
            if (condValue instanceof Boolean) {
                value = (Boolean) condValue ? 1 : 0;
            } else if (condValue instanceof Integer) {
                value = (Integer) condValue;
            } else {
                log.error("Неизвестный тип значения в условии: {}", condValue);
                return;
            }

            Condition condition = conditionRepository.findByTypeAndOperationAndValue(type, operation, value)
                    .orElseGet(() -> conditionRepository.save(new Condition(type, operation, value)));

            scenarioConditionRepository.save(new ScenarioCondition(
                    scenario.getId(),
                    cond.getSensorId(),
                    condition.getId()));
        });

        event.getActions().forEach(act -> {
            String type = act.getType().name();
            Integer value = act.getValue();

            Action action = actionRepository.findByTypeAndValue(type, value)
                    .orElseGet(() -> actionRepository.save(new Action(type, value)));

            scenarioActionRepository.save(new ScenarioAction(
                    scenario.getId(),
                    act.getSensorId(),
                    action.getId())
            );
        });
    }

    private void removeScenario(String hubId, String name) {
        scenarioRepository.findByHubIdAndName(hubId, name).ifPresent(s -> {
            scenarioConditionRepository.deleteByScenarioId(s.getId());
            scenarioActionRepository.deleteByScenarioId(s.getId());
            scenarioRepository.delete(s);
        });
    }
}