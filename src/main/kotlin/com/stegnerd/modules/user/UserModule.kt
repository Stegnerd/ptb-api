package com.stegnerd.modules.user

import com.stegnerd.models.UpdateUserRequest
import com.stegnerd.models.toResponseUser
import com.stegnerd.user
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.userModule() {

    val controller by inject<UserController>()

    route("/user") {
        get("account") {
            call.respond(call.user.toResponseUser())
        }

        put{
            val updateUserRequest = call.receive<UpdateUserRequest>()
            val user = controller.updateAccount(call.user.id, updateUserRequest)
            call.respond(user)
        }

        delete {
            controller.deleteAccount(call.user.id)
            call.respond(HttpStatusCode.OK)
        }
    }
}