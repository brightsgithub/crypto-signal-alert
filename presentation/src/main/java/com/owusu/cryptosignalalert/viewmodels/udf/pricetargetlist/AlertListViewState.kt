package com.owusu.cryptosignalalert.viewmodels.udf.pricetargetlist

import com.owusu.cryptosignalalert.models.PriceTargetUI
import com.owusu.cryptosignalalert.viewmodels.udf.UdfUiState

data class AlertListViewState(
    val isLoading: Boolean = false,
    val priceTargets: List<PriceTargetUI> = listOf(),
    val numberOfTargetsMet: Int = 0,
    val totalNumberOfTargets: Int = 0,
    val remainingSyncPercentageToBeUpdated: Float = 0f,
    val shouldShowSyncState: Boolean = false
): UdfUiState
