package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.CoinDomain
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
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
    private val dateUtils: CryptoDateUtils
    ): SuspendedUseCaseUnit<Boolean> {

    private val MAX_BATCH_SIZE = 5

    // needs to have a flow so that the Alarm manager can listen
    // and the calling view model can listen
    override suspend fun invoke(): Boolean {
        System.out.println("\nSyncForPriceTargetsUseCase ################################################")
        System.out.println("SyncForPriceTargetsUseCase SYNC STARTED "+ Date())

        var updatedPriceTargets = emptyList<PriceTargetDomain>()

        // 1. getPriceTargetsThatHaveNotBeenHitUseCase which will give you a list of price targets.
        // need to get the price targets that have not been hit
        val priceTargets = getPriceTargets()
        if (priceTargets.isEmpty()) return false

        val ids = getListOfIds(priceTargets)

        // 2. Query against the api for targets obtained above.
        val coinsList = getCoinsList(ids)

        val batches = priceTargets.chunked(MAX_BATCH_SIZE) // since we can have many repeated targets i.e. multiple btc targets

        System.out.println("SyncForPriceTargetsUseCase original priceTargets size = "+ priceTargets.size)
        System.out.println("SyncForPriceTargetsUseCase max size per batch = $MAX_BATCH_SIZE")
        System.out.println("SyncForPriceTargetsUseCase Therefore total number of batches = "+batches.size)
        for ((index, currentPriceTargetBatch) in batches.withIndex()) {

            System.out.println("\nSyncForPriceTargetsUseCase executing batch "+(index + 1)+ " of "+ batches.size + " ...............")
            System.out.println("SyncForPriceTargetsUseCase current batch size  = "+currentPriceTargetBatch.size)
            // 3. carry out business rules so we know what targets have been met etc. and we know
            // what fields to update in the DB via savePriceTargetUseCase
            updatedPriceTargets = mergeOldPriceTargetWithNew(coinsList,currentPriceTargetBatch)

            // 4. update all price targets. At this point some may have met their price targets
            // and some will just have updated their current price updated
            updatePriceTargets(updatedPriceTargets)
            if (index != batches.lastIndex) {
                System.out.println("SyncForPriceTargetsUseCase before delay "+ Date())
                delay(65000) // Wait for 1 minute and 5 seconds  before processing the next batch
                System.out.println("SyncForPriceTargetsUseCase after delay "+ Date())
            } else {
                System.out.println("\nSyncForPriceTargetsUseCase OK, All BATCHES COMPLETED!!! ******** "+ Date())
                System.out.println("SyncForPriceTargetsUseCase ################################################")
            }
        }

        return updatedPriceTargets.isNotEmpty()
    }

    private suspend fun updatePriceTargets(updatedPriceTargets: List<PriceTargetDomain>) {
        updatePriceTargetsUseCase.invoke(UpdatePriceTargetsUseCase.Params(updatedPriceTargets))
    }

    private suspend fun getPriceTargets(): List<PriceTargetDomain> {
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

        return getCoinsListUseCase.invoke(params)
    }
}