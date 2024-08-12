package com.owusu.cryptosignalalert.viewmodels.udf.purchase

import com.owusu.cryptosignalalert.models.SkuDetailsUI
import com.owusu.cryptosignalalert.viewmodels.udf.UdfUiState

data class PurchaseViewState(
    val skuDetailsList: List<SkuDetailsUI> = emptyList(),
    val isLoading: Boolean = false,
) : UdfUiState
