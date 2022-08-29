package com.owusu.cryptosignalalert.domain.models

sealed class SomePriceTargetError : DomainResult.DomainError {
    object SomeErrorWhenGettingTargets: SomePriceTargetError()
}
