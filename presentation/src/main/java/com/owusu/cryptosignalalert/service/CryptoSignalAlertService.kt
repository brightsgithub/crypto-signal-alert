package com.owusu.cryptosignalalert.service

import android.app.ActivityManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import com.owusu.cryptosignalalert.alarm.CryptoAlarmManager
import com.owusu.cryptosignalalert.notification.NotificationUtil
import com.owusu.cryptosignalalert.notification.NotificationUtil.Companion.SERVICE_NOTIFICATION_ID
import org.koin.core.KoinComponent
import org.koin.core.inject

class CryptoSignalAlertService : Service(), KoinComponent {

    private val notificationUtil: NotificationUtil by inject()
    private val cryptoAlarmManager: CryptoAlarmManager by inject()

    companion object {
        val ACTION_STOP = "com.owusu.cryptosignalalert.service.ACTION_STOP"
        val TAG = "CryptoSignalService"

        fun startAppForegroundService(context: Context, msg: String) {
            val clazz = CryptoSignalAlertService::class.java
            val serviceIntent = Intent(context, clazz)
            serviceIntent.putExtra("inputExtra", msg)
            if(!isMyServiceRunning(context)) {
                ContextCompat.startForegroundService(context, serviceIntent)
            }
        }

        fun isMyServiceRunning(context: Context) : Boolean {
            val serviceClass = CryptoSignalAlertService::class.java
            var manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.name.equals(service.service.className)) {
                    return true
                }
            }
            return false
        }

        fun stopAlertService(context: Context) {
            val serviceIntent = Intent(context, CryptoSignalAlertService::class.java)
            context?.stopService(serviceIntent)
        }
    }

    private lateinit var stopReceiver: StopReceiver

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.v("CryptoSignalService", "onStartCommand")

        intent?.let {

            stopReceiver = StopReceiver()
            registerReceiver(stopReceiver, IntentFilter(ACTION_STOP))

            val input = it.getStringExtra("inputExtra")
            input?.let {
                notificationUtil.createNotificationChannel(this@CryptoSignalAlertService)
                startForeground(SERVICE_NOTIFICATION_ID, notificationUtil.getNotification(SERVICE_NOTIFICATION_ID, input, this))
            }

            startAlarmFirstTime()
        }

        return START_REDELIVER_INTENT
    }

    /**
     * Start the alarm looping process when the service has been started.
     * This service represents that there is some kind of work occurring in the background.
     */
    private fun startAlarmFirstTime() {
        cryptoAlarmManager.startAlarmFirstTime(this)
    }

    /**
     * When the service has been stopped, stop all work.
     */
    private fun stopAlarmLooping() {
        cryptoAlarmManager.stopAlarm(this)
    }

    private inner class StopReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let { intent ->
                intent.action?.let { action ->
                    if (action == ACTION_STOP) {
                        context?.let {
                            stopAlarmLooping()
                            stopAlertService(it)
                        }
                    }
                }
            }
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        Log.d("CryptoSignalService", "onDestroy")
        if (::stopReceiver.isInitialized) {
            stopAlarmLooping()
            unregisterReceiver(stopReceiver)
        }
    }
}