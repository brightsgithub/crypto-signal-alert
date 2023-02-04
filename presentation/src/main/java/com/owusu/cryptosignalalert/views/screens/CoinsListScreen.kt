package com.owusu.cryptosignalalert.views.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.models.CoinUI
import com.owusu.cryptosignalalert.viewmodels.CoinsListViewModel
import com.owusu.cryptosignalalert.viewmodels.SharedViewModel
import com.owusu.cryptosignalalert.views.theme.CryptoSignalAlertTheme
import org.koin.androidx.compose.getViewModel

// https://developer.android.com/codelabs/jetpack-compose-state?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fjetpack-compose-for-android-developers-1%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fjetpack-compose-state#9
@Composable
fun CoinsListScreen(sharedViewModel: SharedViewModel, navigateToPriceTargetEntryScreen:(coin: CoinUI) -> Unit) {
    Surface(color = MaterialTheme.colors.background) {
        ShowCoinsList(sharedViewModel, navigateToPriceTargetEntryScreen)
    }
}

@Composable
private fun ShowCoinsList(sharedViewModel: SharedViewModel, navigateToPriceTargetEntryScreen:(coin: CoinUI) -> Unit) {
    val viewModel = getViewModel<CoinsListViewModel>()
    val lazyPagingItems = viewModel.coinsListFlow.collectAsLazyPagingItems()

    // Remember our own LazyListState
    val listState = lazyPagingItems.rememberLazyListStateWorkAround()
    // https://dailydevsblog.com/troubleshoot/resolved-paging-3-list-auto-refresh-on-navigation-back-in-jetpack-compose-navigation-46232/
    val isRefreshing = rememberSwipeRefreshState(
        isRefreshing = lazyPagingItems.loadState.refresh is LoadState.Loading,
    )

    // My temp solution
    val refresh = lazyPagingItems.loadState.refresh
    if (lazyPagingItems.itemCount == 0 && refresh is LoadState.NotLoading) return //skip dummy state, waiting next compose

    SwipeRefresh(
        state = isRefreshing,
        onRefresh = { lazyPagingItems.refresh() },
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.padding(vertical = 4.dp),
        ) {
            items(lazyPagingItems) { coin ->
                CoinItem(coin, navigateToPriceTargetEntryScreen = navigateToPriceTargetEntryScreen)
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
}

@Composable
private fun CoinItem(coin: CoinUI?, navigateToPriceTargetEntryScreen:(coin: CoinUI) -> Unit) {

    val context = LocalContext.current
    val expanded = rememberSaveable { mutableStateOf(false) }

    val extraPadding = if (expanded.value) 48.dp else 0.dp

    Surface(
        color = colorResource(id = R.color.dark_coin_row),
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp).clickable {
            navigateToPriceTargetEntryScreen(coin!!)
        }
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
                painter = rememberImagePainter(if (coin.hasPriceTarget.value) R.drawable.ic_alart_set else R.drawable.ic_alert_not_set),
                contentDescription = stringResource(R.string.alert_icon),
                modifier = Modifier
                    .size(25.dp)
                    .constrainAs(alertImage){
                        end.linkTo(parent.end, margin = 4.dp)
                        top.linkTo(coinName.top)
                    }
                    .clickable {
                        navigateToPriceTargetEntryScreen(coin)
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
private fun getPercentageColor(is24HrPriceChangePositive: Boolean) : Color {
    return if (is24HrPriceChangePositive) {
        colorResource(R.color.percentage_gain_green)
    } else {
        colorResource(R.color.red)
    }
}
