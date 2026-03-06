package ru.yandex.practicum.collector.service.handler.hub;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.producer.KafkaEventProducer;
import ru.yandex.practicum.collector.service.HubEventHandler;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ScenarioRemovedEventHandler implements HubEventHandler {

    private final KafkaEventProducer kafkaEventProducer;

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }

    @Override
    public void handle(HubEventProto event) {
        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(
                        event.getTimestamp().getSeconds(),
                        event.getTimestamp().getNanos()
                ))
                .setPayload(ScenarioRemovedEventAvro.newBuilder()
                        .setName(event.getScenarioRemoved().getName())
                        .build())
                .build();

        kafkaEventProducer.send(new ProducerRecord<>(
                "telemetry.hubs.v1",
                event.getHubId(),
                hubEventAvro
        ));
    }
}
