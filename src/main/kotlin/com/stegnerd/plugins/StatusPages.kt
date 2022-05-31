package com.stegnerd.plugins

import com.stegnerd.statuspages.authStatusPages
import com.stegnerd.statuspages.genericStatusPages
import com.stegnerd.statuspages.userStatusPages
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages

fun Application.configureStatusPages() {
    install(StatusPages) {
        genericStatusPages()
        userStatusPages()
        authStatusPages()
    }
}