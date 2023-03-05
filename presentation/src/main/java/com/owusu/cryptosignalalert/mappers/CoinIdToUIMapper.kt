package com.owusu.cryptosignalalert.mappers

import com.owusu.cryptosignalalert.domain.models.CoinIdDomain
import com.owusu.cryptosignalalert.models.CoinIdUI

class CoinIdToUIMapper() {

    fun map(coinIdDomainList: List<CoinIdDomain>): List<CoinIdUI> {
        val coinIdUIList = arrayListOf<CoinIdUI>()
        coinIdDomainList.forEach { coinIdUIList.add(mapItem(it)) }
        return coinIdUIList
    }


    fun mapItem(coinIdDomain: CoinIdDomain): CoinIdUI {
         coinIdDomain.apply {
             return CoinIdUI(
                id = id,
                name = name,
                symbol = symbol
            )
        }
    }
}
