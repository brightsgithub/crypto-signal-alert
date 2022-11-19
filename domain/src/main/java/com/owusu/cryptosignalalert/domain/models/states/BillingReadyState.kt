package com.owusu.cryptosignalalert.domain.models.states

// We need to know if we are able to query billing to avoid a race condition.
sealed class BillingReadyState {
    object Ready: BillingReadyState()
    object NotReady: BillingReadyState()
    object NoSkusExist: BillingReadyState()
}
