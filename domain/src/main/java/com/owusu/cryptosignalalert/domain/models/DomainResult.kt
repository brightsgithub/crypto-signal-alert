package com.owusu.cryptosignalalert.domain.models

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class DomainResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : DomainResult<T>()
    data class Error(val domainError: DomainError) : DomainResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "DomainError[domainError=$domainError]"
        }
    }

    sealed interface DomainError
}