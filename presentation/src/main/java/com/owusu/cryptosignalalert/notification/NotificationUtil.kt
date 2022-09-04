package com.owusu.cryptosignalalert.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.owusu.cryptosignalalert.CryptoSignalAlertApp
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.service.CryptoSignalAlertService
import com.owusu.cryptosignalalert.views.activities.MainActivity
import kotlin.random.Random

class NotificationUtil {
    companion object {

        val SERVICE_NOTIFICATION_ID = 12
        val CHANNEL_ID = "cryptoSignalAlertIdNew"
    }
        fun sendNewStandAloneNotification(newMsg: String) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val manager = CryptoSignalAlertApp.instance.getSystemService(NotificationManager::class.java)
                val notificationId = rand(1, Int.MAX_VALUE)
                manager.notify(notificationId, getNewNotification(notificationId ,newMsg, CryptoSignalAlertApp.instance))
            }
        }

        private fun rand(start: Int, end: Int): Int {
            require(start <= end) { "Illegal Argument" }
            val rand = Random(System.nanoTime())
            return (start..end).random(rand)
        }

        fun updateServiceNotification(newMsg: String) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val manager = CryptoSignalAlertApp.instance.getSystemService(NotificationManager::class.java)
                manager.notify(SERVICE_NOTIFICATION_ID, getNotification(SERVICE_NOTIFICATION_ID ,newMsg, CryptoSignalAlertApp.instance))
            }
        }

        fun getNotification(notificationId: Int, input: String, context: Context): Notification {
            val actionStopIntent = Intent(CryptoSignalAlertService.ACTION_STOP)

            var updatePendingIntent: PendingIntent? = null
            updatePendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getBroadcast(context, notificationId, actionStopIntent, PendingIntent.FLAG_IMMUTABLE)
            } else {
                PendingIntent.getBroadcast(context, notificationId, actionStopIntent, PendingIntent.FLAG_ONE_SHOT)
            }

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(
                    BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.ic_launcher_foreground))
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setContentIntent(getPendingIntent(context))
                .addAction(R.drawable.ic_launcher_foreground, "Stop", updatePendingIntent)
                .setOnlyAlertOnce(true)
                .build()
            return notification
        }

        private fun getNewNotification(notificationId: Int, input: String, context: Context): Notification {
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(
                    BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.ic_launcher_foreground))
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setContentIntent(getPendingIntent(context))
                .build()
            return notification
        }

        private fun getPendingIntent(context: Context): PendingIntent {
            val notificationIntent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
            )
            return pendingIntent
        }

        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val serviceChannel = NotificationChannel(
                    CHANNEL_ID,
                    "Crypto Signal Alert",
                    NotificationManager.IMPORTANCE_HIGH
                )

                val manager = context.getSystemService(NotificationManager::class.java)
                serviceChannel.setShowBadge(false)
                manager?.createNotificationChannel(serviceChannel)
            }
        }
}