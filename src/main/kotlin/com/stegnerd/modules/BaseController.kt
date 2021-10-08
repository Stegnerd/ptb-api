package com.stegnerd.modules

import com.stegnerd.database.DatabaseProviderContract
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class BaseController : KoinComponent {

    private val dbProvider by inject<DatabaseProviderContract>()

    suspend fun <T> dbQuery(block: () -> T): T {
        return dbProvider.dbQuery(block)
    }
}