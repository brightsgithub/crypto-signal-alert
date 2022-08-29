package com.owusu.cryptosignalalert.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.usecase.GetPriceTargetsToAlertUserUseCase
import com.owusu.cryptosignalalert.domain.usecase.SyncForPriceTargetsUseCase
import com.owusu.cryptosignalalert.domain.usecase.UpdatePriceTargetsUseCase
import com.owusu.cryptosignalalert.notification.NotificationUtil
import com.owusu.cryptosignalalert.workmanager.Constants.KEY_PRICE_TARGET_UPDATED_STATUS
import com.owusu.cryptosignalalert.workmanager.Constants.PRICE_TARGET_NOT_UPDATED
import com.owusu.cryptosignalalert.workmanager.Constants.PRICE_TARGET_UPDATED
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

class SyncLatestPriceTargetsWorker(val context: Context, workerParams: WorkerParameters):
    CoroutineWorker(context, workerParams), KoinComponent {

    private val syncForPriceTargetsUseCase: SyncForPriceTargetsUseCase by inject()
    private val getPriceTargetsToAlertUserUseCase: GetPriceTargetsToAlertUserUseCase by inject()
    private val notificationUtil: NotificationUtil by inject()
    private val updatePriceTargetsUseCase: UpdatePriceTargetsUseCase by inject()
    private val priceNotificationHelper: PriceNotificationHelper by inject()

    override suspend fun doWork(): Result {
        return try {
            withContext(Dispatchers.IO) {
                val hasThereBeenUpdates = syncForPriceTargetsUseCase.invoke()
                // not designed for large data. Instead place data maybe in your db and fetch?
                val outputData = if(hasThereBeenUpdates) {
                    workDataOf(KEY_PRICE_TARGET_UPDATED_STATUS to PRICE_TARGET_UPDATED)
                } else {
                    workDataOf(KEY_PRICE_TARGET_UPDATED_STATUS to PRICE_TARGET_NOT_UPDATED)
                }

                val updatedPriceTargets = getPriceTargetsToAlertUserUseCase.invoke()
                val hasUserBeenAlerted = priceNotificationHelper.notifyUser(context, updatedPriceTargets)
                updatePriceTargets(hasUserBeenAlerted, updatedPriceTargets)

                Result.success(outputData)
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            Result.failure()
        }
    }

    private suspend fun updatePriceTargets(hasUserBeenAlerted: Boolean, updatedPriceTargets: List<PriceTargetDomain>) {
        if (hasUserBeenAlerted) {
            val newUpdatedPriceTargetList = arrayListOf<PriceTargetDomain>()
            updatedPriceTargets.forEach {
                newUpdatedPriceTargetList.add(it.copy(hasUserBeenAlerted = hasUserBeenAlerted))
            }
            updatePriceTargetsUseCase.invoke(UpdatePriceTargetsUseCase.Params(newUpdatedPriceTargetList))
        }
    }
}