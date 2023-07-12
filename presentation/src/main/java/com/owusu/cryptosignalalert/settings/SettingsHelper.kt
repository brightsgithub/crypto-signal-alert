package com.owusu.cryptosignalalert.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.domain.models.ScreenProxy

class SettingsHelper {

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

    fun shareApp(activity: Context) {
        try {
            activity as Activity
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name))
            var sAux = "\n" + activity.getString(R.string.tell_a_friend_msg_body) + "\n\n"
            sAux += activity.getString(R.string.app_play_store_url) + " \n\n"
            i.putExtra(Intent.EXTRA_TEXT, sAux)
            activity.startActivity(Intent.createChooser(i, "choose one"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    fun openAppOnGooglePlayStore(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse("market://details?id=com.owusu.cryptosignalalert"))
        context.startActivity(intent)
    }
}