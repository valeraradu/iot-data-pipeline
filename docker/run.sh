#!/bin/bash

#WARNING a lot of console output!!!

cd cassandra &&\
 docker build . -t cassandra:3.11.2 &&\
 docker run -t -p 9042:9042 cassandra:3.11.2 &

cd kafka &&\
 docker build . -t kafka:1.1.0 &&\
 exec docker run -t --net=host kafka:1.1.0 &

