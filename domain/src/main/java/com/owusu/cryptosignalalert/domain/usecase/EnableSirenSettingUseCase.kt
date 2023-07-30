package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.repository.AppPreferencesRepository

class EnableSirenSettingUseCase(private val appPreferencesRepository: AppPreferencesRepository) {
    fun execute() {
        appPreferencesRepository.allowSirenSound()
    }
}