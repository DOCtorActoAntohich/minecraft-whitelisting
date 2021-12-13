#!/bin/sh
CONSOLE_INPUT=/console/input

# Prepare the input pipe
[ -w "$CONSOLE_INPUT" ] || mkfifo "$CONSOLE_INPUT"
exec 3<> "$CONSOLE_INPUT"

# Find the jar file
JAR_NAME="$(find /server -maxdepth 1 -iname '*.jar')"

# Run the server with the pipe as input
java -Xmx1024M -Xms1024M -jar "$JAR_NAME" nogui < /console/input
