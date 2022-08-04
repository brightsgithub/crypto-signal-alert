package com.owusu.cryptosignalalert.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.owusu.cryptosignalalert.service.CryptoSignalAlertService

/**
 * Created by Bright Owusu-Amankwaa on 03/02/2019.
 */
class BootCompletedReceiver : BroadcastReceiver(){

    override fun onReceive(context: Context, p1: Intent?) {
        startService(context)
    }

    private fun startService(context: Context) {
        CryptoSignalAlertService.startAppForegroundService(context, "Crypto Signal Alert is enabled")
    }
}