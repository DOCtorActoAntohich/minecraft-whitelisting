# Minecraft
## Game server directory (`/server`)
We put files `eula.txt` with the confirmed agreement and `server.properties` with enabled whitelist to the `server/` folder so that it is not needed to change these files manually.
By launching the game server, you agree to [Minecraft EULA](https://account.mojang.com/documents/minecraft_eula).

To run the server, you also need to pick a `.jar` file you want and put it into the `server/` directory.
The setup was tested with Paper 1.16.5, available at the [download page of legacy Paper versions](https://papermc.io/legacy). However, any version compatible with Java 16 should work (you may change Java version too by editing the [Dockerfile](https://github.com/DOCtorActoAntohich/minecraft-whitelisting/blob/master/minecraft/Dockerfile))
