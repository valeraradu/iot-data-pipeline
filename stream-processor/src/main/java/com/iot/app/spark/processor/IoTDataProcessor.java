package com.iot.app.spark.processor;

import com.google.common.base.Optional;
import com.iot.app.spark.model.TemperatureEvent;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function3;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.streaming.State;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import scala.Tuple2;

@ComponentScan("com.device.com.simulator")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@SpringBootApplication
public class IoTDataProcessor implements CommandLineRunner {

    private static final Logger logger = Logger.getLogger(IoTDataProcessor.class);
    private static final Function3<String, Optional<TemperatureEvent>, State<Boolean>, Tuple2<TemperatureEvent, Boolean>> processedVehicleFunc = (String, iot, state) -> {
        Tuple2<TemperatureEvent, Boolean> vehicle = new Tuple2<>(iot.get(), false);
        if (state.exists()) {
            vehicle = new Tuple2<>(iot.get(), true);
        } else {
            state.update(Boolean.TRUE);
        }
        return vehicle;
    };

    @Value("${com.iot.app.spark.app.name}")
    private String appName;

    @Value("${com.iot.app.spark.master}")
    private String sparkMaster;

    @Value("${com.iot.app.cassandra.host}")
    private String cassandraHost;

    @Value("${com.iot.app.cassandra.port}")
    private String cassandraPort;

    @Value("${com.iot.app.cassandra.username}")
    private String cassandraUsername;

    @Value("${com.iot.app.cassandra.password}")
    private String cassandraPassword;

    @Value("${com.iot.app.cassandra.keep_alive}")
    private String cassandraKeep_alive;

    @Value("${com.iot.app.spark.checkpoint.dir}")
    private String checkpointDir;

    @Value("${com.iot.app.kafka.zookeeper}")
    private String zookeeper;

    @Value("${com.iot.app.kafka.brokerlist}")
    private String brokerlist;

    @Value("${com.iot.app.kafka.topic}")
    private String kafkaTopic;


    public static void main(String[] args) throws Exception {

        SpringApplication.run(IoTDataProcessor.class, args);

    }

    @Override
    public void run(String... args) throws StreamingQueryException, InterruptedException {

        SparkConf conf = new SparkConf()
                .setAppName(appName)
                .setMaster(sparkMaster)
                .set("spark.cassandra.connection.host", cassandraHost)
                .set("spark.cassandra.connection.port", cassandraPort)
                .set("spark.cassandra.auth.username", cassandraUsername)
                .set("spark.cassandra.auth.password", cassandraPassword)
                .set("spark.cassandra.connection.keep_alive_ms", cassandraKeep_alive);

        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();

        Dataset<TemperatureEvent> dataStreamReader = spark
                .readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", brokerlist)
                .option("subscribe", kafkaTopic)
                .load().as(Encoders.bean(TemperatureEvent.class));

        StreamingQuery query = dataStreamReader.groupBy("str1")
                .count()
                .writeStream()
                .queryName("Test query")
                .outputMode("complete")
                .format("console")
                .start();

        query.awaitTermination();

        }

    }


