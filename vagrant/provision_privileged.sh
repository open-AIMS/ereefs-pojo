#!/usr/bin/env bash

#
# Prepare the VM.
#

echo ""
echo "-> Upgrade installed packages"
echo "-----------------------------"

echo "-> apt-get : refresh package list."
apt-get update

echo ""
echo "-> Install Docker Compose"
echo "-------------------------"

## Install Docker Compose
echo "-> Install Docker Compose"
curl -L "https://github.com/docker/compose/releases/download/1.24.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
