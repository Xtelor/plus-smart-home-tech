package ru.yandex.practicum.collector.service.handler.sensor;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.producer.KafkaEventProducer;
import ru.yandex.practicum.collector.service.SensorEventHandler;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ClimateSensorEventHandler implements SensorEventHandler {

    private final KafkaEventProducer kafkaEventProducer;


    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR;
    }

    @Override
    public void handle(SensorEventProto event) {
        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(
                        event.getTimestamp().getSeconds(),
                        event.getTimestamp().getNanos()))
                .setPayload(ClimateSensorAvro.newBuilder()
                        .setTemperatureC(event.getClimateSensor().getTemperatureC())
                        .setHumidity(event.getClimateSensor().getHumidity())
                        .setCo2Level(event.getClimateSensor().getCo2Level())
                        .build())
                .build();

        kafkaEventProducer.send(new ProducerRecord<>(
                "telemetry.sensors.v1",
                event.getHubId(),
                sensorEventAvro
        ));
    }
}
