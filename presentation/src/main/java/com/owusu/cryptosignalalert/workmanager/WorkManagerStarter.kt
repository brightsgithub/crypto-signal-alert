package com.owusu.cryptosignalalert.workmanager

import android.content.Context
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit

object WorkManagerStarter {

    fun startOneTimeWorkers(context: Context): UUID  {
        // Create the Constraints
        val constraints = getConstraints()

        // Define the input
        //val imageData = workDataOf(Constants.KEY_IMAGE_URI to imageUriString)

        val syncLatestPriceTargetsRequest = OneTimeWorkRequestBuilder<SyncLatestPriceTargetsWorker>()
            .setConstraints(constraints)
            .build()

//        val notificationRequest = OneTimeWorkRequestBuilder<PriceNotificationHelper>()
//            .setConstraints(constraints)
//            .build()

        // here we chain the work.
        WorkManager.getInstance(context)
            .beginWith(syncLatestPriceTargetsRequest)
            //.then(notificationRequest)
            .enqueue()

        return syncLatestPriceTargetsRequest.id
    }

    // Call from application
    /**
     * To avoid the same work is running twice or more times everytime you start the application,
     * use the enqueueUniquePeriodicWork and policy ExistingPeriodicWorkPolicy.KEEP to keep the existing
     * work going if it is and do not cancel it.
     */
    fun startPeriodicWorker(context: Context): UUID {
        val constraints = getConstraints()

        // Minimum interval is 15 as specified by the api
        val syncLatestPriceTargetsWorker = PeriodicWorkRequestBuilder<SyncLatestPriceTargetsWorker>(
            repeatInterval = 15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "SyncPriceTargetsUniqueName",
        ExistingPeriodicWorkPolicy.KEEP,
            syncLatestPriceTargetsWorker)

        return syncLatestPriceTargetsWorker.id
    }

    private fun getConstraints(): Constraints {
        return Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .build()
    }
}