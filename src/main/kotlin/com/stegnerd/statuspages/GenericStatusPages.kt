package com.stegnerd.statuspages

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.statuspages.StatusPagesConfig
import io.ktor.server.response.respondText

fun StatusPagesConfig.genericStatusPages() {
    exception<UnknownError> { call, _ ->
        call.respondText("Internal Server Error", ContentType.Text.Plain, status = HttpStatusCode.InternalServerError)
    }
}