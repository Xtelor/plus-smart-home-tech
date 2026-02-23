package ru.yandex.practicum.collector.producer;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaEventProducer {
    private final KafkaProducer<String, SpecificRecordBase> kafkaProducer;

    public void send(ProducerRecord<String, SpecificRecordBase> record) {
        kafkaProducer.send(record);
    }
}
