package com.owusu.cryptosignalalert.domain.models.states

sealed class StartUpBillingState {
    object ReadyToListen: StartUpBillingState()
    object NotReady: StartUpBillingState()
    object Finished: StartUpBillingState()
}