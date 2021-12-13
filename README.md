# Minecraft whitelisting

A bunch of Docker containers to implement automated whitelisting on Minecraft servers.

## Architecture

The project consists of the following components:
1. Front-end with registration form.
2. Back-end that performs email verification.
3. SQL database.
4. `whitelist add` request server.
5. Minecraft server with a Linux file pipeline to allow executing console commands.

Each component can be replaced with custom part to improve the system as you like.

## How to run

1. Install the latest `docker` and `docker-compose`.
2. Clone the `git` repository.
3. Go to the root folder (where `docker-compose.yml` is).
4. Run `docker-compose up -d`
