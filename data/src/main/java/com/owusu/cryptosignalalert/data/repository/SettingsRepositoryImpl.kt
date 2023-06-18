package com.owusu.cryptosignalalert.data.repository

import com.owusu.cryptosignalalert.data.datasource.settings.SettingsDataSource
import com.owusu.cryptosignalalert.domain.models.SettingDomain
import com.owusu.cryptosignalalert.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val settingsDataSource: SettingsDataSource
): SettingsRepository {

    override suspend fun getSettings(): List<SettingDomain> {
        return settingsDataSource.getSettings()
    }
}