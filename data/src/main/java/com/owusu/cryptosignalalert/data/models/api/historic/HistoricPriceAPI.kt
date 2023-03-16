package com.owusu.cryptosignalalert.data.models.api.historic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoricPriceAPI(
    @SerialName("prices")
    val prices: List<List<Double>>
)