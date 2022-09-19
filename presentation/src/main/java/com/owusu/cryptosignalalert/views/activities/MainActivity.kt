package com.owusu.cryptosignalalert.views.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.work.WorkInfo
import com.owusu.cryptosignalalert.viewmodels.AlertListViewModel
import com.owusu.cryptosignalalert.views.theme.CryptoSignalAlertTheme
import com.owusu.cryptosignalalert.workmanager.Constants.DISPLAY_LATEST_DATA
import com.owusu.cryptosignalalert.workmanager.Constants.KEY_PRICE_TARGET_UPDATED_STATUS
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity(), KoinComponent {

    private val viewModel: AlertListViewModel by viewModel()
    private lateinit var viewStateJob: Job
    private lateinit var workManagerJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoSignalAlertTheme {
                MyApp()
            }
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
        //observeViewState()
        observeWorkManagerStatus()
    }

    private fun unRegisterViewStates() {
        viewStateJob.cancel()
        workManagerJob.cancel()
    }

    private fun observeWorkManagerStatus() {
        workManagerJob = lifecycleScope.launch {
            viewModel.workInfoLiveData.observe(this@MainActivity) { workInfoList ->
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
}

@Composable
private fun MyApp() {
    // A surface container using the 'background' color from the theme
    Surface(color = MaterialTheme.colors.background) {
        Greeting("Android")
    }
}

@Composable
fun Greeting(name: String) {
    Surface(color = MaterialTheme.colors.primary) {
        Text(text = "Hello $name!", modifier = Modifier.padding(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CryptoSignalAlertTheme {
        Greeting("Android")
    }
}