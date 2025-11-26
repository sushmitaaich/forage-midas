package com.jpmc.midascore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Test-side Kafka producer used by TaskTwoTests.
 * Converts Transaction to JSON before sending because the configured
 * producer uses StringSerializer for values.
 */
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${general.kafka-topic:test-topic}")
    private String topic;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Serializes the Transaction to JSON and sends it as a String payload.
     */
    public void send(Transaction transaction) {
        try {
            String json = objectMapper.writeValueAsString(transaction);
            kafkaTemplate.send(topic, json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize Transaction to JSON", e);
        }
    }
}
