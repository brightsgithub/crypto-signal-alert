package com.owusu.cryptosignalalert.models

data class SharedViewState(
    val appSnackBar: AppSnackBar = AppSnackBar()
)

data class AppSnackBar(
    val shouldShowSnackBar: Boolean = false,
    val errorMsg: String = "",
    val actionLabel: String = "",
)
