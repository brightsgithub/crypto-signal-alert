package com.owusu.cryptosignalalert.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.work.WorkInfo
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.data.datasource.db.PriceTargetDao
import com.owusu.cryptosignalalert.data.models.entity.PriceTargetEntity
import com.owusu.cryptosignalalert.domain.models.PriceTargetDirection
import com.owusu.cryptosignalalert.domain.utils.CryptoDateUtils
import com.owusu.cryptosignalalert.models.AlertListViewState
import com.owusu.cryptosignalalert.models.PriceTargetUI
import com.owusu.cryptosignalalert.notification.NotificationUtil
import com.owusu.cryptosignalalert.viewmodels.AlertListViewModel
import com.owusu.cryptosignalalert.workmanager.Constants.DISPLAY_LATEST_DATA
import com.owusu.cryptosignalalert.workmanager.Constants.KEY_PRICE_TARGET_UPDATED_STATUS
import com.owusu.cryptosignalalert.workmanager.Constants.PRICE_TARGET_UPDATED
import kotlinx.android.synthetic.main.fragment_alert_list.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

import java.util.*

class FragmentAlertList: Fragment(), KoinComponent {

    private lateinit var viewStateJob: Job
    private lateinit var workManagerJob: Job
    private val viewModel: AlertListViewModel by viewModel()
    private val notificationUtil: NotificationUtil by inject()
    private val cryptoDateUtils: CryptoDateUtils by inject()
    private val repoForTesting: PriceTargetDao by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_alert_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        clear_entries.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                repoForTesting.nukeTable()
            }
        }

        create_entries.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val numOfCoins = 1
                val hasPriceTargetBeenHit = false
                val hasUserBeenAlerted = false
                //val userPriceTarget = 22000.0
                val userPriceTarget = 21000.0
                val priceTargetDirection = PriceTargetDirection.ABOVE
                val coinsToBeAddedToBb = createPriceTargets(numOfCoins, hasPriceTargetBeenHit, hasUserBeenAlerted, userPriceTarget, priceTargetDirection)
                repoForTesting.insertPriceTargets(coinsToBeAddedToBb)
            }
        }

        create_notification.setOnClickListener {
            notificationUtil.sendNewStandAloneNotification("Created at "+ cryptoDateUtils.convertDateToFormattedStringWithTime(
                Calendar.getInstance().timeInMillis))
        }
    }

    override fun onStart() {
        super.onStart()
        registerViewStates()
    }

    override fun onStop() {
        super.onStop()
        unRegisterViewStates()
    }

    private fun registerViewStates() {
        observeViewState()
        observeWorkManagerStatus()
    }

    private fun unRegisterViewStates() {
        viewStateJob.cancel()
        workManagerJob.cancel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAlertList()
    }

    private fun observeViewState() {
        viewStateJob = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.viewState.collect { state ->
                when(state) {
                    is AlertListViewState.AlertListDataFailure -> {}
                    is AlertListViewState.AlertListDataSuccess -> { displayData(state.priceTargets) }
                    is AlertListViewState.HideLoadingState -> {}
                    is AlertListViewState.ShowLoadingState -> {}
                }
            }
        }
    }

    private fun observeWorkManagerStatus() {
        workManagerJob = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.workInfoLiveData.observe(viewLifecycleOwner) { workInfo ->
                if ((workInfo != null) && (workInfo.state == WorkInfo.State.ENQUEUED)) {
                    Log.v("FragmentAlertList", "state" + workInfo.state.toString())
                    val myOutputData = workInfo.outputData.getString(KEY_PRICE_TARGET_UPDATED_STATUS)
                    //if (myOutputData == DISPLAY_LATEST_DATA) {
                        Log.v("FragmentAlertList", "DISPLAY_LATEST_DATA")
                        // When a sync has occurred, refresh the screen
                        viewModel.loadAlertList()
                    //}
                }
            }
        }
    }

    private fun displayData(priceTargets: List<PriceTargetUI>) {
        for (item in priceTargets) {
            coin_name.text = "Coin name: " + item.name
            last_updated.text = "Last updated: " + item.lastUpdated
            has_target_been_hit.text = "Has target been hit: " + item.hasPriceTargetBeenHit.toString()
            current_price.text = "Current price: " + item.currentPrice.toString()
            price_direction.text = "Price direction: " + item.priceTargetDirection.toString()
            price_target.text = "Price target: " + item.userPriceTarget
        }
    }

    private fun createPriceTargets(
        size: Int,
        hasPriceTargetBeenHit: Boolean,
        hasUserBeenAlerted: Boolean,
        userPriceTarget: Double,
        priceTargetDirection: PriceTargetDirection) : List<PriceTargetEntity>{

        val list = arrayListOf<PriceTargetEntity>()
        for (i in 1.. size) {
            list.add(getPriceTarget(i, hasPriceTargetBeenHit, hasUserBeenAlerted, userPriceTarget, priceTargetDirection))
        }
        return list
    }

    private fun getPriceTarget(
        index: Int,
        hasPriceTargetBeenHit: Boolean,
        hasUserBeenAlerted: Boolean,
        userPriceTarget: Double,
        priceTargetDirection: PriceTargetDirection
    ): PriceTargetEntity {
        return PriceTargetEntity(
            id = "bitcoin",
            symbol = "btc",
            name = "Bitcoin",
            image = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579",
            currentPrice = 21518.0,
            marketCap = 411096596530.0,
            marketCapRank = 1.0,
            fullyDilutedValuation = 452245724314L,
            totalVolume = 48963778515.0,
            high24h = 22007.0,
            low24h = 21251.0,
            priceChange24h = -16.19950226412402,
            priceChangePercentage24h = -0.07523,
            marketCapChange24h = -2334324524.703125,
            marketCapChangePercentage24h = -0.56462,
            circulatingSupply = 19089243.0,
            totalSupply = 21000000.0,
            maxSupply = 21000000.0,
            ath = 69045.0,
            athChangePercentage = -68.88983,
            athDate = "2021-11-10T14:24:11.849Z",
            atl = 67.81,
            atlChangePercentage = 31577.13346,
            atlDate = "2013-07-06T00:00:00.000Z",
            lastUpdated = "2022-07-09T12:31:40.339Z",
            userPriceTarget = userPriceTarget,
            hasPriceTargetBeenHit = hasPriceTargetBeenHit,
            hasUserBeenAlerted = hasUserBeenAlerted,
            priceTargetDirection = priceTargetDirection
        )
    }
}