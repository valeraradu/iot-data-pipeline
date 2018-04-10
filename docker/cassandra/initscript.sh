#!/bin/bash

cassandra -R &

for f in /init_table/*; do
  echo "$f"
  until cqlsh --username cassandra --password cassandra -f "$f"; do
   echo "Cassandra is unavailable - sleeping"; sleep 10;
  done
done

exec "$@"
