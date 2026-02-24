package com.example.kafkaswing.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Service;

@Service
public class Publisher {
    private final KafkaTemplate<String, String> kafkaTemplate; // typically wired to embedded broker

    public Publisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendToBootstrap(String bootstrapServers, String topic, String jsonPayload) {
        if (bootstrapServers == null || bootstrapServers.isEmpty() || "embedded".equalsIgnoreCase(bootstrapServers)) {
            // use injected template (embedded broker)
            send(topic, jsonPayload);
            return;
        }
        // create a temporary KafkaTemplate for the specified bootstrap servers
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        ProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<>(props);
        KafkaTemplate<String, String> temp = new KafkaTemplate<>(pf);
        temp.send(topic, jsonPayload);
        System.out.println("[Publisher] Sent to external broker '" + bootstrapServers + "' topic '" + topic + "': " + jsonPayload);
        // no explicit cleanup required for the temporary template, but you could stop the producer if needed
    }

    public void send(String topic, String jsonPayload) {
        kafkaTemplate.send(topic, jsonPayload);
        System.out.println("[Publisher] Sent to topic '" + topic + "': " + jsonPayload);
    }
}
