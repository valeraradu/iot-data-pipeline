
FROM bitnami/cassandra:3.11.2

ADD IoTData.cql /init_table/IoTData.cql
ADD initscript.sh /initscript.sh

ENTRYPOINT ["/app-entrypoint.sh"]
CMD /initscript.sh & /bin/bash