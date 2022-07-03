package com.owusu.cryptosignalalert.data.endpoints

class EndPointProd : EndPoints {
    override fun getHostName() : String {
        return "https://api.coingecko.com/api/v3/"
    }
}