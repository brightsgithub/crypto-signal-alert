package com.owusu.cryptosignalalert.di

import androidx.work.WorkManager
import com.owusu.cryptosignalalert.alarm.CryptoAlarmManager
import com.owusu.cryptosignalalert.alarm.CryptoMediaPlayer
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.notification.NotificationUtil
import com.owusu.cryptosignalalert.domain.utils.CryptoDateUtils
import com.owusu.cryptosignalalert.mappers.*
import com.owusu.cryptosignalalert.models.PriceTargetUI
import com.owusu.cryptosignalalert.resource.AppStringProvider
import com.owusu.cryptosignalalert.settings.ContactDeveloperHelper
import com.owusu.cryptosignalalert.settings.SettingsHelper
import com.owusu.cryptosignalalert.util.PriceDisplayUtils
import com.owusu.cryptosignalalert.util.PriceUtils
import com.owusu.cryptosignalalert.viewmodels.*
import com.owusu.cryptosignalalert.viewmodels.helpers.ToolBarHelper
import com.owusu.cryptosignalalert.viewmodels.SharedViewModel
import com.owusu.cryptosignalalert.workmanager.Constants
import com.owusu.cryptosignalalert.workmanager.PriceNotificationHelper
import com.owusu.cryptosignalalert.workmanager.WorkManagerStarter
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
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

    factory { SkuDetailsDomainToUIMapper() }

    factory { CoinDetailToUIMapper() }

    factory { CoinIdToUIMapper() }

    viewModel {
        val appContext = androidApplication()
        AlertListViewModel(
            priceTargetsMapper = get(named(PriceTargetUIMapper::class.java.name)),
            getPriceTargetsUseCase = get(),
            deletePriceTargetsUseCase = get(),
            isSyncRunningUseCase = get(),
            dispatcherBackground = get(named(IO)),
            dispatcherMain = get(named(MAIN)),
            workerTag = Constants.SYNC_PRICES_WORKER_TAG,
            app = appContext,
            WorkManager.getInstance(appContext),
            listenToSyncPriceTargetsUpdatesUseCase = get()
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
            saveNewPriceTargetsWithLimitUseCase = get(),
            getCoinDetailUseCase = get(),
            coinDetailToUIMapper = get(),
            dispatcherBackground = get(named(IO)),
            dispatcherMain = get(named(MAIN))
        )
    }

    viewModel {
        SharedViewModel(
            startupBillingUseCase = get(),
            populateCoinIdsUseCase = get(),
            savedPurchasedStateChangesUseCase = get(),
            refreshSkuDetailsUseCase = get(),
            appStringProvider = get(),
            toolBarHelper = get()
        )
    }

    viewModel {
        PurchaseViewModel(
            getSkuDetailsUseCase = get(),
            refreshSkuDetailsUseCase = get(),
            buySkyUseCase = get(),
            skuDetailsDomainToUIMapper = get(),
            dispatcherBackground = get(named(IO)),
            dispatcherMain = get(named(MAIN))
        )
    }

    viewModel {
        CoinSearchViewModel(
            searchCoinIdsUseCase = get(),
            getCoinsListUseCase = get(),
            coinDomainToUIMapper = get(),
            coinIdToUIMapper = get(),
            dispatcherBackground = get(named(IO)),
            dispatcherMain = get(named(MAIN))
        )
    }

    viewModel {
        SettingsViewModel (
            loadSettingsUseCase = get(),
            settingDomainToUIMapper = get(),
            dispatcherBackground = get(named(IO)),
            dispatcherMain = get(named(MAIN)),
            isSirenEnabledUseCase = get(),
            enableSirenSettingUseCase = get(),
            disableSirenSettingUseCase = get()
        )
    }

    factory { SettingDomainToUIMapper }

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
        CryptoMediaPlayer(isSirenEnabledUseCase = get())
    }

    single {
        SettingsHelper()
    }

    single {
        ContactDeveloperHelper(settingsHelper = get())
    }

    factory {
        AppStringProvider(resources = androidContext().resources)
    }

    factory {
        ToolBarHelper(appStringProvider = get())
    }
}