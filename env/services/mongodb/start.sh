#!/usr/bin/env bash

# Start a MongoDB instance.
SCRIPT=$(readlink -f "$0")
SCRIPT_PATH=$(dirname "$SCRIPT")

docker image prune --force
docker container prune --force
docker network prune --force

# Start the service.
docker network create ereefs-pojo-network
docker run \
    -d \
    --rm \
    --name=ereefs-pojo-mongodb \
    --network=ereefs-pojo-network \
    -p 27017:27017 \
    -v ${SCRIPT_PATH}/init-scripts:/docker-entrypoint-initdb.d \
    mongo:4.2
