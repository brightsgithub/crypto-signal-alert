package com.owusu.cryptosignalalert.views.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.models.CoinUI
import com.owusu.cryptosignalalert.viewmodels.PriceTargetEntryViewModel
import com.owusu.cryptosignalalert.viewmodels.SharedViewModel
import com.owusu.cryptosignalalert.views.theme.CryptoSignalAlertTheme
import org.koin.androidx.compose.getViewModel

@Composable
private fun PriceTargetEntryScreen(sharedViewModel: SharedViewModel) {

    CryptoSignalAlertTheme {
        val coinUI = sharedViewModel.selectedCoinUI
        Surface(color = MaterialTheme.colors.background) {
            val viewModel = getViewModel<PriceTargetEntryViewModel>()
            ShowPriceTargetEntryScreen(
                coinUI,
                onSaveClicked = { userPriceTarget ->
                    viewModel.saveNewPriceTarget(coinUI, userPriceTarget)
                }
            )
        }
    }
}

@Composable
private fun ShowPriceTargetEntryScreen(coinUI: CoinUI, onSaveClicked:(target: String) -> Unit) {

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
            viewTargetsBtn
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
            Text("Save")
        }

        Button(
            modifier = Modifier
                .constrainAs(viewTargetsBtn) {
                    top.linkTo(saveBtn.bottom, margin = 24.dp)
                    centerHorizontallyTo(parent)
                }
                .fillMaxWidth(),
            onClick = {
                //context.startActivity(PriceTargetsActivity.getIntent(context))
            }
        ) {
            Text("View Targets")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PriceTargetEntryScreenPreview() {
    CryptoSignalAlertTheme {
        ShowPriceTargetEntryScreen(
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
                hasPriceTarget = mutableStateOf(false)
            ),
            onSaveClicked = {
                    userPriceTarget -> { }
            }
        )
    }
}