#!/usr/bin/env bash

# Stop the MongoDB instance started by './start.sh'.

# Identify the directory of this script, which is also the root directory of the project.
SCRIPT=$(readlink -f "$0")
SCRIPT_PATH=$(dirname "$SCRIPT")

# Terminate the service.
docker container stop ereefs-pojo-mongodb

docker image prune --force
docker container prune --force
docker network prune --force
