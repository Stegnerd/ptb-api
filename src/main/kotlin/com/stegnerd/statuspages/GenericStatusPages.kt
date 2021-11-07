package com.stegnerd.statuspages

import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText

fun StatusPages.Configuration.genericStatusPages() {
    exception<UnknownError> {
        call.respondText("Internal Server Error", ContentType.Text.Plain, status = HttpStatusCode.InternalServerError)
    }
}