package com.owusu.cryptosignalalert.workmanager

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class WorkManagerStarter {

    fun startWorkers(context: Context) {
        // Create the Constraints
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .build()

        // Define the input
        //val imageData = workDataOf(Constants.KEY_IMAGE_URI to imageUriString)

        val syncLatestPriceTargetsRequest = OneTimeWorkRequestBuilder<SyncLatestPriceTargetsWorker>()
            .setConstraints(constraints)
            .build()

        val notificationRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setConstraints(constraints)
            .build()

        // here we chain the work.
        WorkManager.getInstance(context)
            .beginWith(syncLatestPriceTargetsRequest)
            .then(notificationRequest)
            .enqueue()
    }

}