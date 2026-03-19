package ru.yandex.practicum.analyzer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotProcessor {

    private final KafkaConsumer<String, SensorsSnapshotAvro> snapshotConsumer;
    private final AnalyzerService analyzerService;

    public void start() {
        try {
            snapshotConsumer.subscribe(List.of("telemetry.snapshots.v1"));
            while (true) {
                snapshotConsumer.poll(Duration.ofMillis(1000))
                        .forEach(record -> {
                            try {
                                analyzerService.analyze(record.value());
                            } catch (Exception e) {
                                log.warn("Ошибка обработки снапшота для хаба {}: {}",
                                        record.value().getHubId(), e.getMessage());
                            }
                        });
                snapshotConsumer.commitSync();
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка обработки снапшотов", e);
        } finally {
            snapshotConsumer.close();
        }
    }

    public void stop() {
        snapshotConsumer.wakeup();
    }
}
