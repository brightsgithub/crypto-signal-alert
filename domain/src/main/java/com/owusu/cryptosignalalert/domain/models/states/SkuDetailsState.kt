package com.owusu.cryptosignalalert.domain.models.states

import com.owusu.cryptosignalalert.domain.models.SkuDetailsDomain

sealed class SkuDetailsState {
    data class Success(val skuList: List<SkuDetailsDomain>): SkuDetailsState()
    object Ready: SkuDetailsState()
    object NotReady: SkuDetailsState()
}
