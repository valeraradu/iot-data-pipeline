package com.device.simulator.producer;

import com.device.simulator.model.TemperatureEvent;
import com.device.simulator.util.Sender;
import com.device.simulator.util.SenderConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@ComponentScan("com.device")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@SpringBootApplication
public class IoTDataProducer implements CommandLineRunner {

    private static final Logger logger = Logger.getLogger(IoTDataProducer.class);

    @Value("${com.iot.app.kafka.zookeeper}")
    private String zookeeper;

    @Value("${com.iot.app.kafka.brokerlist}")
    private String brokerList;

    @Value("${com.iot.app.kafka.topic}")
    private String topic;

    @Autowired
    private Sender sender;

    public static void main(String[] args) throws Exception {

        SpringApplication.run(IoTDataProducer.class, args);

    }

    @Override
    public void run(String... args) throws Exception {

        logger.info("Using Zookeeper=" + zookeeper + " ,Broker-list=" + brokerList + " and topic " + topic);

        generateIoTEvent(topic);

    }

    private void generateIoTEvent(String topic) throws InterruptedException {

        Random rand = new Random();
        logger.info("Sending events");

        while (true) {
            List<TemperatureEvent> eventList = new ArrayList<TemperatureEvent>();
            for (int i = 0; i < 100; i++) {
                Integer temperature = 20 + new Random().nextInt(5);

                for (int j = 0; j < 5; j++) {
                    String coords = getCoordinates();
                    String latitude = coords.substring(0, coords.indexOf(","));
                    String longitude = coords.substring(coords.indexOf(",") + 1, coords.length());
                    TemperatureEvent event = new TemperatureEvent(latitude, longitude, temperature);
                    eventList.add(event);
                }
            }
            Collections.shuffle(eventList);// shuffle for random events
            for (TemperatureEvent event : eventList) {

                sender.send(event);

                Thread.sleep(rand.nextInt(3000 - 1000) + 1000);//random delay of 1 to 3 seconds
            }
        }
    }

    private String getCoordinates() {
        Random rand = new Random();
        int latPrefix = 0;
        int longPrefix = -0;
        Float lati = latPrefix + rand.nextFloat();
        Float longi = longPrefix + rand.nextFloat();
        return lati + "," + longi;
    }
}

