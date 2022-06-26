package com.owusu.cryptosignalalert.di

import com.owusu.cryptosignalalert.domain.usecase.GetAlertListUseCase
import org.koin.dsl.module

val domainModule = module(override = true) {

    factory {
        GetAlertListUseCase(get())
    }
}
