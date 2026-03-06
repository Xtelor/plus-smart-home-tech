package ru.yandex.practicum.collector.service;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Optional;

public interface AggregatorService {
    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event);
}
