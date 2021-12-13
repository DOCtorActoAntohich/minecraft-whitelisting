# Database (PostgreSQL)
To make the database persistent, we mount the volume under `db/data` directory. You may want to change this in [docker-compose.yml](https://github.com/DOCtorActoAntohich/minecraft-whitelisting/blob/master/docker-compose.yml)
The necessity of a `db` subfolder is related to permission issues. More info is in [this discussion on Github](https://github.com/docker/for-linux/issues/380).
