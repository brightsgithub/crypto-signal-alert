package com.owusu.cryptosignalalert.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.util.AppNavigationUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.core.KoinComponent

class MainActivity : AppCompatActivity(), KoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar()
        navigateToFragmentAlertList()
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