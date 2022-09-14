package com.owusu.cryptosignalalert.workmanager

import android.content.Context
import com.owusu.cryptosignalalert.alarm.CryptoAlarmManager
import com.owusu.cryptosignalalert.alarm.CryptoMediaPlayer
import com.owusu.cryptosignalalert.domain.models.PriceTargetDomain
import com.owusu.cryptosignalalert.domain.utils.CryptoDateUtils
import com.owusu.cryptosignalalert.notification.NotificationUtil
import org.koin.core.KoinComponent

class PriceNotificationHelper(
    private val notificationUtil: NotificationUtil,
    private val cryptoDateUtils: CryptoDateUtils,
    private val cryptoMediaPlayer: CryptoMediaPlayer
    ): KoinComponent {

    fun notifyUser(context: Context, updatedPriceTargets: List<PriceTargetDomain>): Boolean {

        if (updatedPriceTargets.isEmpty()) return false

        return try {
            notificationUtil.createNotificationChannel(context)
            notifyUserOfPriceTargetHit(updatedPriceTargets)
            createSound()
            true
        }
        catch (t: Throwable) {
            t.printStackTrace()
            false
        }
    }

    private fun notifyUserOfPriceTargetHit(priceTargets: List<PriceTargetDomain>) {
        priceTargets.forEach {
            val msg = it.name +"\nis now "+it.priceTargetDirection.toString().lowercase() + " "+ it.userPriceTarget
            notificationUtil.sendNewStandAloneNotification(msg)
        }
    }

    private fun createSound() {
        cryptoMediaPlayer.startAlarmSounds()
    }
}