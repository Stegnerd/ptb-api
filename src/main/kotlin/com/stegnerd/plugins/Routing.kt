package com.stegnerd.plugins

import com.stegnerd.modules.registration.registrationModule
import com.stegnerd.modules.user.userModule
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.authenticate
import io.ktor.routing.Routing

fun Application.configureRouting() {
    install(Routing) {
        registrationModule()
        authenticate("jwt") {
            userModule()
        }
    }
}
