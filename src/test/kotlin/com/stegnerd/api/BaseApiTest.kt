package com.stegnerd.api

import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module

abstract class BaseApiTest {

    init {
        stopKoin()
    }

    fun startInjection(module: Module) {
        startKoin {
            modules(module)
        }
    }
}