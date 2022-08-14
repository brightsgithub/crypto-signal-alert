package com.owusu.cryptosignalalert.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.owusu.cryptosignalalert.domain.usecase.SyncForPriceTargetsUseCase
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
                syncForPriceTargetsUseCase.invoke()
                // not designed for large data. Instead place data maybe in your db and fetch?
                val outputData = workDataOf(Constants.KEY_PRICE_TARGET_UPDATED to "SOME STRING")
                Result.success(outputData)
            }
        } catch (t: Throwable) {
            Result.failure()
        }
    }
}