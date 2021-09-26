
object Versions {
    const val exposed = "0.35.1"
    const val hikariCP = "5.0.0"
    const val kotlin = "1.5.31"
    const val ktor = "1.6.3"
    const val logback = "1.2.3"
    const val postgresql = "42.2.24"
}

object Deps {

    const val logback = "ch.qos.logback:logback-classic:${Versions.logback}"

    object Database {
        const val exposedCore = "org.jetbrains.exposed:exposed-core:${Versions.exposed}"
        const val exposedDao = "org.jetbrains.exposed:exposed-dao:${Versions.exposed}"
        const val exposedJDBC = "org.jetbrains.exposed:exposed-jdbc:${Versions.exposed}"
        const val hikariCP = "com.zaxxer:HikariCP:${Versions.hikariCP}"
        const val postgresql = "org.postgresql:postgresql:${Versions.postgresql}"
    }

    object Ktor {
        const val auth = "io.ktor:ktor-auth:${Versions.ktor}"
        const val authJwt = "io.ktor:ktor-auth-jwt:${Versions.ktor}"
        const val serialization = "io.ktor:ktor-serialization:${Versions.ktor}"
        const val serverCore = "io.ktor:ktor-server-core:${Versions.ktor}"
        const val serverNetty = "io.ktor:ktor-server-netty:${Versions.ktor}"
    }

    object Tests {
        object Kotlin {
            const val test = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"
        }

        object Ktor {
            const val serverTests = "io.ktor:ktor-server-tests:${Versions.ktor}"
        }
    }
}