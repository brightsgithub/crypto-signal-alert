package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.repository.AppPreferencesRepository

class IsSirenEnabledUseCase(private val appPreferencesRepository: AppPreferencesRepository) {
    fun execute(): Boolean {
        return appPreferencesRepository.isSirenEnabled()
    }
}