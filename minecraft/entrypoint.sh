#!/bin/sh
DIR="/server"
NEW_USER="mc-server"
NEW_GROUP="mc-server"

# Find out the owner of the directory mounted as a volume
NEW_UID="$(stat -c '%u' $DIR)"
NEW_GID="$(stat -c '%g' $DIR)"

# Create group and user with the same IDs
groupadd -g $NEW_GID $NEW_GROUP
useradd -u $NEW_UID -g $NEW_GID $NEW_USER

# Set permissions on the shared volume
chmod -R 777 /console

# Start the task as the newly created user
cd $DIR
exec su $NEW_USER -c "/run-server.sh"
