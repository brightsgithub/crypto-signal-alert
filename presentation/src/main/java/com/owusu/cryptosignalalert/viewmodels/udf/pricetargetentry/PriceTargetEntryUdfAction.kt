package com.owusu.cryptosignalalert.viewmodels.udf.pricetargetentry

import com.owusu.cryptosignalalert.viewmodels.udf.UdfAction

sealed class PriceTargetEntryUdfAction: UdfAction {

    object NavigateToPriceTargetsAppEntry: PriceTargetEntryUdfAction()
}
