package com.owusu.cryptosignalalert.domain.repository

import com.owusu.cryptosignalalert.domain.models.SettingDomain

interface SettingsRepository {

    suspend fun getSettings(): List<SettingDomain>
}