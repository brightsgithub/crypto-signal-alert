package com.owusu.cryptosignalalert.models

data class SharedViewState(
    val appSnackBar: AppSnackBar = AppSnackBar(actionCallback = {})
)

data class AppSnackBar(
    val shouldShowSnackBar: Boolean = false,
    val snackBarMessage: String = "",
    val actionLabel: String = "",
    val actionCallback:() -> Unit
)
