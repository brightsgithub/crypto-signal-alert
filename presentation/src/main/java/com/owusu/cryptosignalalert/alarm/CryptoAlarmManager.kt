package com.owusu.cryptosignalalert.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.owusu.cryptosignalalert.CryptoSignalAlertApp
import com.owusu.cryptosignalalert.notification.NotificationUtil
import com.owusu.cryptosignalalert.receivers.AlarmReceiver
import com.owusu.cryptosignalalert.utils.DateUtils
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

/**
 * Created by Bright Owusu-Amankwaa on 24/01/21.
 */
class CryptoAlarmManager(
    private val dispatcherBackground: CoroutineDispatcher,
    private val dispatcherMain: CoroutineDispatcher
) : KoinComponent {

    companion object {
        val INTENT_ACTION_START_ALARM_LISTENER = "INTENT_ACTION_START_ALARM_LISTENER"
    }

    private lateinit var context: CryptoSignalAlertApp
    private val dateUtils: DateUtils by inject()
    private val notificationUtil: NotificationUtil by inject()
    private val START_ALARM_REQUEST_CODE = 412232
    private val fiveMins = 300000L
    private val thirtySeconds = 30000L
    private val oneMin = 60000L
    private val twoMin = 120000L
    private val thirtyMins = oneMin * 30
    private val fifteenMins = oneMin * 15
    private val ALARM_INTERVAL = thirtySeconds
    private var hasInitBeenCalled: Boolean = false
    private var backgroundJob: Job? = null

    fun startAlarmFirstTime() {
        init()
        startAlarm(1000)
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

    fun startAlarm(interval: Long = ALARM_INTERVAL) {
        try {
            cancelOngoingJob()
            // just in case we get an network error or something
            Log.v("CryptoSignalService", "CryptoAlertAlarm.starting alarm.....")
            val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            var pendingIntent: PendingIntent? = null
            pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getBroadcast(context, START_ALARM_REQUEST_CODE, getAlarmIntent(), PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT)
            } else {
                PendingIntent.getBroadcast(context, START_ALARM_REQUEST_CODE, getAlarmIntent(), PendingIntent.FLAG_CANCEL_CURRENT)
            }

            alarmMgr.set(
                AlarmManager.RTC_WAKEUP,
                Calendar.getInstance().timeInMillis + interval,
                pendingIntent
            )
        } catch (t: Throwable) {
            t.printStackTrace()
        }

        notificationUtil.updateServiceNotification("Last updated at "+ dateUtils.convertDateToFormattedStringWithTime(Calendar.getInstance().timeInMillis))
        Log.v("CryptoSignalService","Next alarm scheduled at " + Date(Calendar.getInstance().timeInMillis + interval).toString())
    }

    fun stopAlarm() {
        cancelOngoingJob()
        Log.v("CryptoSignalService", "CryptoAlertAlarm.stopAlarmListenerEvent called")
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

        var pendingIntent: PendingIntent? = null
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(context, START_ALARM_REQUEST_CODE, getAlarmIntent(), PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(context, START_ALARM_REQUEST_CODE, getAlarmIntent(), PendingIntent.FLAG_NO_CREATE)
        }


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

    fun onReadyToStartBackgroundWork() {
        cancelOngoingJob()
        backgroundJob = GlobalScope.launch(dispatcherBackground) {

//            Log.v("CryptoSignalService", "start sleep")
//            Thread.sleep(60000)
//            Log.v("CryptoSignalService", "end sleep")

            // using isActive functionality to check whether the
            // coroutine is active or not
            // https://www.geeksforgeeks.org/jobs-waiting-cancellation-in-kotlin-coroutines/
            if(isActive) {
                // do some work and then start the alarm again
                startAlarm()
            } else {
                Log.v("CryptoSignalService", "****** No Longer active ********")
            }
        }
    }

    private fun cancelOngoingJob() {
        backgroundJob?.cancel()
    }
}