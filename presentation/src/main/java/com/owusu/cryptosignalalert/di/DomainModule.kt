package com.owusu.cryptosignalalert.di

import com.owusu.cryptosignalalert.domain.usecase.MergeOldPriceTargetWithNewDataUseCase
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
        UpdatePriceTargetsForAlertedUserUseCase(get())
    }

    factory {
        GetPriceTargetsToAlertUserUseCase(get())
    }

    factory {
        SaveNewPriceTargetsUseCase(get())
    }

    factory {
        DeletePriceTargetsUseCase(get())
    }

    factory {
        DeleteAllPriceTargetsUseCase(get())
    }

    factory {
        MergeOldPriceTargetWithNewDataUseCase()
    }

    single {
        SyncForPriceTargetsUseCase(get(), get(), get(), get(), get())
    }
}
