
# How to get up and running
You will need to update the configurations files and remove the period in front of the file names:
- .docker-compose.yml
- src/main/resources/.application.conf
- gradle.properties

TODO:
1. env file for env variables (remove hard coded strings!)
2. connection json or volume for postrges in docker? (auto connect to it?)
3. set up sample http requests
4. HTTPS

### How to connect to items in docker container
- api: http://localhost:5031
- postgres: http://localhost:5021

### Setting up postgres connection
1. create server
2. name = what you want to see in postgres
3. host = database name in docker-compose file: aka db
4. user and pass = POSTGRES_USER && POSTGRES_PASSWORD

## Docker Commands
#### How to start
- docker-compose up
#### How to tear down
- docker-compose down
#### How to tear down and reset env
- docker-compose down --volumes
#### How to stop but keep data
- docker-compose stop
#### If you change something about the image you may need to delete them in docker dashboard to trigger updates
