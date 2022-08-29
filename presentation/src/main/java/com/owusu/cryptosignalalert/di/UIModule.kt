package com.owusu.cryptosignalalert.di

import com.owusu.cryptosignalalert.alarm.CryptoAlarmManager
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.notification.NotificationUtil
import com.owusu.cryptosignalalert.domain.utils.CryptoDateUtils
import com.owusu.cryptosignalalert.mappers.PriceTargetUIMapper
import com.owusu.cryptosignalalert.mappers.UIListMapper
import com.owusu.cryptosignalalert.models.PriceTargetUI
import com.owusu.cryptosignalalert.viewmodels.AlertListViewModel
import com.owusu.cryptosignalalert.workmanager.PriceNotificationHelper
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val uiModule = module(override = true) {

    factory<UIListMapper<PriceTargetDomain, PriceTargetUI>> {
        PriceTargetUIMapper()
    }

    factory {
        PriceNotificationHelper(get(), get())
    }

    viewModel {
        AlertListViewModel(get(), get(), get(named(IO)), get(named(MAIN)), androidApplication())
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
}