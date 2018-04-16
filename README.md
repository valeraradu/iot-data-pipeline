# IoT Data Pipeline

heavily inspired from https://github.com/baghelamit/iot-traffic-monitor

that this system does is it receives a continuous stream of temperature events with coordinates
and calculates median temperature for each event in radius 30 km and 1 hour timeframe before storing event
in database

Requerements:
ubuntu(possible to make it work with any other os but as is it will not work due to some docker networking
 issues like kafka can not accept connections on localhost if it is not on the same network),
 gradle 4, docker, java 8, RAM 8Gb(16 preferable), 3GB free space on disk

Kafka, Spark, Cassandra, Spring Boot

Improvements: receiving messages via MQTT bridge, autoscaling with Kubernetes or Docker Swarm

