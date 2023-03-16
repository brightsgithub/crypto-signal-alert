package com.owusu.cryptosignalalert.di

import com.owusu.cryptosignalalert.domain.usecase.MergeOldPriceTargetWithNewDataUseCase
import com.owusu.cryptosignalalert.domain.usecase.*
import org.koin.dsl.module

val domainModule = module() {

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
        GetPriceTargetsThatHaveNotBeenHitUseCase(get())
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
        MergeOldPriceTargetWithNewDataUseCase(
            getHistoricalPriceUseCase = get()
        )
    }

    single {
        SyncForPriceTargetsUseCase(get(), get(), get(), get(), get())
    }

    factory {
        GetSkuDetailsUseCase(billingRepository = get(), refreshSkuDetailsUseCase = get())
    }

    factory {
        RefreshSkuDetailsUseCase(billingRepository = get())
    }

    factory {
        StartupBillingUseCase(billingRepository = get(), refreshSkuDetailsUseCase = get())
    }

    factory {
        BuySkyUseCase(billingRepository = get())
    }

    factory { PopulateCoinIdsUseCase(coinsRepository = get()) }

    factory { GetCoinDetailUseCase(coinsRepository = get()) }

    factory { SearchCoinIdsUseCase(coinsRepository = get()) }

    factory { GetHistoricalPriceUseCase(coinsRepository = get()) }
}
