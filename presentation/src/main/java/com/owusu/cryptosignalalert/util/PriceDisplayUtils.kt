package com.owusu.cryptosignalalert.util

import java.math.BigDecimal

class PriceDisplayUtils(private val priceUtils: PriceUtils) {

    fun convertToDecimalPlace(price: Double?, decimalPlaces: Int): Double? {
        if (price == null) return price
        val a = BigDecimal(price)
        return a.setScale(decimalPlaces, BigDecimal.ROUND_HALF_EVEN).toDouble()
    }

    fun convertPriceToString(price: Double?): String? {
        if (price == null) return ""
        return PriceUtils.numberFormatter("USD", price.toString())
    }

    private fun convertPriceToDouble(price: String?): Double? {
        if (price == null) return 0.0
        return price.toDouble()
    }

}