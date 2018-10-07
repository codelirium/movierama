FROM openjdk:8-jre

MAINTAINER codelirium


RUN mkdir -p /app/scripts

COPY ./docker/scripts/wait-for-it.sh /app/scripts

COPY ./docker/scripts/start.sh /app/scripts


RUN mkdir -p /opt/movierama

COPY ./target/movierama-recommendation-service.jar /opt/movierama


RUN mkdir -p /var/cache/api/movierama


RUN chmod +x /app/scripts/start.sh

ENTRYPOINT ["/app/scripts/start.sh"]
