package com.stegnerd.plugins

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import org.slf4j.event.Level

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.DEBUG
    }
}
