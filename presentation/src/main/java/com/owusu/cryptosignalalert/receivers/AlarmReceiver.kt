package com.owusu.cryptosignalalert.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.owusu.cryptosignalalert.alarm.CryptoAlarmManager
import com.owusu.cryptosignalalert.alarm.CryptoAlarmManager.Companion.INTENT_ACTION_START_ALARM_LISTENER
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

//import org.koin.core.KoinComponent
//import org.koin.core.inject

/**
 * Created by Bright Owusu-Amankwaa on 24/01/21.
 */
class AlarmReceiver : BroadcastReceiver() , KoinComponent {

    private val cryptoAlarmManager: CryptoAlarmManager by inject()

    init {
        Log.v("CryptoSignalService", "AlarmReceiver instance created ")
    }

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        intent.let {
            it.action?.let { action ->
                Log.v("CryptoSignalService", "CryptoAlertAlarm.AlarmReceiver called with " + action)
                if (action == INTENT_ACTION_START_ALARM_LISTENER) {
                    //job?.cancel()
                    // 1) yes this needs to be done in the background, but it needs to call a class
                    //    that is a singleton that holds and keeps a reference to a JOB.
                    //    This receiver each time it is called, creates a new instance, so do not try to keep
                    //    a job reference here and manage it. This is a fire and forget class
                    cryptoAlarmManager.onReadyToStartBackgroundWork(context)
                }
            }
        }
    }
}