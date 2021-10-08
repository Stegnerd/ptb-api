package com.stegnerd.modules.user

import com.stegnerd.models.UpdateUserRequest
import com.stegnerd.models.toResponseUser
import com.stegnerd.user
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.routing.route
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
            call.respond(user.toResponseUser())
        }

        delete {
            controller.deleteAccount(call.user.id)
            call.respond(HttpStatusCode.OK)
        }
    }
}