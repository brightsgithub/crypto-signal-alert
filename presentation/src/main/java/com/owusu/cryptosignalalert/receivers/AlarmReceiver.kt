package com.owusu.cryptosignalalert.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.owusu.cryptosignalalert.alarm.CryptoAlarmManager
import com.owusu.cryptosignalalert.alarm.CryptoAlarmManager.INTENT_ACTION_START_ALARM_LISTENER
import com.owusu.cryptosignalalert.alarm.CryptoAlarmManager.INTENT_ACTION_STOP_ALARM_LISTENER
import org.koin.core.KoinComponent

/**
 * Created by Bright Owusu-Amankwaa on 24/01/21.
 */
class AlarmReceiver : BroadcastReceiver() , KoinComponent {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        Log.v("CryptoSignalService", "CryptoAlertAlarm.AlarmReceiver called")
        intent.let { intent ->
            intent.action?.let { action ->

                Log.v("CryptoSignalService", "CryptoAlertAlarm.AlarmReceiver called with " + action)

                if (action == INTENT_ACTION_START_ALARM_LISTENER) {
                    startAlarmAgain()
                } else if (action == INTENT_ACTION_STOP_ALARM_LISTENER) {
                    stopAlarm()
                }
            }
        }
    }

    private fun startAlarmAgain() {
        CryptoAlarmManager.startAlarm()
    }

    private fun stopAlarm() {
        CryptoAlarmManager.stopAlarm()
    }
}