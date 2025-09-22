#!/usr/bin/env bash

docker run \
    -d \
    -p 8080:8080 \
    --cpus="1.0" \
    --memory="1g" \
    -e "JAVA_OPTS=-Xms256m -Xmx512m" \
    --name suki-local-test \
    suki-helper
