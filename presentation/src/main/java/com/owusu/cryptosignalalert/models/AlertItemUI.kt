package com.owusu.cryptosignalalert.models

import android.os.Parcelable
import com.owusu.cryptosignalalert.domain.models.AlertFrequency
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AlertItemUI (
    val symbol:String,
    val cryptoName: String,
    val frequency: AlertFrequencyUI,
    val currentPrice: String,
    val alertPrice: String,
    val progress: Int,
    val hasAlertBeenTriggered: Int
) : Parcelable
