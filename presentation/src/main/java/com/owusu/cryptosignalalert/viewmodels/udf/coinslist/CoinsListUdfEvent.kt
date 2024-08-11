package com.owusu.cryptosignalalert.viewmodels.udf.coinslist

sealed class CoinsListUdfEvent {
    object OnRowClicked: CoinsListUdfEvent()
}