package com.owusu.cryptosignalalert.workmanager

import android.content.Context
import android.text.format.DateUtils
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.usecase.GetPriceTargetsToAlertUserUseCase
import com.owusu.cryptosignalalert.domain.usecase.UpdatePriceTargetsUseCase
import com.owusu.cryptosignalalert.notification.NotificationUtil
import org.koin.core.KoinComponent
import org.koin.core.inject

class NotificationWorker(context: Context, workerParams: WorkerParameters):
    CoroutineWorker(context, workerParams), KoinComponent {

    private val dateUtils: DateUtils by inject()
    private val notificationUtil: NotificationUtil by inject()
    private val getPriceTargetsToAlertUserUseCase: GetPriceTargetsToAlertUserUseCase by inject()
    private val updatePriceTargetsUseCase: UpdatePriceTargetsUseCase by inject()
    override suspend fun doWork(): Result {
        return try {

            val priceTargets = getPriceTargetsToAlertUserUseCase.invoke()
            notifyUserOfPriceTargetHit(priceTargets)
            updatePriceTargets(priceTargets)
            Result.success()
        } catch (t: Throwable) {
            t.printStackTrace()
            Result.failure()
        }
    }

    private fun notifyUserOfPriceTargetHit(priceTargets: List<PriceTargetDomain>) {
        notificationUtil.sendNewStandAloneNotification("price target hit")
    }

    private suspend fun updatePriceTargets(updatedPriceTargets: List<PriceTargetDomain>) {
        updatePriceTargetsUseCase.invoke(UpdatePriceTargetsUseCase.Params(updatedPriceTargets))
    }
}