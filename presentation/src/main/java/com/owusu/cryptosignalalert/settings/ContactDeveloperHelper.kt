package com.owusu.cryptosignalalert.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import com.owusu.cryptosignalalert.R

class ContactDeveloperHelper(private val settingsHelper: SettingsHelper) {

    fun provideFeedback(activity: Activity) {
        val messageTemplate = getMessageTemplate(activity)
        try {
            val i = getContactDeveloperIntent(activity, messageTemplate)
            activity.startActivityForResult(
                Intent.createChooser(i, activity.getString(R.string.contact_wear_speaker)),
                20231
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun getMessageTemplate(ctx: Context): String? {
        val sb = StringBuilder()
        sb.append(ctx.getString(R.string.feedback_header_1)).append("\n")
        sb.append(ctx.getString(R.string.device_type)).append(" ")
            .append(ctx.getString(R.string.android)).append("\n")
        sb.append(ctx.getString(R.string.the_android_version)).append(" ")
            .append(Build.VERSION.SDK_INT).append("\n")
        sb.append(ctx.getString(R.string.the_app_version)).append(" ")
            .append(settingsHelper.getAppVersionName(ctx)).append("\n")
        sb.append(ctx.getString(R.string.device_model)).append(" ").append(Build.MODEL).append("\n")
        sb.append(ctx.getString(R.string.manufacturer)).append(" ").append(Build.MANUFACTURER)
            .append("\n")
        sb.append(ctx.getString(R.string.feedback_header_2)).append("\n\n")
        return sb.toString()
    }

    fun getContactDeveloperIntent(ctx: Context, message: String?): Intent {
        val i = Intent(
            Intent.ACTION_SENDTO,
            Uri.parse("mailto:" + ctx.getString(R.string.contact_title_detail))
        )
        i.putExtra(Intent.EXTRA_SUBJECT, ctx.getString(R.string.email_subject))
        i.putExtra(Intent.EXTRA_TEXT, message)
        return i
    }
}