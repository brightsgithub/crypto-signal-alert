package com.owusu.cryptosignalalert.viewmodels.udf.coinslist

import com.owusu.cryptosignalalert.viewmodels.udf.UdfEvent

sealed class CoinsListUdfEvent: UdfEvent {
    object HideSnackBar: CoinsListUdfEvent()
}