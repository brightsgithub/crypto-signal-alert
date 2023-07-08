package com.owusu.cryptosignalalert.views.activities

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.rememberNavController
import com.owusu.cryptosignalalert.domain.models.ScreenProxy
import com.owusu.cryptosignalalert.domain.utils.CryptoDateUtils
import com.owusu.cryptosignalalert.navigation.NavigationItem
import com.owusu.cryptosignalalert.navigation.RootNavigationGraph
import com.owusu.cryptosignalalert.notification.NotificationUtil
import com.owusu.cryptosignalalert.views.theme.CryptoSignalAlertTheme
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

// The Compose application is designed to be used in a single-activity architecture with no fragments.
// https://stackoverflow.com/questions/68962458/how-are-android-activities-handled-with-jetpack-compose-and-compose-navigation
class MainActivity : ComponentActivity(), KoinComponent, ScreenProxy {

    private val notificationUtil: NotificationUtil by inject()
    private val cryptoDateUtils: CryptoDateUtils by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val preselectedScreen: MutableState<String?> = getPreSelectedScreen()
            CryptoSignalAlertTheme {
                val nav = rememberNavController()
                RootNavigationGraph(navHostController = nav, preselectedScreen = preselectedScreen)
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

    override fun onResume() {
        super.onResume()
        Handler().postDelayed ({
            msg()
        }, 3000)
    }

    private fun msg() {
        notificationUtil.sendNewStandAloneNotification("Last updated at "+ cryptoDateUtils.convertDateToFormattedStringWithTime(
            Calendar.getInstance().timeInMillis) + "\n" + "Hello testing")
    }

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
