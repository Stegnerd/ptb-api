plugins {
    application
    kotlin("jvm") version Versions.kotlin
    id("org.jetbrains.kotlin.plugin.serialization") version Versions.kotlin
    id("org.flywaydb.flyway") version Versions.flyway
    id("com.github.johnrengelman.shadow") version Versions.shadow
}

group = "com.stegnerd"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
}

// this builds an uber jar for the docker container to deploy with
tasks{
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "io.ktor.server.netty.EngineMain"))
        }
    }
}

// used for heroku
tasks.create("stage") {
    dependsOn("installDist")
}

dependencies {
    // Database
    implementation(Deps.Database.exposedCore)
    implementation(Deps.Database.exposedDao)
    implementation(Deps.Database.exposedJDBC)
    implementation(Deps.Database.flyway)
    implementation(Deps.Database.hikariCP)
    implementation(Deps.Database.postgresql)

    // Ktor
    implementation(Deps.Ktor.auth)
    implementation(Deps.Ktor.authJwt)
    implementation(Deps.Ktor.serialization)
    implementation(Deps.Ktor.serverCore)
    implementation(Deps.Ktor.serverNetty)

    // Logging
    implementation(Deps.logback)

    // Security
    implementation(Deps.jbcrypt)

    // Tests
    // Kotlin
    testImplementation(Deps.Tests.Kotlin.test)

    // Ktor
    testImplementation(Deps.Tests.Ktor.serverTests)
}

val dbUrl: String by project
val dbUser: String by project
val dbPassword: String by project
flyway {
    url = System.getenv("jdbcUrl")
    user = System.getenv("dbUser")
    password = System.getenv("dbPassword")
    baselineOnMigrate=true
    locations = arrayOf("filesystem:resources/db/migrations")
}