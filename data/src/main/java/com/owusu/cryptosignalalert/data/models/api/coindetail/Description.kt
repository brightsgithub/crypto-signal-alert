package com.owusu.cryptosignalalert.data.models.api.coindetail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Description(
    @SerialName("en")
    val en: String
)