package com.owusu.cryptosignalalert.data.datasource.settings

import android.content.Context

class SettingsUtils {

    fun getAppVersionName(context: Context): String {
        var versionName = ""
        try {
            versionName = context.getPackageManager()
                .getPackageInfo(context.getPackageName(), 0).versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return versionName
    }

    fun getAppVersionCode(context: Context): Int {
        var versionCode = 0
        try {
            versionCode = context.getPackageManager()
                .getPackageInfo(context.getPackageName(), 0).versionCode
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return versionCode
    }
}