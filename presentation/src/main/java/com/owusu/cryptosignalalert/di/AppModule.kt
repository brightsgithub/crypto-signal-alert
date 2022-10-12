package com.owusu.cryptosignalalert.di

import com.owusu.cryptosignalalert.CryptoSignalAlertApp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val IO = "IO"
const val MAIN = "MAIN"

val appModule = module() {

    single { androidContext() as CryptoSignalAlertApp }

    factory<CoroutineDispatcher>(named(IO)) {
        Dispatchers.IO
    }

    factory<CoroutineDispatcher>(named(MAIN)) {
        Dispatchers.Main
    }
}