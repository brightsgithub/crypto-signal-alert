package com.owusu.cryptosignalalert.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.owusu.cryptosignalalert.models.CoinUI

class SharedViewModel: ViewModel() {

//    var selectedCoinUI by mutableStateOf<CoinUI?>(null)
//        private set
//
//    fun addSelectedCoinUI(coinUI: CoinUI) {
//        selectedCoinUI = coinUI
//    }

    lateinit var selectedCoinUI: CoinUI

}