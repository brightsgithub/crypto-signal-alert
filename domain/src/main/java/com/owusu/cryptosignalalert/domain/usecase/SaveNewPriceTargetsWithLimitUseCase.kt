package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.models.PurchaseConstants
import com.owusu.cryptosignalalert.domain.repository.AppPreferencesRepository
import com.owusu.cryptosignalalert.domain.repository.PriceTargetsRepository

class SaveNewPriceTargetsWithLimitUseCase(
    private val priceTargetsRepository: PriceTargetsRepository,
    private val saveNewPriceTargetsUseCase: SaveNewPriceTargetsUseCase,
    private val appPreferencesRepository: AppPreferencesRepository
): SuspendedUseCase<SaveNewPriceTargetsWithLimitUseCase.Params, Boolean> {

    override suspend fun invoke(params: Params): Boolean {

        val noOfRecords = priceTargetsRepository.getPriceTargetsThatHaveNotBeenHitCount() + 1

        val noOfRecsAllowed = if(appPreferencesRepository.isPriceTargetLimitPurchased()) {
            PurchaseConstants.MAX_PRICE_TARGET_RECORDS_ALLOWED
        } else {
            PurchaseConstants.MIN_PRICE_TARGET_RECORDS_ALLOWED
        }

        return if (noOfRecords > noOfRecsAllowed) {
            // fail
            false
        } else {
            saveNewPriceTargetsUseCase.invoke(SaveNewPriceTargetsUseCase.Params(params.priceTargets))
            true
        }
    }
    data class Params(val priceTargets: List<PriceTargetDomain>)
}