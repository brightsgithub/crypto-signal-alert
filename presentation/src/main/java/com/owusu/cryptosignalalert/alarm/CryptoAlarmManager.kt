package com.owusu.cryptosignalalert.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.owusu.cryptosignalalert.CryptoSignalAlertApp
import com.owusu.cryptosignalalert.domain.usecase.SyncForPriceTargetsUseCase
import com.owusu.cryptosignalalert.notification.NotificationUtil
import com.owusu.cryptosignalalert.receivers.AlarmReceiver
import com.owusu.cryptosignalalert.service.CryptoSignalAlertService
import com.owusu.cryptosignalalert.domain.utils.CryptoDateUtils
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
//import org.koin.core.KoinComponent
//import org.koin.core.inject
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

    // private lateinit var context: CryptoSignalAlertApp
    private val cryptoDateUtils: CryptoDateUtils by inject()
    private val notificationUtil: NotificationUtil by inject()
    private val getLatestPricesForTargetsUseCase: SyncForPriceTargetsUseCase by inject()
    private val START_ALARM_REQUEST_CODE = 412232
    private val fiveMins = 300000L
    private val thirtySeconds = 30000L
    private val oneMin = 60000L
    private val twoMin = 120000L
    private val thirtyMins = oneMin * 30
    private val fifteenMins = oneMin * 15
    private val ALARM_INTERVAL = oneMin * 2
    private var hasInitBeenCalled: Boolean = false
    private var backgroundJob: Job? = null

    fun startAlarmFirstTime(context: Context) {
        init()
        startAlarm(1000, context)
    }

    private fun init() {
        if(!hasInitBeenCalled) {
            hasInitBeenCalled = true
            // context = CryptoSignalAlertApp.instance
            Log.v("CryptoSignalService", "CryptoAlertAlarm.init called")
        } else {
            Log.v("CryptoSignalService", "CryptoAlertAlarm.init already called!")
        }
    }

    fun startAlarm(interval: Long = ALARM_INTERVAL, context: Context) {
        try {
            //stopAlarm(context) // stop alarm first?
            cancelOngoingJob()
            // just in case we get an network error or something
            Log.v("CryptoSignalService", "CryptoAlertAlarm.starting alarm.....")
            val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            var pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getBroadcast(context, START_ALARM_REQUEST_CODE, getAlarmIntent(context), PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT)
            } else {
                PendingIntent.getBroadcast(context, START_ALARM_REQUEST_CODE, getAlarmIntent(context), PendingIntent.FLAG_CANCEL_CURRENT)
            }

            alarmMgr.set(
                AlarmManager.RTC_WAKEUP,
                Calendar.getInstance().timeInMillis + interval,
                pendingIntent
            )

            sendNotifications(interval)

        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    private fun sendNotifications(interval: Long) {

        val nextAlarm = cryptoDateUtils.convertDateToFormattedStringWithTime(Calendar.getInstance().timeInMillis + interval)
        val nextUpdatedAMsg = "Next alarm scheduled at " + nextAlarm

        if (CryptoSignalAlertService.isMyServiceRunning(CryptoSignalAlertApp.instance)) {
            notificationUtil.updateServiceNotification("Last updated at "+ cryptoDateUtils.convertDateToFormattedStringWithTime(Calendar.getInstance().timeInMillis) + "\n" + nextUpdatedAMsg)
        }

        Log.v("CryptoSignalService",nextUpdatedAMsg)
        if (!CryptoSignalAlertService.isMyServiceRunning(CryptoSignalAlertApp.instance)) {
            //notificationUtil.sendNewStandAloneNotification("Service DEAD. Next alarm at " + nextAlarm, 181818)
        }
    }

    fun stopAlarm(context: Context) {
        cancelOngoingJob()
        Log.v("CryptoSignalService", "CryptoAlertAlarm.stopAlarmListenerEvent called")
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

        var pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(context, START_ALARM_REQUEST_CODE, getAlarmIntent(context), PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(context, START_ALARM_REQUEST_CODE, getAlarmIntent(context), PendingIntent.FLAG_NO_CREATE)
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
    private fun getAlarmIntent(context: Context): Intent {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.action = INTENT_ACTION_START_ALARM_LISTENER
        return intent
    }

    fun onReadyToStartBackgroundWork(context: Context) {
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
                getLatestPricesForTargetsUseCase()
                // past the list to notificationManager so it can carry out alerts
                startAlarm(context = context)
            } else {
                Log.v("CryptoSignalService", "****** No Longer active ********")
            }
        }
    }

    private fun cancelOngoingJob() {
        backgroundJob?.cancel()
    }
}