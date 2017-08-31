#!/bin/bash

rm Dockerfile

cp ubuntu-dockerfile.txt Dockerfile

docker build -t myubuntu:1 .

rm Dockerfile

cp catserver-dockerfile.txt Dockerfile

docker build -t catserver .

cd labdocker

mvn clean
mvn package

docker run catserver
