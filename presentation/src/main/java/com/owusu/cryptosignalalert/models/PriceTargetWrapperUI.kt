package com.owusu.cryptosignalalert.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PriceTargetWrapperUI(val priceTargets: List<PriceTargetUI>): Parcelable
