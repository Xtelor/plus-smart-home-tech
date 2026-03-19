package ru.yandex.practicum.collector.service.handler.hub;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.producer.KafkaEventProducer;
import ru.yandex.practicum.collector.service.HubEventHandler;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ScenarioAddedEventHandler implements HubEventHandler {

    private final KafkaEventProducer kafkaEventProducer;

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    public void handle(HubEventProto event) {
        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(
                        event.getTimestamp().getSeconds(),
                        event.getTimestamp().getNanos()
                ))
                .setPayload(ScenarioAddedEventAvro.newBuilder()
                        .setName(event.getScenarioAdded().getName())
                        .setConditions(event.getScenarioAdded().getConditionList().stream()
                                .map(c -> ScenarioConditionAvro.newBuilder()
                                        .setSensorId(c.getSensorId())
                                        .setType(ConditionTypeAvro.valueOf(c.getType().name()))
                                        .setOperation(ConditionOperationAvro.valueOf(c.getOperation().name()))
                                        .setValue(c.hasIntValue() ? c.getIntValue() : c.getBoolValue() ? 1 : 0)
                                        .build())
                                .collect(Collectors.toList()))
                        .setActions(event.getScenarioAdded().getActionList().stream()
                                .map(a -> DeviceActionAvro.newBuilder()
                                        .setSensorId(a.getSensorId())
                                        .setType(ActionTypeAvro.valueOf(a.getType().name()))
                                        .setValue(a.getValue())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .build();

        kafkaEventProducer.send(new ProducerRecord<>(
                "telemetry.hubs.v1",
                event.getHubId(),
                hubEventAvro
        ));
    }
}
