package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.repository.AppPreferencesRepository

class IsSyncRunningUseCase(
    private val appPreferencesRepository: AppPreferencesRepository
) {

    fun execute(): Boolean {
        return appPreferencesRepository.isWorkManagerExecuting()
    }
}