package com.owusu.cryptosignalalert.alarm

import android.content.Context
import android.os.Vibrator

/**
 * Created by brightowusu-amankwaa on 22/12/2015.
 */
class VibrateUtil private constructor (val v: Vibrator) {

    companion object {
        /**
         * Holds a reference to this class.
         */
        private var singleton: VibrateUtil?= null

        /**
         * Creates a single instance of this class.
         */
        fun initialize(pContext: Context) : VibrateUtil {
            if(singleton == null) {
                val v = pContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                singleton = VibrateUtil(v)
            }
            return singleton!!
        }
    }

    /**
     * Alert the user via vibration.
     * */
    fun shortVibrate() {
        val time = 500
        vibrate(time.toLong())
    }

    fun vibrateToGetUserAttention() {
        vibrate(100)
    }

    fun vibrateForASecond() {
        vibrate(1000)
    }

    fun vibrate(duration: Long) {
        try {
            v.vibrate(duration)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Dot dot.
     * @param context the context.
     */
    fun doubleVibrate(context: Context) {
        val vibrate = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val dot = 200      // Length of a Morse Code "dot" in milliseconds
        val shortGap = 200    // Length of Gap Between dots/dashes
        val pattern = longArrayOf(0, // Start immediately
                dot.toLong(), shortGap.toLong(), dot.toLong())
        vibrate.vibrate(pattern, -1)
    }
}