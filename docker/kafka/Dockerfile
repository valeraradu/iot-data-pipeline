# Kafka and Zookeeper

FROM java:openjdk-8-jre

ENV DEBIAN_FRONTEND noninteractive
ENV SCALA_VERSION 2.11
ENV KAFKA_VERSION 1.1.0
ENV KAFKA_HOME /opt/kafka_"$SCALA_VERSION"-"$KAFKA_VERSION"

# Install Kafka, Zookeeper and other needed things
RUN apt-get update && \
    apt-get install -y zookeeperd wget supervisor dnsutils && \
    rm -rf /var/lib/apt/lists/* && \
    apt-get clean && \
    wget -q http://apache.mirrors.spacedump.net/kafka/"$KAFKA_VERSION"/kafka_"$SCALA_VERSION"-"$KAFKA_VERSION".tgz -O /tmp/kafka_"$SCALA_VERSION"-"$KAFKA_VERSION".tgz && \
    tar xfz /tmp/kafka_"$SCALA_VERSION"-"$KAFKA_VERSION".tgz -C /opt && \
    rm /tmp/kafka_"$SCALA_VERSION"-"$KAFKA_VERSION".tgz

RUN echo '\nlisteners=PLAINTEXT://localhost:9092' >> /opt/kafka_"$SCALA_VERSION"-"$KAFKA_VERSION"/config/server.properties
RUN echo '\nauto.create.topics.enable=true' >> /opt/kafka_"$SCALA_VERSION"-"$KAFKA_VERSION"/config/server.properties

# 2181 is zookeeper, 9092 is kafka
EXPOSE 2181 9092

ENTRYPOINT cd /opt/kafka_"$SCALA_VERSION"-"$KAFKA_VERSION"\
 && service zookeeper start && ./bin/kafka-server-start.sh config/server.properties
