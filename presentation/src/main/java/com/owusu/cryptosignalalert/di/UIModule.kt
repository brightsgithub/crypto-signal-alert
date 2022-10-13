package com.owusu.cryptosignalalert.di

import com.owusu.cryptosignalalert.alarm.CryptoAlarmManager
import com.owusu.cryptosignalalert.alarm.CryptoMediaPlayer
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.notification.NotificationUtil
import com.owusu.cryptosignalalert.domain.utils.CryptoDateUtils
import com.owusu.cryptosignalalert.mappers.CoinDomainToUIMapper
import com.owusu.cryptosignalalert.mappers.PriceTargetUIMapper
import com.owusu.cryptosignalalert.mappers.UIMapper
import com.owusu.cryptosignalalert.models.PriceTargetUI
import com.owusu.cryptosignalalert.viewmodels.AlertListViewModel
import com.owusu.cryptosignalalert.viewmodels.CoinsListViewModel
import com.owusu.cryptosignalalert.workmanager.Constants
import com.owusu.cryptosignalalert.workmanager.PriceNotificationHelper
import com.owusu.cryptosignalalert.workmanager.WorkManagerStarter
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val uiModule = module() {

    factory<UIMapper<PriceTargetDomain, PriceTargetUI>>(named(PriceTargetUIMapper::class.java.name)) {
        PriceTargetUIMapper()
    }

    factory {
        CoinDomainToUIMapper()
    }

    factory {
        PriceNotificationHelper(get(), get(), get())
    }

    viewModel {
        AlertListViewModel(
            priceTargetsMapper = get(named(PriceTargetUIMapper::class.java.name)),
            getPriceTargetsUseCase = get(),
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