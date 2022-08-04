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
import androidx.core.content.ContextCompat.getSystemService
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.service.CryptoSignalAlertService
import com.owusu.cryptosignalalert.views.activities.MainActivity

class NotificationUtil {
    companion object {

        val NOTIFICATION_ID = 12
        val CHANNEL_ID = "cryptoSignalAlertIdNew"

        fun getNotification(input: String, context: Context): Notification {

            val actionStopIntent = Intent(CryptoSignalAlertService.ACTION_STOP)
            val updatePendingIntent =
                PendingIntent.getBroadcast(context, NOTIFICATION_ID, actionStopIntent, PendingIntent.FLAG_ONE_SHOT)

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
                .build()
            return notification
        }

        fun getPendingIntent(context: Context): PendingIntent {
            val notificationIntent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0, notificationIntent, 0
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
}