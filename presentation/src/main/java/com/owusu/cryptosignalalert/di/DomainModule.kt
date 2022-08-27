package com.owusu.cryptosignalalert.di

import com.owusu.cryptosignalalert.domain.usecase.GetPriceTargetsUseCase
import com.owusu.cryptosignalalert.domain.usecase.GetCoinsListUseCase
import com.owusu.cryptosignalalert.domain.usecase.SyncForPriceTargetsUseCase
import org.koin.dsl.module

val domainModule = module(override = true) {

    factory {
        GetPriceTargetsUseCase(get())
    }

    factory {
        GetCoinsListUseCase(get())
    }

    factory {
        GetPriceTargetsUseCase(get())
    }

    single {
        SyncForPriceTargetsUseCase(get(), get(), get(), get())
    }
}
