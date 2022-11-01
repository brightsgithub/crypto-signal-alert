package com.owusu.cryptosignalalert.views.helpers

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.work.WorkInfo
import com.owusu.cryptosignalalert.viewmodels.AlertListViewModel
import com.owusu.cryptosignalalert.workmanager.Constants
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PriceTargetsHelper {
    private var workManagerJob: Job? = null

    fun observeWorkManagerStatus(activity: ComponentActivity, viewModel: AlertListViewModel) {
        workManagerJob = activity.lifecycleScope.launch {
            viewModel.workInfoLiveData.observe(activity) { workInfoList ->
                Log.v("My_Sync_FragAlertList", "START")

                if ((workInfoList != null) &&  workInfoList.isNotEmpty() &&(workInfoList.first().state == WorkInfo.State.RUNNING)) {
                    Log.v("My_Sync_FragAlertList", "show spinner")
                } else if ((workInfoList != null) &&  workInfoList.isNotEmpty() &&(workInfoList.first().state == WorkInfo.State.ENQUEUED)) {
                    val workInfo =workInfoList.first()
                    Log.v("My_Sync_FragAlertList", "state" + workInfo.state.toString())
                    val myOutputData = workInfo.outputData.getString(Constants.KEY_PRICE_TARGET_UPDATED_STATUS)
                    //if (myOutputData == DISPLAY_LATEST_DATA) { // only seems to work with one time req. maybe for chained workers?
                    Log.v("My_Sync_FragAlertList", Constants.DISPLAY_LATEST_DATA)
                    // When a sync has occurred, refresh the screen
                    viewModel.loadAlertList()
                    // }
                }
                Log.v("My_Sync_FragAlertList", "END")
            }
        }
    }

    fun stopObservingWorkManagerStatus() {
        workManagerJob?.cancel()
    }
}