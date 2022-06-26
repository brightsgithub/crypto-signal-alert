package com.owusu.cryptosignalalert.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class AlertFrequencyUI: Parcelable {
    @Parcelize
    object Once: AlertFrequencyUI()
    @Parcelize
    object EveryTime: AlertFrequencyUI()
}
