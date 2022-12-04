package com.owusu.cryptosignalalert.domain.models.states

sealed class NewPurchasesState {
    object EmptyInitialState: NewPurchasesState()
    data class BundleBuyAllPurchased(val sku: String): NewPurchasesState()
    data class UnlimitedAlertsPurchased(val sku: String): NewPurchasesState()
    data class RemoveAdsPurchased(val sku: String): NewPurchasesState()
}
