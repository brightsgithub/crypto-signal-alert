package com.owusu.cryptosignalalert.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.owusu.cryptosignalalert.CryptoSignalAlertApp
import com.owusu.cryptosignalalert.receivers.AlarmReceiver
import org.koin.core.KoinComponent
import java.util.*

/**
 * Created by Bright Owusu-Amankwaa on 24/01/21.
 */
object CryptoAlarmManager : KoinComponent {

    private lateinit var alarmIntent: PendingIntent
    private lateinit var context: CryptoSignalAlertApp

    val INTENT_ACTION_START_ALARM_LISTENER = "INTENT_ACTION_START_ALARM_LISTENER"
    val INTENT_ACTION_STOP_ALARM_LISTENER = "INTENT_ACTION_STOP_ALARM_LISTENER"
    private val START_ALARM_REQUEST_CODE = 412232
    private val fiveMins = 300000L
    private val thirtySeconds = 30000L
    private val oneMin = 60000L
    private val twoMin = 120000L
    private val ALARM_INTERVAL = twoMin
    private var hasInitBeenCalled: Boolean = false

    fun startAlarmFirstTime() {
        init()
        startAlarm()
    }

    private fun init() {
        if(!hasInitBeenCalled) {
            hasInitBeenCalled = true
            context = CryptoSignalAlertApp.instance
            Log.v("CryptoSignalService", "CryptoAlertAlarm.init called")
        } else {
            Log.v("CryptoSignalService", "CryptoAlertAlarm.init already called!")
        }
    }

    fun startAlarm() {
        try {
            // just in case we get an network error or something
            Log.v("CryptoSignalService", "CryptoAlertAlarm.starting alarm.....")
            val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val pendingIntent =
                PendingIntent.getBroadcast(context, START_ALARM_REQUEST_CODE, getAlarmIntent(),
                    PendingIntent.FLAG_CANCEL_CURRENT)

            alarmMgr.set(
                AlarmManager.RTC_WAKEUP,
                Calendar.getInstance().timeInMillis + ALARM_INTERVAL,
                pendingIntent
            )
        } catch (t: Throwable) {
            t.printStackTrace()
        }

        Log.v("CryptoSignalService","Next alarm scheduled at " + Date(Calendar.getInstance().timeInMillis + ALARM_INTERVAL).toString())
    }

    fun stopAlarm() {

        Log.v("CryptoSignalService", "CryptoAlertAlarm.stopAlarmListenerEvent called")
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                START_ALARM_REQUEST_CODE,
                getAlarmIntent(),
                PendingIntent.FLAG_NO_CREATE)

        if (pendingIntent != null && alarmManager != null) {
            Log.v("CryptoSignalService", "CryptoAlertAlarm.stopAlarmListenerEvent CANCEL ALARM")
            alarmManager.cancel(pendingIntent)
        }
    }

    /**
     * The below intent is used for starting the alarm and cancelling it.
     * NOTE. the exact same intent needs to be used when trying to cancel the alarm
     * i.e. even the ACTION needs to be the same or it will return a null Pending intent when trying to cancel
     * the alarm
     */
    private fun getAlarmIntent(): Intent {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.action = INTENT_ACTION_START_ALARM_LISTENER
        return intent
    }
}