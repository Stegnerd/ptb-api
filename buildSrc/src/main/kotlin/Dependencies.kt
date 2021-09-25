

object Versions {
    const val ktor = "1.6.3"
    const val kotlin = "1.5.31"
    const val logback = "1.2.3"
}

object Deps {

    const val logback = "ch.qos.logback:logback-classic:${Versions.logback}"

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