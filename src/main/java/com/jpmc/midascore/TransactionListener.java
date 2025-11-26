package com.jpmc.midascore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {
    private static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final TransactionBuffer buffer;
    private final DatabaseConduit databaseConduit;

    public TransactionListener(TransactionBuffer buffer, DatabaseConduit databaseConduit) {
        this.buffer = buffer;
        this.databaseConduit = databaseConduit;
    }

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core-listener")
    public void receive(String payload) {
        try {
            Transaction transaction = objectMapper.readValue(payload, Transaction.class);

            logger.info("Received transaction: {}", transaction);

            // Task 2 - keeps buffering behavior intact
            buffer.add(transaction);

            // Task 3 - validate and persist to DB
            databaseConduit.processTransaction(transaction);

        } catch (Exception e) {
            logger.error("Failed to deserialize incoming message. payload={}", payload, e);
        }
    }
}
