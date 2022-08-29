package com.owusu.cryptosignalalert.di

import com.owusu.cryptosignalalert.domain.usecase.GetPriceTargetsUseCase
import com.owusu.cryptosignalalert.domain.usecase.GetCoinsListUseCase
import com.owusu.cryptosignalalert.domain.usecase.SyncForPriceTargetsUseCase
import com.owusu.cryptosignalalert.domain.usecase.UpdatePriceTargetsUseCase
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


    single {
        SyncForPriceTargetsUseCase(get(), get(), get(), get())
    }
}
