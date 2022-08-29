package com.owusu.cryptosignalalert.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.work.WorkInfo
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.models.AlertListUIWrapper
import com.owusu.cryptosignalalert.models.AlertListViewState
import com.owusu.cryptosignalalert.models.PriceTargetUI
import com.owusu.cryptosignalalert.viewmodels.AlertListViewModel
import com.owusu.cryptosignalalert.workmanager.Constants
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentAlertList: Fragment() {

    private lateinit var viewStateJob: Job
    private val viewModel: AlertListViewModel by viewModel()

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
        viewModel.workInfoLiveData.observe(this) { workInfo ->
            if ((workInfo != null) &&                (workInfo.state == WorkInfo.State.SUCCEEDED)
            ) {
                val myOutputData = workInfo.outputData.getString(Constants.KEY_PRICE_TARGET_UPDATED)
                if (myOutputData != null) {
                    viewModel.loadAlertList()
                }
            }
        }
    }

    private fun displayData(priceTargets: List<PriceTargetUI>) {
        for (item in priceTargets) {
            Log.v("FragmentAlertList", item.toString())
        }
    }
}