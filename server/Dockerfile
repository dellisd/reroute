FROM gradle:jdk17 AS builder

COPY . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle :server:shadowJar

FROM openjdk:17-slim-buster

COPY --from=builder /home/gradle/src/server/build/libs /usr/src/app
WORKDIR /usr/src/app
EXPOSE 8888
CMD ["java", "-jar", "server.jar", "server"]

LABEL org.opencontainers.image.source=https://github.com/dellisd/reroute
