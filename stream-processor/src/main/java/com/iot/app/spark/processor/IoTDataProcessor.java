package com.iot.app.spark.processor;

import com.iot.app.spark.model.TemperatureEvent;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashMap;
import java.util.Map;


@ComponentScan("com.device.com.simulator")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@SpringBootApplication
public class IoTDataProcessor implements CommandLineRunner {

    private static final Logger logger = Logger.getLogger(IoTDataProcessor.class);

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
    public void run(String... args) throws StreamingQueryException {

        SparkConf conf = new SparkConf()
                .setAppName(appName)
                .setMaster(sparkMaster)
                .set("spark.cassandra.connection.host", cassandraHost)
                .set("spark.cassandra.connection.port", cassandraPort)
                .set("spark.cassandra.auth.username", cassandraUsername)
                .set("spark.cassandra.auth.password", cassandraPassword)
                .set("spark.cassandra.connection.keep_alive_ms", cassandraKeep_alive)
                .set("spark.sql.streaming.checkpointLocation", checkpointDir);
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();

        Dataset<TemperatureEvent> dataStreamReader = spark
                .readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", brokerlist)
                .option("subscribe", kafkaTopic)
                .load().as(Encoders.bean(TemperatureEvent.class));

        Map<String, String> columnNameMappings = new HashMap<>();
        columnNameMappings.put("keyspace", "mykeyspace");
        columnNameMappings.put("table", "mytable");
        columnNameMappings.put("routeId", "routeid");
        columnNameMappings.put("vehicleType", "vehicletype");
        columnNameMappings.put("totalCount", "totalcount");
        columnNameMappings.put("timeStamp", "timestamp");
        columnNameMappings.put("recordDate", "recorddate");

        StreamingQuery query = dataStreamReader.groupBy(
                functions.window(dataStreamReader
                                .col("timestamp"),
                        "10 seconds", "5 seconds"))
                .count()
                .writeStream()
                .queryName("Test query")
                .format("cassandra")
                .options(columnNameMappings)
                .outputMode("update")
                .start();

        query.awaitTermination();

        return;

    }

/*    private void streamingApi() throws InterruptedException {
        SparkConf conf = new SparkConf()
                .setAppName(appName)
                .setMaster(sparkMaster)
                .set("spark.cassandra.connection.host", cassandraHost)
                .set("spark.cassandra.connection.port", cassandraPort)
                .set("spark.cassandra.connection.keep_alive_ms", cassandraKeep_alive);
        JavaStreamingContext jssc =
                new JavaStreamingContext(conf, Durations.seconds(5));

        jssc.checkpoint(checkpointDir);

        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("zookeeper.connect", zookeeper);
        kafkaParams.put("bootstrap.servers", brokerlist);
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", TemperatureEventDeserializer.class);
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("group.id", "mitosis");
        kafkaParams.put("enable.auto.commit", false);

        Collection<String> topics = Arrays.asList(kafkaTopic);

        JavaInputDStream<ConsumerRecord<String, TemperatureEvent>> stream =
                KafkaUtils.createDirectStream(jssc,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.Subscribe(topics, kafkaParams)
                );

        JavaDStream<TemperatureEvent> dStream = stream.map(tuple -> tuple.value());

        JavaPairDStream<TemperatureEvent, JavaDStream<TemperatureEvent>>
                javaPairInputDStream =
                dStream.mapToPair(te ->
                        new Tuple2<>(te, dStream.filter(te2 ->
                                GeoDistanceCalculator.isInPOIRadius(
                                        Double.valueOf(te.getLatitude()),
                                        Double.valueOf(te.getLongitude()),
                                        Double.valueOf(te2.getLatitude()),
                                        Double.valueOf(te2.getLongitude()),
                                        30
                                ))));

        javaPairInputDStream.map(te -> new EnrichedTemperatureEvent(
                te._1.getLatitude(),
                te._1.getLongitude(),
                te._1.getTemp(),
                te._2.va

        ))


        // Map Cassandra table column
        Map<String, String> columnNameMappings = new HashMap<>();
        columnNameMappings.put("routeId", "routeid");
        columnNameMappings.put("vehicleType", "vehicletype");
        columnNameMappings.put("totalCount", "totalcount");
        columnNameMappings.put("timeStamp", "timestamp");
        columnNameMappings.put("recordDate", "recorddate");

        // call CassandraStreamingJavaUtil function to save in DB
        javaFunctions(dStream)
                .writerBuilder("traffickeyspace",
                        "total_traffic",
                        CassandraJavaUtil.mapToRow(
                                EnrichedTemperatureEvent.class,
                                columnNameMappings)).saveToCassandra();


        jssc.start();
        jssc.awaitTermination();
    }*/

}


