package com.stegnerd

import com.auth0.jwt.interfaces.JWTVerifier
import com.stegnerd.api.di.ApiInjection
import com.stegnerd.config.Config
import com.stegnerd.database.DatabaseProvider
import com.stegnerd.database.DatabaseProviderContract
import com.stegnerd.database.di.DaoInjection
import com.stegnerd.modules.auth.JwtConfig
import com.stegnerd.modules.auth.TokenProvider
import com.stegnerd.modules.di.ModulesInjection
import com.stegnerd.utils.PasswordWrapper
import com.stegnerd.utils.PasswordWrapperContract
import com.typesafe.config.ConfigFactory
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.config.HoconApplicationConfig
import io.ktor.network.tls.certificates.generateCertificate
import io.ktor.server.engine.ApplicationEngineEnvironment
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.engine.sslConnector
import io.ktor.server.netty.Netty
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import java.io.File


fun main(args: Array<String>) {
    val config = extractConfig(HoconApplicationConfig(ConfigFactory.load()))
    val environment = setupEnvironment(config)

    embeddedServer(Netty, environment).start(wait = true)
}

// sets up modules for the application
// if you put properties in this method function you can run into runtime errors in heroku
fun Application.module() {
    val config = extractConfig(HoconApplicationConfig(ConfigFactory.load()))
    install(Koin) {
        modules(
            module {
                single { config }
                single<DatabaseProviderContract> { DatabaseProvider() }
                single<JWTVerifier> { JwtConfig.verifier }
                single<PasswordWrapperContract> { PasswordWrapper }
                single<TokenProvider> { JwtConfig }
            },
            ApiInjection.module,
            ModulesInjection.modules,
            DaoInjection.module
        )
    }
    configurePlugins()
}

fun extractConfig(hoconConfig: HoconApplicationConfig): Config {
    return Config(
        hoconConfig.property("ktor.development").getString().toBoolean(),
        hoconConfig.property("db.jdbcUrl").getString(),
        hoconConfig.property("db.dbUser").getString(),
        hoconConfig.property("db.dbPassword").getString(),
        hoconConfig.property("jwt.secret").getString(),
        hoconConfig.property("jwt.issuer").getString(),
        hoconConfig.property("jwt.audience").getString(),
        hoconConfig.property("jwt.realm").getString()
    )
}

// sets up environment for application engine
fun setupEnvironment(config: Config): ApplicationEngineEnvironment {
    val environment = applicationEngineEnvironment {
        connector {
            port = 8080
        }
        module { module() }
        // only set up self-signed cert for local dev
        if(config.isDevelopment){
            val keyStoreFile = File("build/keystore.jks")
            val keystore = generateCertificate(
                file = keyStoreFile,
                keyAlias = "sampleAlias",
                keyPassword = "foobar",
                jksPassword = "foobar"
            )
            sslConnector(
                keyStore = keystore,
                keyAlias = "sampleAlias",
                keyStorePassword = { "foobar".toCharArray() },
                privateKeyPassword = { "foobar".toCharArray() }) {
                port = 8443
                keyStorePath = keyStoreFile
            }
        }
    }

    return environment
}
