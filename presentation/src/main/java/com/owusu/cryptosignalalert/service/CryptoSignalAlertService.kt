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
import com.owusu.cryptosignalalert.notification.NotificationUtil.Companion.NOTIFICATION_ID
import com.owusu.cryptosignalalert.notification.NotificationUtil.Companion.createNotificationChannel
import org.koin.core.KoinComponent

class CryptoSignalAlertService : Service(), KoinComponent {

    companion object {
        val ACTION_STOP = "com.owusu.cryptosignalalert.service.ACTION_STOP"
        val TAG = "CryptoSignalService"

        fun startAppForegroundService(context: Context, msg: String) {
            val clazz = CryptoSignalAlertService::class.java
            val serviceIntent = Intent(context, clazz)
            serviceIntent.putExtra("inputExtra", msg)
            if(!isMyServiceRunning(context, clazz)) {
                ContextCompat.startForegroundService(context, serviceIntent)
            }
        }

        private fun isMyServiceRunning(context: Context, serviceClass : Class<*> ) : Boolean{
            var manager = context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.name.equals(service.service.className)) {
                    return true
                }
            }
            return false
        }

        fun stopService(context: Context, serviceClass : Class<*>) {
            val serviceIntent = Intent(context,serviceClass)
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
                createNotificationChannel(this@CryptoSignalAlertService)
                startForeground(NOTIFICATION_ID, NotificationUtil.getNotification(input, this))
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
        CryptoAlarmManager.startAlarmFirstTime()
    }

    /**
     * When the service has been stopped, stop all work.
     */
    private fun stopAlarmLooping() {
        CryptoAlarmManager.stopAlarm()
    }

    private inner class StopReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let { intent ->
                intent.action?.let { action ->
                    if (action == ACTION_STOP) {
                        context?.let {
                            stopAlarmLooping()
                            stopService(it, CryptoSignalAlertService::class.java)
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