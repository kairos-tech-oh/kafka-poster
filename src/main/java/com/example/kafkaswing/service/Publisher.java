package com.example.kafkaswing.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Publisher {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public Publisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, String jsonPayload) {
        kafkaTemplate.send(topic, jsonPayload);
        System.out.println("[Publisher] Sent to topic '" + topic + "': " + jsonPayload);
    }
}

