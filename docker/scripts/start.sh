#!/usr/bin/env bash


chmod +x /app/scripts/wait-for-it.sh

/app/scripts/wait-for-it.sh postgres:5432 -t 180


/usr/bin/java -Djava.security.egd=file:/dev/./urandom -jar /opt/movierama/movierama-recommendation-service.jar
