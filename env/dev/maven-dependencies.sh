#!/usr/bin/env bash

#
# Use a Maven Docker image to build the dependency licence report.
#
# Notes:
#   - See README.md regarding version number (eg: SNAPSHOT-<yyyymmdd>).
#   - Maven is executed as the current user to ensure the Maven repository (${HOME}/.m2) is not owned by "root".
#

# The version of the Maven Docker image to use.
MAVEN_DOCKER_TAG="3.6.1-jdk-8-alpine"

# Identify the directory of this script.
SCRIPT=$(readlink -f "$0")
SCRIPT_PATH=$(dirname "${SCRIPT}")
PROJECT_ROOT=$(readlink --canonicalize "${SCRIPT_PATH}/../..")

# Ensure the ${HOME}/.m2 directory belongs to the current user.
mkdir -p ${HOME}/.m2

# Execute the Maven container to package and install the JAR.
docker run \
    -u $(id -u):$(id -g) \
    --rm \
    --name "maven-pojo-test" \
    --network=ereefs-pojo-network \
    -v "${HOME}/.m2:/tmp/maven/.m2" \
    -v ${PROJECT_ROOT}:/workdir \
    -w /workdir \
    -e MONGODB_URL="mongodb://pojo:pojo@ereefs-pojo-mongodb" \
    maven:${MAVEN_DOCKER_TAG} \
    mvn -Duser.home="/tmp/maven" clean project-info-reports:dependencies
