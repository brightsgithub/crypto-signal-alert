package com.owusu.cryptosignalalert.data.endpoints

import com.owusu.cryptosignalalert.data.enpoints.EndPointsParent

// https://stackoverflow.com/questions/19645451/folder-naming-convention-for-gradle-build-variants
// Keep in mind EndPoints is deleted from the main folder so that it can exist in each build variant
class EndPoints: EndPointsParent() {
    override fun getHostName(): String {
        return "http://localhost:8080/api/v3/"
    }
}