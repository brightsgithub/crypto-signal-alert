package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.CoinDomain
import com.owusu.cryptosignalalert.domain.models.CoinsListResult
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.models.states.UpdateSyncState
import com.owusu.cryptosignalalert.domain.repository.PriceTargetsRepository
import com.owusu.cryptosignalalert.domain.utils.CryptoDateUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import java.util.*

/*
SINGLE RESPONSIBILITY PRINCIPLE VIOLATIONS
Testing a private method directly is a good indicator of an SRP violation.
One option, and often the easiest if the abstraction makes sense, is to perform an extract class
refactoring. The result of this refactoring is that the private method in question is now a
public method on a newly created class. Now that method can be tested directly in a unit
test targeting the extracted class. This new class now becomes a dependency of the original
class and can be replaced with a test double in the original class’ test cases.
https://anthonysciamanna.com/2016/02/14/should-private-methods-be-tested.html

Applying the above allowed me to create MergeOldPriceTargetWithNewDataUseCase since these methods
used to be in this class and has now been extracted out and is now testable
*/

class SyncForPriceTargetsUseCase(
    private val getPriceTargetsThatHaveNotBeenHitUseCase: GetPriceTargetsThatHaveNotBeenHitUseCase,
    private val getCoinsListUseCase: GetCoinsListUseCase,
    private val updatePriceTargetsUseCase: UpdatePriceTargetsUseCase,
    private val mergeOldPriceTargetWithNewDataUseCase: MergeOldPriceTargetWithNewDataUseCase,
    private val dateUtils: CryptoDateUtils,
    private val priceTargetsRepository: PriceTargetsRepository,
    private val calculateRemainingWorkUseCase: CalculateRemainingWorkUseCase,
    ): SuspendedUseCaseUnit<Boolean> {

    private val MAX_BATCH_SIZE = 5
    private val DELAY_PERIOD_1_MIN_5_SECONDS = 65000L

    // needs to have a flow so that the Alarm manager can listen
    // and the calling view model can listen
    override suspend fun invoke(): Boolean {
        System.out.println("\nSyncForPriceTargetsUseCase ################################################")
        System.out.println("SyncForPriceTargetsUseCase SYNC STARTED "+ Date())

        var updatedPriceTargets = emptyList<PriceTargetDomain>()

        // 1. getPriceTargetsThatHaveNotBeenHitUseCase which will give you a list of price targets.
        // need to get the price targets that have not been hit
        val priceTargets = getPriceTargetsThatHaveNotBeenHit()
        if (priceTargets.isEmpty()) return false

        val ids = getListOfIds(priceTargets)

        // 2. Query against the api for targets obtained above.
        val coinsList = getCoinsList(ids)

        if (coinsList.isEmpty()) return false

        // 3. Perform the Sync in batches so we don't blow our free api rate limit of 10 requests per min.
        updatedPriceTargets = performSyncInBatches(
            priceTargets,
            updatedPriceTargets,
            coinsList,
            MAX_BATCH_SIZE,
            DELAY_PERIOD_1_MIN_5_SECONDS
        )

        return updatedPriceTargets.isNotEmpty()
    }

    private suspend fun performSyncInBatches(
        priceTargets: List<PriceTargetDomain>,
        updatedPriceTargets: List<PriceTargetDomain>,
        coinsList: List<CoinDomain>,
        batchSize: Int,
        delayInMilliSeconds: Long
    ): List<PriceTargetDomain> {
        var updatedPriceTargets1 = updatedPriceTargets
        // since we can have many repeated targets i.e. multiple btc targets
        val batches = priceTargets.chunked(batchSize)

        System.out.println("SyncForPriceTargetsUseCase original priceTargets size = " + priceTargets.size)
        System.out.println("SyncForPriceTargetsUseCase max size per batch = $batchSize")
        System.out.println("SyncForPriceTargetsUseCase Therefore total number of batches = " + batches.size)
        for ((index, currentPriceTargetBatch) in batches.withIndex()) {

            System.out.println("\nSyncForPriceTargetsUseCase executing batch " + (index + 1) + " of " + batches.size + " ...............")
            System.out.println("SyncForPriceTargetsUseCase current batch size  = " + currentPriceTargetBatch.size)
            // 1. carry out business rules so we know what targets have been met etc. and we know
            // what fields to update in the DB via savePriceTargetUseCase
            updatedPriceTargets1 = mergeOldPriceTargetWithNew(coinsList, currentPriceTargetBatch)

            // 2. update all price targets. At this point some may have met their price targets
            // and some will just have updated their current price updated
            updatePriceTargets(updatedPriceTargets1)

            if (index != batches.lastIndex) {
                updateSyncState(totalPriceTargetsSize = priceTargets.size, numberOfItemsCompleted = currentPriceTargetBatch.size)
                System.out.println("SyncForPriceTargetsUseCase before delay " + Date())
                delay(delayInMilliSeconds)
                System.out.println("SyncForPriceTargetsUseCase after delay " + Date())
            } else {
                updateSyncState(totalPriceTargetsSize = 0, numberOfItemsCompleted = 0)
                System.out.println("\nSyncForPriceTargetsUseCase OK, All BATCHES COMPLETED!!! ******** " + Date())
                System.out.println("SyncForPriceTargetsUseCase ################################################")
            }
        }
        return updatedPriceTargets1
    }

    private suspend fun updatePriceTargets(updatedPriceTargets: List<PriceTargetDomain>) {
        updatePriceTargetsUseCase.invoke(UpdatePriceTargetsUseCase.Params(updatedPriceTargets))
    }

    private suspend fun getPriceTargetsThatHaveNotBeenHit(): List<PriceTargetDomain> {
        // Collect the current list result immediately
        return getPriceTargetsThatHaveNotBeenHitUseCase.invoke().first()
    }

    private suspend fun mergeOldPriceTargetWithNew(
        coinsList: List<CoinDomain>, priceTargets: List<PriceTargetDomain>): List<PriceTargetDomain> {
        val lastUpdated = dateUtils.convertDateToFormattedStringWithTime(Calendar.getInstance().timeInMillis)
        val params = MergeOldPriceTargetWithNewDataUseCase.Params(coinsList, priceTargets, lastUpdated)
        return mergeOldPriceTargetWithNewDataUseCase.invoke(params)
    }

    private fun getListOfIds(priceTargets: List<PriceTargetDomain>): List<String> {
        val ids = arrayListOf<String>()
        priceTargets.map {
            ids.add(requireNotNull(it.id))
        }
        return ids
    }

    private suspend fun getCoinsList(ids: List<String>): List<CoinDomain> {

        val params = GetCoinsListUseCase.Params(
            page = 1,
            recordsPerPage = ids.size,
            ids = ids.joinToString(","),
            currencies = "usd"
        )

        return when (val data = getCoinsListUseCase.invoke(params)) {
            is CoinsListResult.Error -> {
                System.out.println("SyncForPriceTargetsUseCase rate limit reached error ")
                emptyList()
            }
            is CoinsListResult.Success -> {
                data.coinDomainList
            }
        }
    }

    private suspend fun updateSyncState(totalPriceTargetsSize: Int, numberOfItemsCompleted: Int) {
        var state = UpdateSyncState()
        val params = CalculateRemainingWorkUseCase.Params(totalPriceTargetsSize, numberOfItemsCompleted)
        val remainingPercentage = calculateRemainingWorkUseCase.invoke(params)
        state = state.copy(remainingPercentageOfWorkToBeDone = remainingPercentage)
        priceTargetsRepository.updateSyncState(state)
    }
}