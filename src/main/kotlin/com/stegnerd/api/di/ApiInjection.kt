package com.stegnerd.api.di

import com.stegnerd.api.user.UserApi
import com.stegnerd.api.user.UserApiImpl
import org.koin.dsl.module

object ApiInjection {
    val module = module {
        single<UserApi> { UserApiImpl }
    }
}