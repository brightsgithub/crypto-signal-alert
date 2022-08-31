package com.owusu.cryptosignalalert.workmanager

import android.content.Context
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit

// https://developer.android.com/codelabs/android-workmanager#7
/**
 * As stated in the above Android code labs example:
 * 'You'll be tagging the SaveImageToFileWorker WorkRequest, so that you can get it using getWorkInfosByTag.
 * You'll use a tag to label your work instead of using the WorkManager ID, because if your user
 * blurs multiple images, all of the saving image WorkRequests will have the same tag
 * but not the same ID. Also you are able to pick the tag.'
 *
 * this is why im using tag and no longer id as id worked intermittently.
 */
object WorkManagerStarter {

    fun startOneTimeWorkers(context: Context, tag: String) {
        val constraints = getConstraints()
        val syncLatestPriceTargetsRequest = OneTimeWorkRequestBuilder<SyncLatestPriceTargetsWorker>()
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context)
            .enqueueUniqueWork(tag, ExistingWorkPolicy.REPLACE, syncLatestPriceTargetsRequest)
    }

    // Call from application
    /**
     * To avoid the same work is running twice or more times everytime you start the application,
     * use the enqueueUniquePeriodicWork and policy ExistingPeriodicWorkPolicy.KEEP to keep the existing
     * work going if it is and do not cancel it.
     */
    fun startPeriodicWorker(context: Context, tag: String) {
        val constraints = getConstraints()

        // Minimum interval is 15 as specified by the api
        val syncLatestPriceTargetsWorker = PeriodicWorkRequestBuilder<SyncLatestPriceTargetsWorker>(
            repeatInterval = 15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            tag,
            ExistingPeriodicWorkPolicy.KEEP,
            syncLatestPriceTargetsWorker
        )
    }

    private fun getConstraints(): Constraints {
        return Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .build()
    }
}