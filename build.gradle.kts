import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

val dbUrl: String by project
val dbUser: String by project
val dbPassword: String by project

plugins {
    application
    kotlin("jvm") version Versions.kotlin
    id("org.jetbrains.kotlin.plugin.serialization") version Versions.kotlin
    id("org.flywaydb.flyway") version Versions.flyway
    id("com.github.johnrengelman.shadow") version Versions.shadow
}

group = "com.stegnerd"
version = "2.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
}

flyway {
    url = System.getenv("jdbcUrl")
    user = System.getenv("dbUser")
    password = System.getenv("dbPassword")
    baselineOnMigrate=true
    locations = arrayOf("filesystem:resources/db/migrations")
}

dependencies {
    // DI
    implementation(Deps.koin)
    // Database
    implementation(Deps.Database.exposedCore)
    implementation(Deps.Database.exposedDao)
    implementation(Deps.Database.exposedJavaTime)
    implementation(Deps.Database.exposedJDBC)
    implementation(Deps.Database.flyway)
    implementation(Deps.Database.h2)
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
    testImplementation(Deps.Tests.assertJ)
    testImplementation(Deps.Tests.mockk)
    //Junit
    testImplementation(Deps.Tests.Junit.jupiter)
    testRuntimeOnly(Deps.Tests.Junit.jupiterEngine)
    // Kotlin
    testImplementation(Deps.Tests.Kotlin.test)
    // Ktor
    testImplementation(Deps.Tests.Ktor.serverTests)
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

tasks.test {
    useJUnitPlatform()

    testLogging {
        lifecycle {
            events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
            exceptionFormat = TestExceptionFormat.FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true
            // turn this on for extra debugging during build
            showStandardStreams = false
        }
        info.events = lifecycle.events
        info.exceptionFormat = lifecycle.exceptionFormat
    }

    val failedTests = mutableListOf<TestDescriptor>()
    val skippedTests = mutableListOf<TestDescriptor>()

    // this adds nice logging after tests to show stats
    addTestListener(object : TestListener {
        override fun beforeSuite(suite: TestDescriptor) {}
        override fun beforeTest(testDescriptor: TestDescriptor) {}
        override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {
            when (result.resultType) {
                TestResult.ResultType.FAILURE -> failedTests.add(testDescriptor)
                TestResult.ResultType.SKIPPED -> skippedTests.add(testDescriptor)
                else -> Unit
            }
        }

        override fun afterSuite(suite: TestDescriptor, result: TestResult) {
            if (suite.parent == null) { // root suite
                logger.lifecycle("----")
                logger.lifecycle("Test result: ${result.resultType}")
                logger.lifecycle(
                    "Test summary: ${result.testCount} tests, " +
                            "${result.successfulTestCount} succeeded, " +
                            "${result.failedTestCount} failed, " +
                            "${result.skippedTestCount} skipped")
                failedTests.takeIf { it.isNotEmpty() }?.prefixedSummary("\tFailed Tests")
                skippedTests.takeIf { it.isNotEmpty() }?.prefixedSummary("\tSkipped Tests:")
            }
        }

        private infix fun List<TestDescriptor>.prefixedSummary(subject: String) {
            logger.lifecycle(subject)
            forEach { test -> logger.lifecycle("\t\t${test.displayName()}") }
        }

        private fun TestDescriptor.displayName() = parent?.let { "${it.name} - $name" } ?: name
    })
}

// cleanTest task doesn't have an accessor on tasks (when this blog post was written)
tasks.named("cleanTest") { group = "verification" }