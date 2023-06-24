package com.owusu.cryptosignalalert.views.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.owusu.cryptosignalalert.domain.models.ScreenProxy
import com.owusu.cryptosignalalert.navigation.RootNavigationGraph
import com.owusu.cryptosignalalert.views.theme.CryptoSignalAlertTheme
import org.koin.core.component.KoinComponent

// The Compose application is designed to be used in a single-activity architecture with no fragments.
// https://stackoverflow.com/questions/68962458/how-are-android-activities-handled-with-jetpack-compose-and-compose-navigation
class MainActivity : ComponentActivity(), KoinComponent, ScreenProxy {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            CryptoSignalAlertTheme {
                RootNavigationGraph(navHostController = rememberNavController())
            }
        }
    }
}