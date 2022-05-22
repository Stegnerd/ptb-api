[![Kotlin Version](https://img.shields.io/badge/kotlin-1.5.31-blue.svg)](http://kotlinlang.org/)
[![Gradle](https://lv.binarybabel.org/catalog-api/gradle/latest.svg?v=7.1)](https://lv.binarybabel.org/catalog/gradle/latest)
[![Ktor Version](https://img.shields.io/badge/ktor-1.6.3-blue.svg)](https://ktor.io/)

This api is the backend for the for a project I am working on called Pokemon Team Builder. 
It will be the api for all ui interfaces. It is a ktor api with a postgres database.


## Table of Contents
- [RoadMap](https://github.com/Stegnerd/ptb-api#roadmap)
- [Setup](https://github.com/Stegnerd/ptb-api#setup)
- [Architecture](https://github.com/Stegnerd/ptb-api#architecture)
- [Technology](https://github.com/Stegnerd/ptb-api#technology)

## Roadmap
- [x] Set up docker container for db
- [x] Set up flyway db migrations
- [x] Set up hosted Heroku
- [ ] Setup workflow action for CI
- [ ] Set up jacoco coverage
- [ ] basic get pokemon get routes
- [ ] set up trainer team crud routes
- [ ] Unit testing and Integration

#### v2
- [ ] Set up saving postgres db connection for docker
- [ ] Add plugins to build: Detekt, Ktlint
- [ ] Git pre commit hook for format/linting


## Setup
### To run project locally you will need to set up the following environment values:
1. install  [env file](https://plugins.jetbrains.com/plugin/7861-envfile) and set up a run configuration with it
2. modify env file with a . in front of it, and fill in values below:
- jdbcUrl: example: jdbc:postgresql://localhost:5432/ptb_api
- dbUser: example: whatever you set want (make sure it matches docker POSTGRES_USER)
- dbPassword: example: whatever you want (make sure matches POSTGRES_PASSWORD)
- jwtSecret: example: just a cryptic password
- jwtIssuer: example: https://whatever you want/
- jwtAudience: example: https://whatever you want/api
- jwtRealm: example: "grants access to api"

### How to get docker up and running
You will need to update the configurations files and remove the period in front of the file names:
- .docker-compose.yml
    - set the following values:
      - POSTGRES_USER
      - POSTGRES_PASSWORD
      - POSTGRES_DB
      - PGADMIN_DEFAULT_EMAIL
      - PGADMIN_DEFAULT_PASSWORD

### How to connect to items in docker container
- api: http://localhost:5031
- postgres: http://localhost:5021

### Setting up postgres connection
1. create server
2. name = what you want to see in postgres
3. host = database name in docker-compose file: aka db
4. user and pass = POSTGRES_USER && POSTGRES_PASSWORD

### Running
1. fill in all variables mentioned above
2. docker-compose up
3. run the api.
4. make sure you can hit `http://localhost:8080/auth/register`

### Docker Commands
#### How to start
- docker-compose up
#### How to tear down
- docker-compose down
#### How to tear down and reset env
- docker-compose down --volumes
#### How to stop but keep data
- docker-compose stop

## Architecture
- TBD

## Technology
### Dependencies

- [Exposed](https://github.com/JetBrains/Exposed): sql orm from jetbrains
- [flyway](https://github.com/flyway/flyway): db migrations
- [hikariCP](https://github.com/brettwooldridge/HikariCP): database wrapper for configurations
- [jBCrypt](https://github.com/jeremyh/jBCrypt): encryption library for password hashing
- [ktor](https://github.com/ktorio/ktor) lightweight minimal api framework
  - ktor-auth: authorization and authentication feature 
  - ktor-auth-jwt: jwt integration with ktor-auth
  - ktor-serialization: content negotiation feature
  - ktor-server-core: core functionality for ktor
  - ktor-server-netty: engine used to run server
- [shadow](https://github.com/johnrengelman/shadow): creating uber jar for when deploying app in docker container also
### Test Dependencies
- kotlin-test: Test Multiplatform library
- ktor-server-tests: helpers for ktor testing