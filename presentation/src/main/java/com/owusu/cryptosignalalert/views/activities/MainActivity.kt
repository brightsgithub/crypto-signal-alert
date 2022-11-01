package com.owusu.cryptosignalalert.views.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.owusu.cryptosignalalert.viewmodels.AlertListViewModel
import com.owusu.cryptosignalalert.views.helpers.PriceTargetsHelper
import com.owusu.cryptosignalalert.views.theme.CryptoSignalAlertTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

// The Compose application is designed to be used in a single-activity architecture with no fragments.
// https://stackoverflow.com/questions/68962458/how-are-android-activities-handled-with-jetpack-compose-and-compose-navigation
class MainActivity : ComponentActivity(), KoinComponent {

    private lateinit var priceTargetsHelper: PriceTargetsHelper
    private val alertListViewModel: AlertListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initScreenHelpers()
        registerViewStates()
        setContent {
            CryptoSignalAlertTheme {
                MyApp()
            }
        }
    }

    /**
     * So we do not bloat all screen info in this MainActivity, we delegate to keep things SRP
     */
    private fun initScreenHelpers() {
        priceTargetsHelper = PriceTargetsHelper()
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterViewStates()
    }

    private fun registerViewStates() {
        priceTargetsHelper.observeWorkManagerStatus(this, alertListViewModel)
    }

    private fun unRegisterViewStates() {
        priceTargetsHelper.stopObservingWorkManagerStatus()
    }
}

@Composable
private fun MyApp() {
    CoinsListScreen()
}