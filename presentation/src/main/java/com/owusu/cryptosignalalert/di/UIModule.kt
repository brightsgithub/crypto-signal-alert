package com.owusu.cryptosignalalert.di

import com.owusu.cryptosignalalert.alarm.CryptoAlarmManager
import com.owusu.cryptosignalalert.domain.models.PriceTargetsWrapper
import com.owusu.cryptosignalalert.mappers.AlertListUIMapper
import com.owusu.cryptosignalalert.mappers.UIMapper
import com.owusu.cryptosignalalert.models.AlertListUIWrapper
import com.owusu.cryptosignalalert.notification.NotificationUtil
import com.owusu.cryptosignalalert.domain.utils.DateUtils
import com.owusu.cryptosignalalert.viewmodels.AlertListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val uiModule = module(override = true) {

    factory<UIMapper<PriceTargetsWrapper, AlertListUIWrapper>> {
        AlertListUIMapper()
    }

    viewModel {
        AlertListViewModel(get(), get(), get(named(IO)), get(named(MAIN)))
    }

    single {
        DateUtils.getInstance()
    }

    single {
        NotificationUtil()
    }

    single {
       CryptoAlarmManager(get(named(IO)), get(named(MAIN)))
    }
}