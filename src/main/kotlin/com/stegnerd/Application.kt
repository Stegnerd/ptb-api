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
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.dsl.module
import org.koin.ktor.ext.Koin


fun main(args: Array<String>) {

    val config = extractConfig(HoconApplicationConfig(ConfigFactory.load()))

    embeddedServer(Netty, port = 8080) {
        module {
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
            main()
        }
    }.start(wait = true)

}

fun Application.main() {
    configurePlugins()
}

fun extractConfig(hoconConfig: HoconApplicationConfig): Config {
    return Config(
        hoconConfig.property("db.jdbcUrl").getString(),
        hoconConfig.property("db.dbUser").getString(),
        hoconConfig.property("db.dbPassword").getString(),
        hoconConfig.property("jwt.secret").getString(),
        hoconConfig.property("jwt.issuer").getString(),
        hoconConfig.property("jwt.audience").getString(),
        hoconConfig.property("jwt.realm").getString()
    )
}