#!/bin/bash

echo "Sleeping for 15 seconds so that Mongo service can start peacefully..."
sleep 15
echo "Woke up, starting backend..."
exec "$@"
