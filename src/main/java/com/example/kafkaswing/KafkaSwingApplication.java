package com.example.kafkaswing;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class KafkaSwingApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(KafkaSwingApplication.class)
                .headless(false)
                .run(args);
    }
}
