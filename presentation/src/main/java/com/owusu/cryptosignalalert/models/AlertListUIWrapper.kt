package com.owusu.cryptosignalalert.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AlertListUIWrapper(val alertList: List<AlertItemUI>): Parcelable
