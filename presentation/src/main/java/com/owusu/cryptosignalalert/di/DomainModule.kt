package com.owusu.cryptosignalalert.di

import com.owusu.cryptosignalalert.domain.usecase.*
import org.koin.dsl.module

val domainModule = module(override = true) {

    factory {
        GetPriceTargetsUseCase(get())
    }

    factory {
        GetCoinsListUseCase(get())
    }

    factory {
        UpdatePriceTargetsUseCase(get())
    }

    factory {
        GetPriceTargetsToAlertUserUseCase(get())
    }


    single {
        SyncForPriceTargetsUseCase(get(), get(), get(), get())
    }
}
