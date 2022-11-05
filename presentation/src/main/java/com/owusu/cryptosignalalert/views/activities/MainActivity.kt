package com.owusu.cryptosignalalert.views.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
//import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.navigation.NavigationItem
import com.owusu.cryptosignalalert.navigation.RootNavigationGraph
import com.owusu.cryptosignalalert.navigation.Routes
import com.owusu.cryptosignalalert.viewmodels.AlertListViewModel
import com.owusu.cryptosignalalert.viewmodels.CoinsListViewModel
import com.owusu.cryptosignalalert.viewmodels.SharedViewModel
import com.owusu.cryptosignalalert.views.helpers.PriceTargetsHelper
import com.owusu.cryptosignalalert.views.screens.CoinsListScreen
import com.owusu.cryptosignalalert.views.screens.PriceTargetsScreen
import com.owusu.cryptosignalalert.views.theme.CryptoSignalAlertTheme
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

// The Compose application is designed to be used in a single-activity architecture with no fragments.
// https://stackoverflow.com/questions/68962458/how-are-android-activities-handled-with-jetpack-compose-and-compose-navigation
class MainActivity : ComponentActivity(), KoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoSignalAlertTheme {
                RootNavigationGraph(navHostController = rememberNavController())
            }
        }
    }
}