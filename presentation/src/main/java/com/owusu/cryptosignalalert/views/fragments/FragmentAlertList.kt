package com.owusu.cryptosignalalert.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.data.datasource.db.PriceTargetDao
import com.owusu.cryptosignalalert.data.models.entity.PriceTargetEntity
import com.owusu.cryptosignalalert.domain.models.PriceTargetDirection
import com.owusu.cryptosignalalert.domain.utils.CryptoDateUtils
import com.owusu.cryptosignalalert.models.AlertListViewState
import com.owusu.cryptosignalalert.models.PriceTargetUI
import com.owusu.cryptosignalalert.notification.NotificationUtil
import com.owusu.cryptosignalalert.viewmodels.AlertListViewModel
import com.owusu.cryptosignalalert.views.adapters.AlertListAdapter
import com.owusu.cryptosignalalert.workmanager.Constants.DISPLAY_LATEST_DATA
import com.owusu.cryptosignalalert.workmanager.Constants.KEY_PRICE_TARGET_UPDATED_STATUS
import com.owusu.cryptosignalalert.workmanager.Constants.PRICE_TARGET_UPDATED
import com.owusu.cryptosignalalert.workmanager.Constants.SYNC_PRICES_WORKER_TAG
import com.owusu.cryptosignalalert.workmanager.WorkManagerStarter
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
    private lateinit var alertListAdapter: AlertListAdapter
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
        initAdapter()
    }

    private fun initAdapter() {
        // this creates a vertical layout Manager
        alertList.layoutManager = LinearLayoutManager(activity)
        alertListAdapter = AlertListAdapter()
        alertList.adapter = alertListAdapter
    }

    private fun initListeners() {
        clear_entries.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                repoForTesting.nukeTable()
            }
        }

        create_entries.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {

                val currentTime = cryptoDateUtils.convertDateToFormattedStringWithTime(Calendar.getInstance().timeInMillis)
                val list = arrayListOf<PriceTargetEntity>()
                list.add(getPriceTarget(
                    id = "bitcoin",
                    name = "Bitcoin",
                    symbol = "btc",
                    lastUpdated = currentTime,
                    userPriceTarget = 22000.0,
                    priceTargetDirection = PriceTargetDirection.ABOVE))

                list.add(getPriceTarget(
                    id = "bitcoin",
                    name = "Bitcoin",
                    symbol = "btc",
                    lastUpdated = currentTime,
                    userPriceTarget = 21300.0,
                    priceTargetDirection = PriceTargetDirection.BELOW))

                list.add(getPriceTarget(
                    id = "ethereum",
                    name = "Ethereum",
                    symbol = "eth",
                    lastUpdated = currentTime,
                    userPriceTarget = 1791.0,
                    priceTargetDirection = PriceTargetDirection.ABOVE))

                repoForTesting.insertPriceTargets(list)
            }
        }

        create_request.setOnClickListener {
            startOneTimeReq()
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
        //viewModel.loadAlertList()
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
            viewModel.workInfoLiveData.observe(viewLifecycleOwner) { workInfoList ->
                Log.v("My_Sync_FragAlertList", "START")

                if ((workInfoList != null) &&  workInfoList.isNotEmpty() &&(workInfoList.first().state == WorkInfo.State.RUNNING)) {
                    Log.v("My_Sync_FragAlertList", "show spinner")
                } else if ((workInfoList != null) &&  workInfoList.isNotEmpty() &&(workInfoList.first().state == WorkInfo.State.ENQUEUED)) {
                    val workInfo =workInfoList.first()
                    Log.v("My_Sync_FragAlertList", "state" + workInfo.state.toString())
                    val myOutputData = workInfo.outputData.getString(KEY_PRICE_TARGET_UPDATED_STATUS)
                    //if (myOutputData == DISPLAY_LATEST_DATA) { // only seems to work with one time req. maybe for chained workers?
                        Log.v("My_Sync_FragAlertList", DISPLAY_LATEST_DATA)
                        // When a sync has occurred, refresh the screen
                        viewModel.loadAlertList()
                  // }
                }
                Log.v("My_Sync_FragAlertList", "END")
            }

        }
    }

    private fun startOneTimeReq() {
        WorkManagerStarter.startPeriodicWorker(context = requireActivity().application, SYNC_PRICES_WORKER_TAG)
    }

    private fun displayData(priceTargets: List<PriceTargetUI>) {
        alertListAdapter.setData(priceTargets)
        alertListAdapter.notifyDataSetChanged()
    }

    private fun getPriceTarget(
        id: String,
        name: String,
        symbol: String,
        lastUpdated: String,
        userPriceTarget: Double,
        priceTargetDirection: PriceTargetDirection
    ): PriceTargetEntity {
        return PriceTargetEntity(
            localPrimeId = 0,
            id = id,
            symbol = symbol,
            name = name,
            image = null,
            currentPrice = 0.0,
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
            lastUpdated = lastUpdated,
            userPriceTarget = userPriceTarget,
            hasPriceTargetBeenHit = false,
            hasUserBeenAlerted = false,
            priceTargetDirection = priceTargetDirection
        )
    }
}