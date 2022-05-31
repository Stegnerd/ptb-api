
object Versions {
    const val assertJ = "3.21.0"
    const val exposed = "0.35.1"
    const val flyway = "7.15.0"
    const val h2 = "1.4.200"
    const val hikariCP = "5.0.0"
    const val jbcrypt = "0.4"
    const val junit = "5.8.1"
    const val koin = "3.2.0"
    const val kotlin = "1.5.31"
    const val ktor = "2.0.2"
    const val logback = "1.2.3"
    const val mockk = "1.12.0"
    const val postgresql = "42.2.24"
    const val shadow = "7.0.0"
}

object Deps {
    const val jbcrypt = "org.mindrot:jbcrypt:${Versions.jbcrypt}"
    const val koin = "io.insert-koin:koin-ktor:${Versions.koin}"
    const val logback = "ch.qos.logback:logback-classic:${Versions.logback}"

    object Database {
        // sql orm
        const val exposedCore = "org.jetbrains.exposed:exposed-core:${Versions.exposed}"
        const val exposedDao = "org.jetbrains.exposed:exposed-dao:${Versions.exposed}"
        const val exposedJavaTime = "org.jetbrains.exposed:exposed-java-time:${Versions.exposed}"
        const val exposedJDBC = "org.jetbrains.exposed:exposed-jdbc:${Versions.exposed}"
        // db migrations
        const val flyway = "org.flywaydb:flyway-core:${Versions.flyway}"
        // in mem db for unit tests
        const val h2 = "com.h2database:h2:${Versions.h2}"
        // db configuration
        const val hikariCP = "com.zaxxer:HikariCP:${Versions.hikariCP}"
        // sql driver
        const val postgresql = "org.postgresql:postgresql:${Versions.postgresql}"
    }

    object Ktor {
        // auth
        const val auth = "io.ktor:ktor-server-auth:${Versions.ktor}"
        const val authJwt = "io.ktor:ktor-server-auth-jwt:${Versions.ktor}"
        const val tls = "io.ktor:ktor-network-tls-certificates:${Versions.ktor}"
        // content negotiation
        const val contentNegotiation = "io.ktor:ktor-server-content-negotiation:${Versions.ktor}"
        const val serialization = "io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}"
        // core
        const val serverCore = "io.ktor:ktor-server:${Versions.ktor}"
        const val serverNetty = "io.ktor:ktor-server-netty:${Versions.ktor}"
        // forwarded headers
        const val forwardedHeader = "io.ktor:ktor-server-forwarded-header:${Versions.ktor}"

    }

    object Tests {
        const val assertJ = "org.assertj:assertj-core:${Versions.assertJ}"
        const val mockk = "io.mockk:mockk:${Versions.mockk}"

        object Junit {
            const val jupiter = "org.junit.jupiter:junit-jupiter:${Versions.junit}"
            const val jupiterEngine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit}"
        }

        object Kotlin {
            const val test = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"
        }

        object Ktor {
            const val serverTests = "io.ktor:ktor-server-tests:${Versions.ktor}"
        }
    }
}