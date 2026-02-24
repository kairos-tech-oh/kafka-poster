package com.example.kafkaswing.util;

import com.example.kafkaswing.model.Sample;
import org.instancio.Instancio;

public class AvroInstancioFactory {
    public static Sample createSample() {
        return Instancio.create(Sample.class);
    }
}
