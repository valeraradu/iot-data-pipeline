package com.device.simulator.util;

import com.device.simulator.model.TemperatureEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;


public class Sender {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);

    @Value("${com.iot.app.kafka.topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, TemperatureEvent> kafkaTemplate;

    public void send(TemperatureEvent temperatureEvent) {
        LOGGER.info("sending temperatureEvent='{}'", temperatureEvent.toString());
        kafkaTemplate.send(topic, temperatureEvent);
    }
}