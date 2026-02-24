package com.example.kafkaswing.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class BeanWrapperUtil {
    /**
     * Sets a nested property on the target object using dot-separated path (e.g., transaction.id)
     */
    public static void setNestedProperty(Object target, String path, Object value) {
        BeanWrapper bw = new BeanWrapperImpl(target);
        bw.setPropertyValue(path, value);
    }
}
