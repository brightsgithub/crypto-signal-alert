package com.owusu.cryptosignalalert.models

data class SharedViewState(
    val appSnackBar: AppSnackBar = AppSnackBar(actionCallback = {}),
    val purchasedState: PurchasedState = PurchasedState(),
    val actionButtonState: ActionButtonState = ActionButtonState(),
    val shouldShowInterstitialAd: Boolean = false
)

data class AppSnackBar(
    val shouldShowSnackBar: Boolean = false,
    val snackBarMessage: String = "",
    val actionLabel: String = "",
    val actionCallback:() -> Unit,
    val shouldShowIndefinite: Boolean = false
)

data class PurchasedState(
    val isAppFree: Boolean = false,
    val isPriceTargetLimitPurchased: Boolean = false,
    val isAdsPurchased: Boolean = false
)

data class ActionButtonState(
    val title: String = "",
    val shouldShowSearchIcon: Boolean = true,
    val shouldShowSettingsIcon: Boolean = true,
    val shouldShowUpButtonIcon: Boolean = false,
    val shouldShowToolTar: Boolean = true,
)