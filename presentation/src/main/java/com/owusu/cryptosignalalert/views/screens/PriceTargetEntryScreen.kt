package com.owusu.cryptosignalalert.views.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberImagePainter
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.models.CoinDetailUI
import com.owusu.cryptosignalalert.models.CoinUI
import com.owusu.cryptosignalalert.models.PriceTargetEntryViewState
import com.owusu.cryptosignalalert.viewmodels.PriceTargetEntryViewModel
import com.owusu.cryptosignalalert.viewmodels.SharedViewModel
import com.owusu.cryptosignalalert.views.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.androidx.compose.getViewModel

@Composable
fun PriceTargetEntryScreen(
    sharedViewModel: SharedViewModel,
    navigateToPurchaseScreen:() -> Unit,
    navigateToTargetsList:() -> Unit,
    onShowSnackBar: (msg: String, actionLabel: String, shouldShowIndefinite: Boolean, actionCallback: () -> Unit) -> Unit
) {

  //  AppTheme {
        val coinUI = sharedViewModel.selectedCoinUI
        val viewModel = getViewModel<PriceTargetEntryViewModel>()
        val viewState = viewModel.viewState.collectAsState(initial = PriceTargetEntryViewState())

        LaunchedEffect(viewModel) {
            viewModel.getCoinDetails(coinUI = coinUI)
        }

        //Surface(color = MaterialTheme.colors.background) {
            
            ShowPriceTargetEntryScreen(
                viewState,
                coinUI
            ) { userPriceTarget ->
                viewModel.saveNewPriceTarget(coinUI, userPriceTarget)
                navigateToTargetsList()
            }
        //}

        if (viewState.value.priceTargetsMessage.shouldShowMessage) {
            onShowSnackBar(
                viewState.value.priceTargetsMessage.message,
                viewState.value.priceTargetsMessage.ctaText,
                false,
                {
                  if (viewState.value.priceTargetsMessage.isError) {
                          navigateToPurchaseScreen()
                  }
                }
            )
        }

        LoadingWidget3()
   // }
}

@Composable
fun LoadingWidget3(boxModifier: Modifier? = null){
    val viewModel = getViewModel<PriceTargetEntryViewModel>()

    val modifier = boxModifier ?: Modifier
        .fillMaxWidth()

    val alpha = if (viewModel.viewState.collectAsState(initial = PriceTargetEntryViewState()).value.isLoading) 1.0f else 0.0f

    Box(modifier = modifier
        .padding(16.dp)
        .alpha(alpha)) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.Center)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShowPriceTargetEntryScreen(
    viewState: State<PriceTargetEntryViewState>,
    coinUI: CoinUI,
    onSaveClicked: (target: String) -> Unit
) {

    var text by rememberSaveable { mutableStateOf(coinUI.name!!) }
    var userPriceTarget by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    ConstraintLayout(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .fillMaxHeight()) {
        // Create references for the composables to constrain
        val (
            coinTitle,
            coinTextEntry,
            coinImage,
            currentPrice,
            priceTarget,
            saveBtn,
            viewTargetsBtn,
            loadingDescView,
            descView,
            infoView
        ) = createRefs()

        Image(
            painter = rememberImagePainter(coinUI?.image),
            contentDescription = stringResource(R.string.image_coin_content_desc),
            modifier = Modifier
                // Set image size to 40 dp
                .size(60.dp)
                // Clip image to be shaped as a circle
                .clip(CircleShape)
                .constrainAs(coinImage) {
                    top.linkTo(parent.top, margin = 4.dp)
                    centerHorizontallyTo(parent)
                }
        )

        Text(
            text = coinUI.name!! + " ("+coinUI.symbol?.uppercase()+")",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .constrainAs(coinTitle) {
                    top.linkTo(coinImage.bottom, margin = 2.dp)
                    centerHorizontallyTo(coinImage)
                }
        )

        Text(
            text = coinUI.currentPriceStr!!,
            fontSize = 20.sp,
            modifier = Modifier
                .constrainAs(currentPrice) {
                    top.linkTo(coinTitle.bottom, margin = 4.dp)
                    centerHorizontallyTo(coinImage)
                }
        )

        OutlinedTextField(
            value = text,
            modifier = Modifier
                .clickable(
                    enabled = false,
                    onClick = { }
                )
                .constrainAs(coinTextEntry) {
                    top.linkTo(currentPrice.bottom, margin = 32.dp)
                }
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            label = { Text(text = "Asset name") },
            placeholder = { Text(text = "Enter asset") },
            onValueChange = {
                text = it
            }
        )

        OutlinedTextField(
            value = userPriceTarget,
            modifier = Modifier
                .constrainAs(priceTarget) {
                    top.linkTo(coinTextEntry.bottom, margin = 4.dp)
                }
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(text = "Price target") },
            placeholder = { Text(text = "Enter price target") },
            onValueChange = {
                userPriceTarget = it
            }
        )

        Button(
            modifier = Modifier
                .constrainAs(saveBtn) {
                    top.linkTo(priceTarget.bottom, margin = 24.dp)
                    centerHorizontallyTo(parent)
                }
                .fillMaxWidth(),
            onClick = {
                onSaveClicked(userPriceTarget)
            }
        ) {
            Text("Save Price Target")
        }

        if (viewState.value.isLoading) {

            Box(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(loadingDescView) {
                    top.linkTo(saveBtn.bottom)
                    bottom.linkTo(parent.bottom)
                }
                .padding(16.dp)
                .alpha(1.0f)) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.Center)
                )
            }
        }

        viewState.value.coinDetailUI?.description?.let {

            Text(text="Information", modifier = Modifier.constrainAs(infoView){
                top.linkTo(saveBtn.bottom, margin = 16.dp)
            })

            Box(
                modifier = Modifier
                    .constrainAs(descView) {
                        top.linkTo(infoView.bottom, margin = 16.dp)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .border(width = 1.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {

                SelectionContainer() {
                    Text(
                        text = it,
                        modifier = Modifier.padding(16.dp)
                    )
                }

            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun PriceTargetEntryScreenPreview() {
    AppTheme {

        val _state = MutableStateFlow(
            PriceTargetEntryViewState(isLoading = false,
            coinDetailUI = CoinDetailUI(
                name = "Bitcoin",
                symbol = "BTC",
                description = "Bitcoin is the first successful internet money based on peer-to-peer technology; whereby no central bank or authority is involved in the transaction and production of the Bitcoin currency. It was created by an anonymous individual/group under the name, Satoshi Nakamoto. The source code is available publicly as an open source project, anybody can look at it and be part of the developmental process.\n" +
                        "\n" +
                        "Bitcoin is changing the way we see money as we speak. The idea was to produce a means of exchange, independent of any central authority, that could be transferred electronically in a secure, verifiable and immutable way. It is a decentralized peer-to-peer internet currency making mobile payment easy, very low transaction fees, protects your identity, and it works anywhere all the time with no central authority and banks.\n" +
                        "\n" +
                        "Bitcoin is designed to have only 21 million BTC ever created, thus making it a deflationary currency. Bitcoin uses the <a href=\"https://www.coingecko.com/en?hashing_algorithm=SHA-256\">SHA-256</a> hashing algorithm with an average transaction confirmation time of 10 minutes. Miners today are mining Bitcoin using ASIC chip dedicated to only mining Bitcoin, and the hash rate has shot up to peta hashes.\n" +
                        "\n" +
                        "Being the first successful online cryptography currency, Bitcoin has inspired other alternative currencies such as <a href=\"https://www.coingecko.com/en/coins/litecoin\">Litecoin</a>, <a href=\"https://www.coingecko.com/en/coins/peercoin\">Peercoin</a>, <a href=\"https://www.coingecko.com/en/coins/primecoin\">Primecoin</a>, and so on.\n" +
                        "\n" +
                        "The cryptocurrency then took off with the innovation of the turing-complete smart contract by <a href=\"https://www.coingecko.com/en/coins/ethereum\">Ethereum</a> which led to the development of other amazing projects such as <a href=\"https://www.coingecko.com/en/coins/eos\">EOS</a>, <a href=\"https://www.coingecko.com/en/coins/tron\">Tron</a>, and even crypto-collectibles such as <a href=\"https://www.coingecko.com/buzz/ethereum-still-king-dapps-cryptokitties-need-1-billion-on-eos\">CryptoKitties</a>.",
            id = "bitcoin")
        )) // for emitting
        val viewState: MutableStateFlow<PriceTargetEntryViewState> = _state // for clients to listen to

        ShowPriceTargetEntryScreen(
            viewState = viewState.collectAsState(),
            coinUI = CoinUI(
                id = "bitcoin",
                name = "Bitcoin",
                currentPriceStr = "19000.0",
                marketCapStr = "20000000.0",
                marketCapRank = 1,
                image = "",
                priceChangePercentage24hStr = "25.0",
                marketCapChangePercentage24h = 10.0,
                is24HrPriceChangePositive = true,
                hasPriceTarget = mutableStateOf(true)
            )
        ) { userPriceTarget ->
            { }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun PriceTargetEntryScreenPreviewNoDesc() {
    AppTheme {

        val _state = MutableStateFlow(
            PriceTargetEntryViewState(isLoading = true,
                coinDetailUI = CoinDetailUI(
                    name = "Bitcoin",
                    symbol = "BTC",
                    id = "bitcoin")
            )) // for emitting
        val viewState: MutableStateFlow<PriceTargetEntryViewState> = _state // for clients to listen to

        ShowPriceTargetEntryScreen(
            viewState = viewState.collectAsState(),
            coinUI = CoinUI(
                id = "bitcoin",
                name = "Bitcoin",
                currentPriceStr = "19000.0",
                marketCapStr = "20000000.0",
                marketCapRank = 1,
                image = "",
                priceChangePercentage24hStr = "25.0",
                marketCapChangePercentage24h = 10.0,
                is24HrPriceChangePositive = true,
                hasPriceTarget = mutableStateOf(true)
            )
        ) { userPriceTarget ->
            { }
        }
    }
}