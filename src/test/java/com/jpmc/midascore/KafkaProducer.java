package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
@Component
public class KafkaProducer {
    private final String topic;
    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    public KafkaProducer(@Value("${general.kafka-topic}") String topic, KafkaTemplate<String, Transaction> kafkaTemplate) {
        this.topic = java.util.Objects.requireNonNull(topic, "general.kafka-topic must not be null");
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String transactionLine) {
        String[] transactionData = transactionLine.split(", ");
        if (transactionData.length >= 3 && transactionData[0] != null && transactionData[1] != null && transactionData[2] != null) {
            String accountId = java.util.Objects.requireNonNull(transactionData[0]);
            String counterparty = java.util.Objects.requireNonNull(transactionData[1]);
            String amount = java.util.Objects.requireNonNull(transactionData[2]);
            kafkaTemplate.send(topic, new Transaction(Long.parseLong(accountId), Long.parseLong(counterparty), Float.parseFloat(amount)));
        }
    }
}