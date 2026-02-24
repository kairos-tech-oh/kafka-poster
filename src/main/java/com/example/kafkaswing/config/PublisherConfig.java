package com.example.kafkaswing.config;

import java.util.HashMap;
import java.util.Map;

public class PublisherConfig {
    private Map<String, String> topics = new HashMap<>();
    private String pathToId;

    public Map<String, String> getTopics() {
        return topics;
    }

    public void setTopics(Map<String, String> topics) {
        this.topics = topics;
    }

    public String getPathToId() {
        return pathToId;
    }

    public void setPathToId(String pathToId) {
        this.pathToId = pathToId;
    }
}

