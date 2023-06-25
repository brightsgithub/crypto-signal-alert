package com.owusu.cryptosignalalert.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.owusu.cryptosignalalert.domain.usecase.*
import com.owusu.cryptosignalalert.workmanager.Constants.DISPLAY_LATEST_DATA
import com.owusu.cryptosignalalert.workmanager.Constants.KEY_PRICE_TARGET_UPDATED_STATUS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class SyncLatestPriceTargetsWorker(val context: Context, workerParams: WorkerParameters):
    CoroutineWorker(context, workerParams), KoinComponent {

    private val syncForPriceTargetsUseCase: SyncForPriceTargetsUseCase by inject()
    private val getPriceTargetsToAlertUserUseCase: GetPriceTargetsToAlertUserUseCase by inject()
    private val updatePriceUseCase: UpdatePriceTargetsForAlertedUserUseCase by inject()
    private val priceNotificationHelper: PriceNotificationHelper by inject()
    private val syncHasStartedStatusUseCase: SyncHasStartedStatusUseCase by inject()
    private val syncHasFinishedStatusUseCase: SyncHasFinishedStatusUseCase by inject()
    private val populateCoinIdsUseCase: PopulateCoinIdsUseCase by inject()

    override suspend fun doWork(): Result {
        return try {
            withContext(Dispatchers.IO) {
                syncHasStartedStatusUseCase.execute()
                syncForPriceTargetsUseCase.invoke()
                val updatedPriceTargets = getPriceTargetsToAlertUserUseCase.invoke()
                val hasUserBeenAlerted = priceNotificationHelper.notifyUser(context, updatedPriceTargets)
                updatePriceUseCase.invoke(UpdatePriceTargetsForAlertedUserUseCase.Params(hasUserBeenAlerted, updatedPriceTargets))
                populateCoinIdsUseCase.invoke(
                    PopulateCoinIdsUseCase.Params(currentTime = Calendar.getInstance(), delayInMilliSecs = 60000)
                )
                // not designed for large data. Instead place data maybe in your db and fetch?
                val outputData = workDataOf(KEY_PRICE_TARGET_UPDATED_STATUS to DISPLAY_LATEST_DATA)
                val result = Result.success(outputData)
                result
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            Result.failure()
        } finally {
            withContext(Dispatchers.IO) {
                syncHasFinishedStatusUseCase.execute()
            }
        }
    }
}