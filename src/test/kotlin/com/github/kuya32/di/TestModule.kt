package com.github.kuya32.di

import com.github.kuya32.repository.user.FakeUserRepository
import org.koin.dsl.module

internal val testModule = module {
    single {
        FakeUserRepository()
    }
}