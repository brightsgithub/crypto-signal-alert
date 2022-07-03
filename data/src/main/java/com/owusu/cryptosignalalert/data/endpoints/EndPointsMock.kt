package com.owusu.cryptosignalalert.data.endpoints

class EndPointsMock : EndPoints {
    override fun getHostName() : String {
        return "http://localhost:8080/api/v3/"
    }
}