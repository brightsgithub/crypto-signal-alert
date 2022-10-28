package com.owusu.cryptosignalalert.di

import com.owusu.cryptosignalalert.alarm.CryptoAlarmManager
import com.owusu.cryptosignalalert.alarm.CryptoMediaPlayer
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.usecase.SaveNewPriceTargetsUseCase
import com.owusu.cryptosignalalert.notification.NotificationUtil
import com.owusu.cryptosignalalert.domain.utils.CryptoDateUtils
import com.owusu.cryptosignalalert.mappers.CoinDomainToUIMapper
import com.owusu.cryptosignalalert.mappers.CoinUIToPriceTargetDomainMapper
import com.owusu.cryptosignalalert.mappers.PriceTargetUIMapper
import com.owusu.cryptosignalalert.mappers.UIMapper
import com.owusu.cryptosignalalert.models.PriceTargetUI
import com.owusu.cryptosignalalert.util.PriceDisplayUtils
import com.owusu.cryptosignalalert.util.PriceUtils
import com.owusu.cryptosignalalert.viewmodels.AlertListViewModel
import com.owusu.cryptosignalalert.viewmodels.CoinsListViewModel
import com.owusu.cryptosignalalert.viewmodels.PriceTargetEntryViewModel
import com.owusu.cryptosignalalert.workmanager.Constants
import com.owusu.cryptosignalalert.workmanager.PriceNotificationHelper
import com.owusu.cryptosignalalert.workmanager.WorkManagerStarter
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val uiModule = module() {

    factory<UIMapper<PriceTargetDomain, PriceTargetUI>>(named(PriceTargetUIMapper::class.java.name)) {
        PriceTargetUIMapper(priceDisplayUtils = get())
    }

    factory {
        CoinDomainToUIMapper(priceDisplayUtils = get())
    }

    factory {
        PriceDisplayUtils(priceUtils = get())
    }

    factory {
        PriceUtils()
    }

    factory {
        PriceNotificationHelper(get(), get(), get())
    }

    factory {
        CoinUIToPriceTargetDomainMapper(dateUtils = get())
    }

    viewModel {
        AlertListViewModel(
            priceTargetsMapper = get(named(PriceTargetUIMapper::class.java.name)),
            getPriceTargetsUseCase = get(),
            deletePriceTargetsUseCase = get(),
            dispatcherBackground = get(named(IO)),
            dispatcherMain = get(named(MAIN)),
            workerTag = Constants.SYNC_PRICES_WORKER_TAG,
            app = androidApplication()
        )
    }

    viewModel {
        CoinsListViewModel(
            coinDomainToUIMapper = get(),
            getCoinsListUseCase = get(),
            getPriceTargetsThatHaveNotBeenHitUseCase = get(),
            priceDisplayUtils = get(),
            dispatcherBackground = get(named(IO)),
            dispatcherMain = get(named(MAIN))
        )
    }

    viewModel {
        PriceTargetEntryViewModel(
            coinUIToPriceTargetDomainMapper = get(),
            saveNewPriceTargetsUseCase = get(),
            dispatcherBackground = get(named(IO)),
            dispatcherMain = get(named(MAIN))
        )
    }

    single {
        CryptoDateUtils.getInstance()
    }

    single {
        NotificationUtil()
    }

    single {
       CryptoAlarmManager(get(named(IO)), get(named(MAIN)))
    }

    single {
        WorkManagerStarter
    }

    single {
        CryptoMediaPlayer()
    }
}