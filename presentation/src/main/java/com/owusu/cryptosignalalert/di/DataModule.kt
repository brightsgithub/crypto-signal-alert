package com.owusu.cryptosignalalert.di

import com.owusu.cryptosignalalert.data.repository.AlertListRepositoryImpl
import com.owusu.cryptosignalalert.domain.repository.AlertListRepository
import org.koin.core.definition.Kind
import org.koin.dsl.module

val dataModule = module(override = true) {

    single<AlertListRepository> {
        AlertListRepositoryImpl()
    }
}