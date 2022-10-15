package com.owusu.cryptosignalalert.views.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.owusu.cryptosignalalert.models.CoinUI
import com.owusu.cryptosignalalert.views.theme.CryptoSignalAlertTheme
import org.koin.core.component.KoinComponent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.viewmodels.PriceTargetEntryViewModel
import org.koin.androidx.compose.getViewModel

class PriceTargetEntryActivity : ComponentActivity(), KoinComponent {

    companion object {
        private const val INTENT_EXTRA_COIN_UI = "INTENT_EXTRA_COIN_UI"
        fun getIntent(context: Context, coinUI: CoinUI): Intent {
            val intent = Intent(context, PriceTargetEntryActivity::class.java).apply {
                putExtra(INTENT_EXTRA_COIN_UI, coinUI)
            }
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoSignalAlertTheme {
                val coinUI = requireNotNull(intent.getParcelableExtra<CoinUI>(INTENT_EXTRA_COIN_UI))
                MyApp(coinUI)
            }
        }
    }
}

@Composable
private fun MyApp(coinUI: CoinUI) {
    Surface(color = MaterialTheme.colors.background) {
        val viewModel = getViewModel<PriceTargetEntryViewModel>()
        PriceTargetEntryScreen(
            coinUI,
            onSaveClicked = {
                userPriceTarget -> viewModel.saveNewPriceTarget(coinUI, userPriceTarget)
            }
        )
    }
}

@Composable
private fun PriceTargetEntryScreen(coinUI: CoinUI, onSaveClicked:(target: String) -> Unit) {

    var text by rememberSaveable { mutableStateOf(coinUI.name!!) }
    var userPriceTarget by rememberSaveable { mutableStateOf("") }

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
            saveBtn
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
            modifier = Modifier.constrainAs(saveBtn) {
                top.linkTo(priceTarget.bottom, margin = 24.dp)
                centerHorizontallyTo(parent)
            }.fillMaxWidth(),
            onClick = {
                onSaveClicked(userPriceTarget)
            }
        ) {
            Text("Save")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PriceTargetEntryScreenPreview() {
    CryptoSignalAlertTheme {
        PriceTargetEntryScreen(
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
                hasPriceTarget = true
            ),
            onSaveClicked = {
                    userPriceTarget -> { }
            }
        )
    }
}
