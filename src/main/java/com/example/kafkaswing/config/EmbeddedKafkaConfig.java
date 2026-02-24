package com.example.kafkaswing.config;

import java.util.Map;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

@Configuration
public class EmbeddedKafkaConfig {

    @Bean(destroyMethod = "destroy")
    public EmbeddedKafkaBroker embeddedKafkaBroker() {
        return new EmbeddedKafkaBroker(1, true, 1);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(EmbeddedKafkaBroker broker) {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(broker);
        ProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<>(producerProps, new StringSerializer(), new StringSerializer());
        return new KafkaTemplate<>(pf);
    }
}
