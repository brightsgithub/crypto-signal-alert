package com.owusu.cryptosignalalert.viewmodels.udf.home

import com.owusu.cryptosignalalert.viewmodels.udf.UdfAction

sealed class HomeUdfAction: UdfAction {
    object NavigateToPriceTargetEntry: HomeUdfAction()
    object NavigateToSearch: HomeUdfAction()
    object NavigateToSettings: HomeUdfAction()
    object NavigateToPriceTargets: HomeUdfAction()
    object NavigateToPurchase: HomeUdfAction()
    object ActionNothing: HomeUdfAction()
}