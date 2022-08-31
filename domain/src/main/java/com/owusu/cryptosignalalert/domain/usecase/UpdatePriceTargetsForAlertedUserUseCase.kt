package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain

class UpdatePriceTargetsForAlertedUserUseCase(
    private val updatePriceTargetsUseCase: UpdatePriceTargetsUseCase
): SuspendedUseCase<UpdatePriceTargetsForAlertedUserUseCase.Params, Unit> {
    override suspend fun invoke(params: Params) {

        val hasUserBeenAlerted = params.hasUserBeenAlerted
        val updatedPriceTargets = params.updatedPriceTargets

        if (hasUserBeenAlerted) {
            val newUpdatedPriceTargetList = arrayListOf<PriceTargetDomain>()
            updatedPriceTargets.forEach {
                newUpdatedPriceTargetList.add(it.copy(hasUserBeenAlerted = hasUserBeenAlerted))
            }
            updatePriceTargetsUseCase.invoke(UpdatePriceTargetsUseCase.Params(newUpdatedPriceTargetList))
        }
    }
    data class Params(val hasUserBeenAlerted: Boolean, val updatedPriceTargets: List<PriceTargetDomain>)
}