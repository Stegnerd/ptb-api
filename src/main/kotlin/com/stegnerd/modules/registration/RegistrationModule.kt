package com.stegnerd.modules.registration

import com.stegnerd.models.LoginUserRequest
import com.stegnerd.models.RefreshTokenRequest
import com.stegnerd.models.RegisterUserRequest
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.routing.route
import org.koin.ktor.ext.inject

fun Routing.registrationModule() {

    val controller by inject<RegistrationController>()

    route("/auth") {
        post("register") {
            val registerRequest = call.receive<RegisterUserRequest>()
            val userCreated = controller.register(registerRequest)
            call.respond(userCreated)
        }

        post("login") {
            val loginRequest = call.receive<LoginUserRequest>()
            val loginTokenResponse = controller.login(loginRequest)
            call.respond(loginTokenResponse)
        }

        post("token") {
            val refreshTokenRequest = call.receive<RefreshTokenRequest>()
            val credentialsResponse = controller.refreshToken(refreshTokenRequest)
            call.respond(credentialsResponse)
        }
    }
}