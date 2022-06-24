FROM gradle:jdk17 AS builder

COPY . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle :web:jsBrowserWebpack

FROM nginx:1.23.0

COPY --from=builder /home/gradle/src/web/build/distributions /usr/share/nginx/html

LABEL org.opencontainers.image.source=https://github.com/dellisd/reroute
