#!/bin/bash

rm Dockerfile

cp client-dockerfile.txt Dockerfile

docker build -t client .

cd labdocker

mvn clean
mvn package

docker run client
