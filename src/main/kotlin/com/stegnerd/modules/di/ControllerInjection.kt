package com.stegnerd.modules.di

import com.stegnerd.modules.registration.RegistrationController
import com.stegnerd.modules.registration.RegistrationControllerImpl
import com.stegnerd.modules.user.UserController
import com.stegnerd.modules.user.UserControllerImpl
import org.koin.dsl.module

object ModulesInjection {
    val modules = module {
        single<RegistrationController> { RegistrationControllerImpl() }
        single<UserController> { UserControllerImpl() }
    }
}