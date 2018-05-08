#!/bin/bash
git fetch --all
git reset --hard origin/master
git pull
mvn clean package
docker-compose down
rm -rf target/temp.jar
mv target/dzkandian-1.0.0-dev.jar target/temp.jar
docker-compose up -d
docker-compose logs -f