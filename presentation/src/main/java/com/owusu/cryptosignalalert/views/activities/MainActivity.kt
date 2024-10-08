package com.owusu.cryptosignalalert.views.activities

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.owusu.cryptosignalalert.domain.models.ScreenProxy
import com.owusu.cryptosignalalert.domain.utils.CryptoDateUtils
import com.owusu.cryptosignalalert.navigation.RootNavigationGraph
import com.owusu.cryptosignalalert.notification.NotificationUtil
import com.owusu.cryptosignalalert.views.theme.AppTheme
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

// The Compose application is designed to be used in a single-activity architecture with no fragments.
// https://stackoverflow.com/questions/68962458/how-are-android-activities-handled-with-jetpack-compose-and-compose-navigation
class MainActivity : ComponentActivity(), KoinComponent, ScreenProxy {

    private val notificationUtil: NotificationUtil by inject()
    private val cryptoDateUtils: CryptoDateUtils by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val adRequest = AdRequest.Builder().build()
        MobileAds.initialize(this)
        setContent {
            val preselectedScreen: MutableState<String?> = getPreSelectedScreen()
            AppTheme {
                /**
                 * https://developer.android.com/codelabs/jetpack-compose-theming#3
                 * In the home screen, you'll start by wrapping the main app composable in a Surface()
                 * to provide the base for the app's content to be placed on top of it. Open
                 * MainActivity.kt and wrap the RootNavigationGraph() composable with Surface.
                 * You'll also provide a tonal elevation of 5.dp to give the surface a tonal color
                 * of primary slot, which helps to provide contrast against the list item and the
                 * search bar on top of it. By default, the tonal and shadow elevation for the surface is 0.dp.
                 */
                //Surface(tonalElevation = 5.dp) {
                    val nav = rememberNavController()
                    RootNavigationGraph(
                        navHostController = nav,
                        preselectedScreen = preselectedScreen,
                        adRequest = adRequest
                    )
                //}
            }
        }
    }

    @Composable
    private fun getPreSelectedScreen(): MutableState<String?> {
        val screenToNavTo = intent?.getStringExtra("SCREEN_TO_NAV_TO")
        val preselectedScreen: MutableState<String?> = if (screenToNavTo != null) {
            rememberSaveable { mutableStateOf(screenToNavTo) }
        } else {
            rememberSaveable { mutableStateOf(null) }
        }
        return preselectedScreen
    }

//    override fun onResume() {
//        super.onResume()
//        Handler().postDelayed ({
//            msg()
//        }, 3000)
//    }
//
//    private fun msg() {
//        notificationUtil.sendNewStandAloneNotification("Last updated at "+ cryptoDateUtils.convertDateToFormattedStringWithTime(
//            Calendar.getInstance().timeInMillis) + "\n" + "Hello testing")
//    }

    companion object {

        val SCREEN_TO_NAV_TO = "SCREEN_TO_NAV_TO"
        fun getPendingIntent(context: Context, screen: String): PendingIntent {
            val notificationIntent = Intent(context, MainActivity::class.java)
            notificationIntent.putExtra(SCREEN_TO_NAV_TO, screen) // Add this line
            val pendingIntent = PendingIntent.getActivity(
                context,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
            )
            return pendingIntent
        }
    }
}
