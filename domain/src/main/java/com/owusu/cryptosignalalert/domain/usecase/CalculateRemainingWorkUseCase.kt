package com.owusu.cryptosignalalert.domain.usecase

class CalculateRemainingWorkUseCase: UseCase<CalculateRemainingWorkUseCase.Params, Float> {

    data class Params(val totalPriceTargetsSize: Int, val numberOfItemsCompleted: Int)

    override fun invoke(params: Params): Float {
        return calculateRemainingPercentageOfWorkDone(
            params.totalPriceTargetsSize,
            params.numberOfItemsCompleted
        )
    }

    private fun calculateRemainingPercentageOfWorkDone(totalPriceTargetsSize: Int, numberOfItemsCompleted: Int): Float {

        if (totalPriceTargetsSize == 0) {
            return 1f // 100%
        }

        val remainingAmount = totalPriceTargetsSize - numberOfItemsCompleted
        val remainingPercentage = (remainingAmount.toFloat() / totalPriceTargetsSize.toFloat())
        return remainingPercentage
    }
}