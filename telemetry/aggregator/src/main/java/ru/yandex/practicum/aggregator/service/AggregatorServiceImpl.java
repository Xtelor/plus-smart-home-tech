package ru.yandex.practicum.aggregator.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class AggregatorServiceImpl implements AggregatorService {

    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    @Override
    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {

        SensorsSnapshotAvro currentSnapshot = snapshots.computeIfAbsent(
                event.getHubId(),
                hubId -> new SensorsSnapshotAvro(hubId, event.getTimestamp(), new HashMap<>())
        );

        SensorStateAvro currentStateOfSensor = currentSnapshot.getSensorsState().get(event.getId());

        if (currentStateOfSensor != null && event.getTimestamp().isBefore(currentStateOfSensor.getTimestamp())) {
            return Optional.empty();
        }

        SensorStateAvro newState = new SensorStateAvro(event.getTimestamp(), event.getPayload());

        if (currentStateOfSensor != null && currentStateOfSensor.getData().equals(newState.getData())) {
            return Optional.empty();
        }

        currentSnapshot.getSensorsState().put(event.getId(), newState);
        currentSnapshot.setTimestamp(event.getTimestamp());

        return Optional.of(currentSnapshot);
    }
}
