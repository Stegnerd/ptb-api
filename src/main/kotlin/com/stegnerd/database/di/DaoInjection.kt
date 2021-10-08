package com.stegnerd.database.di

import com.stegnerd.database.dao.UserDao
import com.stegnerd.database.dao.Users
import org.koin.dsl.module

object DaoInjection {
    val module = module {
        single<UserDao> {Users}
    }
}