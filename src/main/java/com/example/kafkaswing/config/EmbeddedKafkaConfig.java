package com.example.kafkaswing.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
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

    private final AppProperties appProperties;

    public EmbeddedKafkaConfig(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Bean(destroyMethod = "destroy")
    public EmbeddedKafkaBroker embeddedKafkaBroker() {
        EmbeddedKafkaBroker broker = new EmbeddedKafkaBroker(1, true, 1);

        // Create topics for the 'local' environment based on configured publishers
        try {
            List<NewTopic> topics = new ArrayList<>();
            appProperties.getPublishers().values().forEach(pc -> {
                String t = pc.getTopics().get("local");
                if (t != null && !t.trim().isEmpty()) {
                    topics.add(new NewTopic(t, 1, (short) 1));
                }
            });
            if (!topics.isEmpty()) {
                broker.addTopics(topics.toArray(new NewTopic[0]));
            }
        } catch (Exception e) {
            System.out.println("[EmbeddedKafkaConfig] Failed to pre-create local topics: " + e.getMessage());
        }

        // Log the embedded broker address so other local apps can connect
        try {
            String bs = broker.getBrokersAsString();
            System.out.println("[EmbeddedKafkaConfig] Embedded Kafka broker bootstrap servers: " + bs);
            // write to a file so local apps can programmatically pick it up
            try {
                Path out = Path.of("target", "embedded-kafka.bootstrap");
                Files.createDirectories(out.getParent());
                Files.writeString(out, bs, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
            } catch (Exception wf) {
                System.out.println("[EmbeddedKafkaConfig] Failed to write bootstrap file: " + wf.getMessage());
            }
        } catch (Exception e) {
            // ignore
        }

        return broker;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(EmbeddedKafkaBroker broker) {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(broker);
        ProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<>(producerProps, new StringSerializer(), new StringSerializer());
        return new KafkaTemplate<>(pf);
    }
}
