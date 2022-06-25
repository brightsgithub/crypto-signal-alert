package com.owusu.cryptosignalalert.domain.models

sealed class AlertFrequency {
    object Once: AlertFrequency()
    object EveryTime: AlertFrequency()
}
