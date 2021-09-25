plugins {
    application
    kotlin("jvm") version Versions.kotlin
    id("org.jetbrains.kotlin.plugin.serialization") version Versions.kotlin
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
    // Ktor
    implementation(Deps.Ktor.auth)
    implementation(Deps.Ktor.authJwt)
    implementation(Deps.Ktor.serialization)
    implementation(Deps.Ktor.serverCore)
    implementation(Deps.Ktor.serverNetty)

    // Logging
    implementation(Deps.logback)

    // Tests
    // Kotlin
    testImplementation(Deps.Tests.Kotlin.test)

    // Ktor
    testImplementation(Deps.Tests.Ktor.serverTests)
}