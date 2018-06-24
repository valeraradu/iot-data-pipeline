package com.iot.app.spark.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.app.spark.model.TemperatureEvent;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class TemperatureEventDeserializer implements Deserializer<TemperatureEvent> {
    private String encoding = "UTF8";

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        String propertyName = isKey ? "key.deserializer.encoding" :
                "value.deserializer.encoding";
        Object encodingValue = configs.get(propertyName);
        if (encodingValue == null)
            encodingValue = configs.get("deserializer.encoding");
        if (encodingValue != null && encodingValue instanceof String)
            encoding = (String) encodingValue;
    }

    @Override
    public TemperatureEvent deserialize(String topic, byte[] data) {

        if (data == null)
            return null;
        else
            try {
                return objectMapper.readValue(data, TemperatureEvent.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return null;

    }

    @Override
    public void close() {
        // nothing to do
    }
}
