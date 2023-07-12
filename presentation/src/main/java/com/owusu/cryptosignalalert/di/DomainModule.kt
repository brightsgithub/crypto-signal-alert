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
        SaveNewPriceTargetsWithLimitUseCase(
            priceTargetsRepository = get(),
            saveNewPriceTargetsUseCase = get(),
            appPreferencesRepository = get()
        )
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
        SyncForPriceTargetsUseCase(get(), get(), get(), get(), get(),
            priceTargetsRepository = get(),
        calculateRemainingWorkUseCase = get())
    }

    factory {
        GetSkuDetailsUseCase(billingRepository = get(), refreshSkuDetailsUseCase = get())
    }

    factory {
        RefreshSkuDetailsUseCase(billingRepository = get())
    }

    factory {
        StartupBillingUseCase(
            billingRepository = get(),
            refreshSkuDetailsUseCase = get(),
            appPreferences = get()
        )
    }

    factory {
        BuySkyUseCase(billingRepository = get())
    }

    factory { PopulateCoinIdsUseCase(coinsRepository = get()) }

    factory { GetCoinDetailUseCase(coinsRepository = get()) }

    factory { SearchCoinIdsUseCase(coinsRepository = get()) }

    factory { GetHistoricalPriceUseCase(coinsRepository = get()) }

    factory { IsSyncRunningUseCase(appPreferencesRepository = get()) }

    factory { SyncHasStartedStatusUseCase(appPreferencesRepository = get()) }

    factory { SyncHasFinishedStatusUseCase(appPreferencesRepository = get()) }

    factory { LoadSettingsUseCase(settingsRepository = get()) }

    factory { ListenToSyncPriceTargetsUpdatesUseCase(priceTargetsRepository = get()) }

    factory { CalculateRemainingWorkUseCase() }

    factory { SavedPurchasedStateChangesUseCase(appPreferencesRepository = get()) }
}
