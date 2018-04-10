
FROM bitnami/cassandra:3.11.2

RUN mkdir -p /init_table
ADD IoTData.cql /init_table/IoTData.cql
ADD initscript.sh /initscript.sh

ENTRYPOINT ["/initscript.sh"]
CMD ["nami","start","--foreground","cassandra"]