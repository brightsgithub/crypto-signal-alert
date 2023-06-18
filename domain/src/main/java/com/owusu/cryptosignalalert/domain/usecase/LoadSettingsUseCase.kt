package com.owusu.cryptosignalalert.domain.usecase

import com.owusu.cryptosignalalert.domain.models.SettingDomain
import com.owusu.cryptosignalalert.domain.repository.SettingsRepository

class LoadSettingsUseCase(
    private val settingsRepository: SettingsRepository): SuspendedUseCaseUnit<List<SettingDomain>> {

    override suspend fun invoke(): List<SettingDomain> {
        return settingsRepository.getSettings()
    }
}