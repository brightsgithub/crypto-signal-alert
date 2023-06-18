package com.owusu.cryptosignalalert.data.datasource.settings

import com.owusu.cryptosignalalert.domain.models.SettingDomain

interface SettingsDataSource {
    suspend fun getSettings(): List<SettingDomain>
}