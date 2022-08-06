package com.owusu.cryptosignalalert.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.alarm.CryptoAlarmManager
import com.owusu.cryptosignalalert.notification.NotificationUtil
import com.owusu.cryptosignalalert.service.CryptoSignalAlertService
import com.owusu.cryptosignalalert.util.AppNavigationUtil
import com.owusu.cryptosignalalert.utils.DateUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class MainActivity : AppCompatActivity(), KoinComponent {

    private val notificationUtil: NotificationUtil by inject()
    private val dateUtils: DateUtils by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar()
        initViews()
        navigateToFragmentAlertList()
    }

    private fun initViews() {
        initListeners()
    }

    private fun initListeners() {
        start_service.setOnClickListener {
            CryptoSignalAlertService.startAppForegroundService(this.applicationContext, "From Main Activity")
        }

        stop_service.setOnClickListener {
            CryptoSignalAlertService.stopAlertService(this.applicationContext)
        }

        create_notification.setOnClickListener {
            notificationUtil.sendNewStandAloneNotification("Created at "+ dateUtils.convertDateToFormattedStringWithTime(
                Calendar.getInstance().timeInMillis))
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(app_toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        //supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun navigateToFragmentAlertList() {
        AppNavigationUtil.navigateToFragmentAlertList(this)
    }
}