package com.owusu.cryptosignalalert.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.owusu.cryptosignalalert.alarm.CryptoMediaPlayer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


const val ACTION_ALERT_STOP = "com.owusu.cryptosignalalert.broadcastreceivers.ACTION_ALERT_STOP"

class AlertStopReceiver : BroadcastReceiver(), KoinComponent  {

    private val cryptoMediaPlayer: CryptoMediaPlayer by inject()

    init {
        Log.v("CryptoBroadcastReceiver", "AlertStopReceiver Created")
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let { intent ->
            intent.action?.let { action ->
                if (action == ACTION_ALERT_STOP) {
                    context?.let {
                        stopAlertSounds()
                    }
                }
            }
        }
    }

    private fun stopAlertSounds() {
        Log.v("CryptoBroadcastReceiver", "AlertStopReceiver.stopAlarmSounds() called")
        cryptoMediaPlayer.stopAlarmSounds()
    }
}