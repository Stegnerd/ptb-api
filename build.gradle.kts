plugins {
    application
    kotlin("jvm") version Versions.kotlin
    id("org.jetbrains.kotlin.plugin.serialization") version Versions.kotlin
    id("org.flywaydb.flyway") version Versions.flyway
}

group = "com.stegnerd"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
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

flyway {
    // TODO: Might need to change this??
    url = System.getenv("DB_URL")
    user = System.getenv("DB_USER")
    password = System.getenv("DB_PASSWORD")
    baselineOnMigrate=true
    locations = arrayOf("filesystem:resources/db/migrations")
}