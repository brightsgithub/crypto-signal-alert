package com.owusu.cryptosignalalert.models

data class SharedViewState(
    val appSnackBar: AppSnackBar = AppSnackBar(actionCallback = {}),
    val purchasedState: PurchasedState = PurchasedState(),
    val actionButtonState: ActionButtonState = ActionButtonState()
)

data class AppSnackBar(
    val shouldShowSnackBar: Boolean = false,
    val snackBarMessage: String = "",
    val actionLabel: String = "",
    val actionCallback:() -> Unit
)

data class PurchasedState(
    val isAppFree: Boolean = false,
    val isPriceTargetLimitPurchased: Boolean = false,
    val isAdsPurchased: Boolean = false
)

data class ActionButtonState(
    val shouldShowSearchIcon: Boolean = true,
    val shouldShowSettingsIcon: Boolean = true
)