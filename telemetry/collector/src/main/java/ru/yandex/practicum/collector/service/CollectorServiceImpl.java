package ru.yandex.practicum.collector.service;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.collector.mapper.HubEventMapper;
import ru.yandex.practicum.collector.mapper.SensorEventMapper;
import ru.yandex.practicum.collector.model.hub.HubEvent;
import ru.yandex.practicum.collector.model.sensor.SensorEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.collector.producer.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Service
@RequiredArgsConstructor
public class CollectorServiceImpl implements CollectorService{

    private static final String SENSORS_TOPIC = "telemetry.sensors.v1";
    private static final String HUBS_TOPIC = "telemetry.hubs.v1";

    private final KafkaEventProducer kafkaEventProducer;
    private final SensorEventMapper sensorEventMapper;
    private final HubEventMapper hubEventMapper;

    @Override
    public void collectSensorEvent(SensorEvent event) {
        SensorEventAvro avro = sensorEventMapper.toSensorEventAvro(event);
        kafkaEventProducer.send(new ProducerRecord<>(SENSORS_TOPIC, event.getHubId(), avro));
    }

    @Override
    public void collectHubEvent(HubEvent event) {
        HubEventAvro avro = hubEventMapper.toHubEventAvro(event);
        kafkaEventProducer.send(new ProducerRecord<>(HUBS_TOPIC, event.getHubId(), avro));
    }
}
