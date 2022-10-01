package com.owusu.cryptosignalalert.views.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import androidx.work.WorkInfo
import coil.compose.rememberImagePainter
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.data.models.api.CoinAPI
import com.owusu.cryptosignalalert.domain.models.CoinDomain
import com.owusu.cryptosignalalert.models.CoinUI
import com.owusu.cryptosignalalert.models.CoinsListUiState
import com.owusu.cryptosignalalert.viewmodels.AlertListViewModel
import com.owusu.cryptosignalalert.viewmodels.CoinsListViewModel
import com.owusu.cryptosignalalert.views.theme.CryptoSignalAlertTheme
import com.owusu.cryptosignalalert.workmanager.Constants.DISPLAY_LATEST_DATA
import com.owusu.cryptosignalalert.workmanager.Constants.KEY_PRICE_TARGET_UPDATED_STATUS
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
//import org.koin.core.KoinComponent
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.getViewModel
import org.koin.core.component.KoinComponent

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
        //viewStateJob.cancel()
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

    // https://developer.android.com/jetpack/compose/libraries#streams
    // https://insert-koin.io/docs/reference/koin-android/compose/
    val viewModel = getViewModel<CoinsListViewModel>()
//    viewModel.loadCoinsList(page = 1, recordsPerPage = 100)
//
//    viewModel.viewState.collectAsState(initial = CoinsListUiState()).value.let {
//
//        // A surface container using the 'background' color from the theme
//        Surface(color = MaterialTheme.colors.background) {
//            Coins(it.coins)
//        }
//    }
//

    val lazyMembers = viewModel.coinsListFlow.collectAsLazyPagingItems()

    Surface(color = MaterialTheme.colors.background) {
        Coins(lazyMembers)
    }
}

@Composable
fun Coins(lazyPagingItems: LazyPagingItems<CoinUI>) {
    val listState = rememberLazyListState()

    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(lazyPagingItems) { coin ->
            Coin(coin)
        }

        lazyPagingItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        loading(boxModifier = Modifier.fillParentMaxSize())
                    }
                }

                loadState.append is LoadState.Loading -> {
                    item {
                        loading()
                    }
                }

                loadState.prepend is LoadState.Loading -> {
                    item {
                        loading()
                    }
                }
            }
        }
    }
}

@Composable
private fun loading(boxModifier: Modifier? = null) {

    val modifier = boxModifier ?: Modifier
        .fillMaxWidth()

    Box(modifier = modifier.padding(16.dp)) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun Coin(coin: CoinUI?) {

    val expanded = remember { mutableStateOf(false) }

    val extraPadding = if (expanded.value) 48.dp else 0.dp

    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .align(Alignment.Top)
            ) {
                Text(text = coin!!.marketCapRank.toString())
            }

            Column(modifier = Modifier
                .padding(end = 16.dp)
                .padding(bottom = extraPadding),
            ) {
                Image(
                    painter = rememberImagePainter(coin?.image),
                    contentDescription = stringResource(R.string.image_coin_content_desc),
                    modifier = Modifier
                        // Set image size to 40 dp
                        .size(40.dp)
                        // Clip image to be shaped as a circle
                        .clip(CircleShape)
                )
            }
            Column(modifier = Modifier
                .weight(1f)
                .padding(bottom = extraPadding)
            ) {
                Text(text = coin!!.name!!, fontWeight = FontWeight.Bold)
                Text(text = coin.currentPriceStr!!)
                Text(text = coin.marketCapStr!!)
            }
            OutlinedButton(
                onClick = { expanded.value = !expanded.value }
            ) {
                Text(if (expanded.value) "Show less" else "Show more")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CryptoSignalAlertTheme {
        Coin(
            CoinUI(
                id = "bitcoin",
                name = "Bitcoin",
                currentPrice = 19000.0,
                marketCap = 2000.0,
                marketCapRank = 1
            )
        )
    }
}