package com.owusu.cryptosignalalert.workmanager

import android.content.Context
import android.text.format.DateUtils
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.owusu.cryptosignalalert.notification.NotificationUtil
import org.koin.core.KoinComponent
import org.koin.core.inject

class NotificationWorker(context: Context, workerParams: WorkerParameters):
    CoroutineWorker(context, workerParams), KoinComponent {

    private val dateUtils: DateUtils by inject()
    private val notificationUtil: NotificationUtil by inject()

    override suspend fun doWork(): Result {
        return try {

            notifyUserOfPriceTargetHit()
            Result.success()
        } catch (t: Throwable) {
            t.printStackTrace()
            Result.failure()
        }
    }

    private fun notifyUserOfPriceTargetHit() {
        notificationUtil.sendNewStandAloneNotification("price target hit")
    }
}