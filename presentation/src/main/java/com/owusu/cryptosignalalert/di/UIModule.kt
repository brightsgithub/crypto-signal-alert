package com.owusu.cryptosignalalert.di

import com.owusu.cryptosignalalert.domain.models.AlertListDomainWrapper
import com.owusu.cryptosignalalert.mappers.AlertListUIMapper
import com.owusu.cryptosignalalert.mappers.UIMapper
import com.owusu.cryptosignalalert.models.AlertListUIWrapper
import com.owusu.cryptosignalalert.viewmodels.AlertListViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val uiModule = module(override = true) {

    factory<UIMapper<AlertListDomainWrapper, AlertListUIWrapper>> {
        AlertListUIMapper()
    }

    viewModel {
        AlertListViewModel(get(), get(), get(named(IO)), get(named(MAIN)))
    }
}