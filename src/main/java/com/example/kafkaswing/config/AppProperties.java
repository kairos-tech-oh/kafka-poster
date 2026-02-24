package com.example.kafkaswing.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private Map<String, PublisherConfig> publishers = new HashMap<>();

    public Map<String, PublisherConfig> getPublishers() {
        return publishers;
    }

    public void setPublishers(Map<String, PublisherConfig> publishers) {
        this.publishers = publishers;
    }
}

