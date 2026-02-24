package com.example.kafkaswing.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.avro.AvroModule;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new AvroModule())
            .registerModule(new JavaTimeModule()) // Register the Date/Time module
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false) // Optional: Use ISO strings
            .enable(SerializationFeature.INDENT_OUTPUT);

    @JsonIgnoreProperties({"schema", "specificData"})
    abstract class AvroIgnoreMixIn {}

    static {
        MAPPER.addMixIn(org.apache.avro.specific.SpecificRecordBase.class, AvroIgnoreMixIn.class);
    }

    public static String toJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert Avro to JSON", e);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
