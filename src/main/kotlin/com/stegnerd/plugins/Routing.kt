package com.stegnerd.plugins

import com.stegnerd.modules.registration.registrationModule
import com.stegnerd.modules.user.userModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Routing

fun Application.configureRouting() {
    install(Routing) {
        registrationModule()
        authenticate("jwt") {
            userModule()
        }
    }
}
