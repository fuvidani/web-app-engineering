#!/bin/bash

auth="-u user -p devPassword"

# MONGODB USER CREATION
(
echo "setup mongodb auth"
create_user="if (!db.getUser('user')) { db.createUser({ user: 'user', pwd: 'devPassword', roles: [ {role:'readWrite', db:'waecmDatabase'} ]}) }"
until mongo waecmDatabase --eval "$create_user" || mongo waecmDatabase ${auth} --eval "$create_user"; do sleep 5; done
killall mongod
sleep 1
killall -9 mongod
) &

# INIT DUMP EXECUTION
(
if test -n "insert_script.js"; then
    echo "execute insert script"
	until mongo waecmDatabase ${auth} insert_script.js; do sleep 5; done
fi
) &

echo "start mongodb without auth"
chown -R mongodb /data/db
gosu mongodb mongod --config /mongod.conf "$@"

echo "restarting with auth on"
sleep 5
exec gosu mongodb mongod --auth --config /mongod.conf "$@"