package com.stegnerd.plugins

import com.stegnerd.statuspages.authStatusPages
import com.stegnerd.statuspages.genericStatusPages
import com.stegnerd.statuspages.userStatusPages
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.StatusPages

fun Application.configureStatusPages() {
    install(StatusPages) {
        genericStatusPages()
        userStatusPages()
        authStatusPages()
    }
}