package com.owusu.cryptosignalalert.alarm

import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import com.owusu.cryptosignalalert.CryptoSignalAlertApp
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.data.datasource.AppPreferences
import com.owusu.cryptosignalalert.domain.usecase.IsSirenEnabledUseCase
import java.util.*

class CryptoMediaPlayer(private val isSirenEnabledUseCase: IsSirenEnabledUseCase) {

    private lateinit var mp: MediaPlayer
    private var alarmTimer: Timer? = null
    private var stopAlarmTimerTask: StopAlarmTimerTask? = null
    private  var vibrate:VibrateUtil? = null
    @Volatile
    private var isVibrateRunning = false

    @Synchronized
    fun startAlarmSounds() {
        if (isSirenEnabledUseCase.execute()) {
            isVibrateRunning = true
            stopAlarmSounds()
            cancelTimerTask()
            alarmTimer = Timer()
            stopAlarmTimerTask = StopAlarmTimerTask()
            alarmTimer?.schedule(stopAlarmTimerTask, 60000L)
            playMediaFile()
            Thread(VibrateRunner()).start()
        }
    }

    @Synchronized
    fun stopAlarmSounds() {
        if (isSirenEnabledUseCase.execute()) {
            stopMediaFile()
            isVibrateRunning = false
        }
    }

    private inner class StopAlarmTimerTask : TimerTask() {
        override fun run() {
            stopAlarmSounds()
        }
    }

    private fun cancelTimerTask() {
        if (alarmTimer != null && stopAlarmTimerTask != null) {
            alarmTimer?.cancel()
            stopAlarmTimerTask?.cancel()
        }
    }

    private inner class VibrateRunner : Runnable {
        override fun run() {
            while (isVibrateRunning) {
                try {
                    Thread.sleep(2500)
                    vibrate?.vibrateForASecond()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun playMediaFile() {
        try {
            val context = CryptoSignalAlertApp.instance.applicationContext
            val mediaFileUri = Uri.parse(
                "android.resource://" + context.getPackageName()
                    .toString() + "/" + R.raw.the_purge_siren
            )
            mp = MediaPlayer()
            mp.setDataSource(context, mediaFileUri)
            mp.prepare()
            mp.setLooping(true)
            mp.start()
        } catch (e: Exception) {
            log("playMediaFile() error playing media file " + e.stackTrace.toString())
            e.printStackTrace()
        }
    }

    private fun stopMediaFile() {
        if (::mp.isInitialized) {
            mp.stop()
        }
        cancelTimerTask()
    }
    private fun log(msg: String) {
        Log.v("CryptoSignalService", msg)
    }
}