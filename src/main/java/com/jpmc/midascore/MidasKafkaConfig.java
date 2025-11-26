package com.jpmc.midascore;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Enable kafka support in the Spring context. Kept separate from the main app for clarity.
 */
@Configuration
@EnableKafka
public class MidasKafkaConfig { }
