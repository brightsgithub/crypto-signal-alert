package com.owusu.cryptosignalalert.domain.models

data class AlertItemDomain(
    val symbol:String,
    val cryptoName: String,
    val frequency: AlertFrequency,
    val currentPrice: String,
    val alertPrice: String,
    val progress: Int,
    val hasAlertBeenTriggered: Boolean
)
