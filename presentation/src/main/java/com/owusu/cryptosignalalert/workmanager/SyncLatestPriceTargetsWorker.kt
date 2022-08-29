package com.owusu.cryptosignalalert.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.owusu.cryptosignalalert.domain.usecase.SyncForPriceTargetsUseCase
import com.owusu.cryptosignalalert.workmanager.Constants.KEY_PRICE_TARGET_NOT_UPDATED
import com.owusu.cryptosignalalert.workmanager.Constants.KEY_PRICE_TARGET_UPDATED
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

class SyncLatestPriceTargetsWorker(context: Context, workerParams: WorkerParameters):
    CoroutineWorker(context, workerParams), KoinComponent {

    private val syncForPriceTargetsUseCase: SyncForPriceTargetsUseCase by inject()
    override suspend fun doWork(): Result {
        return try {
            withContext(Dispatchers.IO) {
                val hasThereBeenUpdates = syncForPriceTargetsUseCase.invoke()
                // not designed for large data. Instead place data maybe in your db and fetch?
                val outputData = if(hasThereBeenUpdates) {
                    workDataOf(KEY_PRICE_TARGET_UPDATED to KEY_PRICE_TARGET_UPDATED)
                } else {
                    workDataOf(KEY_PRICE_TARGET_NOT_UPDATED to KEY_PRICE_TARGET_NOT_UPDATED)
                }

                Result.success(outputData)
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            Result.failure()
        }
    }
}