package com.stegnerd.plugins

import com.stegnerd.DatabaseFactory
import io.ktor.application.Application

fun Application.configureDatabase() {
    DatabaseFactory.init()
}