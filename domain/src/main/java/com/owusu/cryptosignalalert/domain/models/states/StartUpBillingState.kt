package com.owusu.cryptosignalalert.domain.models.states

sealed class StartUpBillingState {
    object NotReady: StartUpBillingState()
    object Finished: StartUpBillingState()
}