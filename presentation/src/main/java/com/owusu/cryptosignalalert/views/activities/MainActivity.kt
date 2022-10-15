package com.owusu.cryptosignalalert.views.activities

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
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

// https://developer.android.com/codelabs/jetpack-compose-state?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fjetpack-compose-for-android-developers-1%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fjetpack-compose-state#9
@Composable
fun Coins(lazyPagingItems: LazyPagingItems<CoinUI>) {
    // Remember our own LazyListState
    val listState = lazyPagingItems.rememberLazyListStateWorkAround()

    // My temp solution
    val refresh = lazyPagingItems.loadState.refresh
    if (lazyPagingItems.itemCount == 0 && refresh is LoadState.NotLoading ) return //skip dummy state, waiting next compose

    LazyColumn(
        state = listState,
        modifier = Modifier.padding(vertical = 4.dp),
    ) {
        items(lazyPagingItems) { coin ->
            Coin2(coin)
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
fun <T : Any> LazyPagingItems<T>.rememberLazyListStateWorkAround(): LazyListState {
    // After recreation, LazyPagingItems first return 0 items, then the cached items.
    // This behavior/issue is resetting the LazyListState scroll position.
    // Below is a workaround. More info: https://issuetracker.google.com/issues/177245496.
    return when (itemCount) {
        // Return a different LazyListState instance.
        0 -> remember(this) { LazyListState(0, 0) }
        // Return rememberLazyListState (normal case).
        else -> androidx.compose.foundation.lazy.rememberLazyListState()
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
private fun Coin2(coin: CoinUI?) {

    val context = LocalContext.current
    val expanded = rememberSaveable { mutableStateOf(false) }

    val extraPadding = if (expanded.value) 48.dp else 0.dp

    Surface(
        color = colorResource(id = R.color.dark_coin_row),
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {

        ConstraintLayout(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()) {
            // Create references for the composables to constrain
            val (
                marketRank,
                coinImage,
                currentPriceLabel,
                marketCapLabel,
                currentPrice,
                marketCap,
                coinName,
                priceChangePercentage24h,
                marketCapChangePercentage24h,
                _24HrChangeLabel,
                alertImage
            ) = createRefs()

            Text(text = coin!!.marketCapRank.toString(),
                modifier = Modifier.constrainAs(marketRank) {
                top.linkTo(parent.top, margin = 4.dp)
            }, fontSize = 12.sp)

            Image(
                painter = rememberImagePainter(coin?.image),
                contentDescription = stringResource(R.string.image_coin_content_desc),
                modifier = Modifier
                    // Set image size to 40 dp
                    .size(30.dp)
                    // Clip image to be shaped as a circle
                    .clip(CircleShape)
                    .constrainAs(coinImage) {
                        start.linkTo(marketRank.end, margin = 16.dp)
                    }
            )

            Text(text = coin.name!! + " ("+coin.symbol?.uppercase()+")", modifier = Modifier
                .constrainAs(coinName) {
                start.linkTo(marketCapLabel.end, margin = 16.dp)
            }, fontWeight = FontWeight.Bold)

            Image(
                painter = rememberImagePainter(if (coin.hasPriceTarget) R.drawable.ic_alart_set else R.drawable.ic_alert_not_set),
                contentDescription = stringResource(R.string.alert_icon),
                modifier = Modifier
                    .size(25.dp)
                    .constrainAs(alertImage){
                        end.linkTo(parent.end, margin = 4.dp)
                        top.linkTo(coinName.top)
                    }
                    .clickable {
                        if (!coin.hasPriceTarget) {
                            context.startActivity(PriceTargetEntryActivity.getIntent(context, coin))
                        } else {
                            // TODO - Open PriceTargetListScreen
                        }
                    }
            )

            Text(text = "Price:", modifier = Modifier.constrainAs(currentPriceLabel) {
                start.linkTo(coinImage.start)
                top.linkTo(coinImage.bottom, margin = 16.dp)
            })

            Text(text = "Market Cap:", modifier = Modifier.constrainAs(marketCapLabel) {
                start.linkTo(currentPriceLabel.start)
                top.linkTo(currentPriceLabel.bottom)
            }, fontSize = 14.sp)

            Text(text = coin.currentPriceStr!!, modifier = Modifier.constrainAs(currentPrice) {
                start.linkTo(coinName.start)
                top.linkTo(currentPriceLabel.top)
            }, fontWeight = FontWeight.Bold)

            Text(text = coin.marketCapStr!!, modifier = Modifier.constrainAs(marketCap) {
                start.linkTo(coinName.start)
                top.linkTo(marketCapLabel.top)
            }, fontSize = 14.sp)

            Text(text = "24hr", modifier = Modifier.constrainAs(_24HrChangeLabel) {
                end.linkTo(priceChangePercentage24h.start, margin = 4.dp)
                top.linkTo(currentPrice.top)
            }, fontSize = 12.sp)

            Text(text = coin.priceChangePercentage24hStr!!, modifier = Modifier.constrainAs(priceChangePercentage24h) {
                end.linkTo(parent.end, margin = 4.dp)
                top.linkTo(_24HrChangeLabel.top)
            },
            color = getPercentageColor(coin.is24HrPriceChangePositive), fontSize = 12.sp)
        }
    }
}

@Composable
private fun getPercentageColor(is24HrPriceChangePositive: Boolean) : Color {
    return if (is24HrPriceChangePositive) {
        colorResource(R.color.percentage_gain_green)
    } else {
        colorResource(R.color.red)
    }
}


@Composable
private fun Coin(coin: CoinUI?) {

    val expanded = remember { mutableStateOf(false) }

    val extraPadding = if (expanded.value) 48.dp else 0.dp

    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 1.dp, horizontal = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
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
                modifier = Modifier.padding(end = 14.dp),
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
        Coin2(
            CoinUI(
                id = "bitcoin",
                name = "Bitcoin",
                currentPriceStr = "19000.0",
                marketCapStr = "20000000.0",
                marketCapRank = 1,
                image = "",
                priceChangePercentage24hStr = "25.0",
                marketCapChangePercentage24h = 10.0,
                is24HrPriceChangePositive = true,
                hasPriceTarget = true
            )
        )
    }
}