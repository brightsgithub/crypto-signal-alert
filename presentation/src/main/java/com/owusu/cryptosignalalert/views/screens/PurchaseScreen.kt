package com.owusu.cryptosignalalert.views.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.domain.models.ScreenProxy
import com.owusu.cryptosignalalert.models.PurchaseTypeUI
import com.owusu.cryptosignalalert.viewmodels.udf.purchase.PurchaseViewState
import com.owusu.cryptosignalalert.models.SkuDetailsUI
import com.owusu.cryptosignalalert.viewmodels.PurchaseViewModel
import com.owusu.cryptosignalalert.viewmodels.udf.purchase.PurchaseUdfEvent
import com.owusu.cryptosignalalert.views.theme.AppTheme
import com.owusu.cryptosignalalert.views.theme.white
import org.koin.androidx.compose.getViewModel

@Composable
fun PurchaseScreen() {

    val purchaseViewModel = getViewModel<PurchaseViewModel>()
    val handleEvent = purchaseViewModel::handleEvent
    val uiState = purchaseViewModel.uiState.collectAsState(initial = PurchaseViewState())

    LaunchedEffect(Unit) {
        handleEvent(PurchaseUdfEvent.LoadSkuDetails)
    }

    ShowPriceList(uiState.value.skuDetailsList, handleEvent)
    LoadingWidget2(boxModifier = Modifier.fillMaxSize(), uiState)
}

@Composable
fun LoadingWidget2(boxModifier: Modifier? = null, uiState: State<PurchaseViewState>) {
    val purchaseViewModel = getViewModel<PurchaseViewModel>()

    val modifier = boxModifier ?: Modifier
        .fillMaxWidth()

    //val alpha = if (purchaseViewModel.loadingState.collectAsState(initial = false).value) 1.0f else 0.0f
    val alpha = if (uiState.value.isLoading) {
        1.0f
    } else {
        0.0f
    }

    Box(
        modifier = modifier
            .padding(16.dp)
            .alpha(alpha)
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun ShowPriceList(skuDetailsList: List<SkuDetailsUI>, handleEvent: (PurchaseUdfEvent) -> Unit) {

    val purchaseViewModel = getViewModel<PurchaseViewModel>()
    val screenProxy = LocalContext.current as ScreenProxy

    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = skuDetailsList) { skuDetails ->
            PurchaseItem(skuDetails, onBuyClicked = { sku ->
                handleEvent(PurchaseUdfEvent.OnPurchaseClicked(screenProxy,sku.sku))
            })
        }
    }
}

// https://www.answertopia.com/jetpack-compose/jetpack-compose-constraintlayout-examples/
@Composable
fun PurchaseItem(skuDetails: SkuDetailsUI, onBuyClicked: (sku: SkuDetailsUI) -> Unit) {
    Card(
        // backgroundColor = colorResource(id = R.color.dark_coin_row),
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp),
        // elevation = 10.dp
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            // Create references for the composables to constrain
            val (
                title,
                subTitle,
                price,
                description,
                icon,
                icon2,
                buyBtn
            ) = createRefs()

            Text(text = skuDetails.title, modifier = Modifier.constrainAs(title) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(icon.start)
                linkTo(parent.start, icon.start, bias = 0f) // opposing constraints
            }, fontWeight = FontWeight.Bold, fontSize = 20.sp)

            if (skuDetails.purchaseTypeUI.equals(PurchaseTypeUI.BuyAll)) {
                Image(
                    painter = rememberImagePainter(R.drawable.ic_alart_set),
                    contentDescription = skuDetails.title,
                    modifier = Modifier
                        // Set image size to 40 dp
                        .size(40.dp)
                        // Clip image to be shaped as a circle
                        .clip(CircleShape)
                        .constrainAs(icon) {
                            end.linkTo(icon2.start, margin = 16.dp)
                            top.linkTo(title.top)
                        }
                )
                Image(
                    painter = rememberImagePainter(R.drawable.noads),
                    contentDescription = skuDetails.title,
                    modifier = Modifier
                        // Set image size to 40 dp
                        .size(40.dp)
                        // Clip image to be shaped as a circle
                        .clip(CircleShape)
                        .constrainAs(icon2) {
                            end.linkTo(parent.end, margin = 16.dp)
                            top.linkTo(title.top)
                        }
                )
            } else {
                Image(
                    painter = rememberImagePainter(skuDetails.imageResId),
                    contentDescription = skuDetails.title,
                    modifier = Modifier
                        // Set image size to 40 dp
                        .size(70.dp)
                        // Clip image to be shaped as a circle
                        .clip(CircleShape)
                        .constrainAs(icon) {
                            end.linkTo(parent.end, margin = 16.dp)
                            top.linkTo(title.top)
                        }
                )
            }


            Text(text = skuDetails.subTitle, modifier = Modifier.constrainAs(subTitle) {
                start.linkTo(title.start)
                top.linkTo(title.bottom, margin = 16.dp)
            }, fontWeight = FontWeight.Bold)

            Text(text = skuDetails.description, modifier = Modifier.constrainAs(description) {
                start.linkTo(subTitle.start)
                top.linkTo(subTitle.bottom, margin = 16.dp)
            })

            Button(
                onClick = { onBuyClicked(skuDetails) },
                Modifier
                    .constrainAs(buyBtn) {
                        start.linkTo(description.start)
                        top.linkTo(description.bottom, margin = 24.dp)
                        bottom.linkTo(parent.bottom, margin = 24.dp)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth(),
                //.background(getProgressColor(skuDetailsUI = skuDetails)),
                enabled = !skuDetails.isPurchased

            ) {
                if (!skuDetails.isPurchased) {
                    Text(
                        text = "Buy " + skuDetails.price,
                        fontWeight = FontWeight.Bold,
                        color = white
                    )
                } else {
                    Text(text = "Purchased", fontWeight = FontWeight.Bold)
                }

            }
        }
    }
}

//@Composable
//private fun getProgressColor(skuDetailsUI: SkuDetailsUI) : Color {
//    return if (skuDetailsUI.isPurchased) {
//        colorResource(R.color.percentage_gain_green)
//    } else {
//        colorResource(R.color.white)
//    }
//}

@Composable
@Preview(showBackground = true)
fun PreviewPurchaseItem() {
    AppTheme {
        PurchaseItem(skuDetails = SkuDetailsUI(
            sku = "com.owusu.cryptosignalalert.unlock_all",
            title = "Buy All Bundle",
            subTitle = "Buy everyting",
            description = "Special bundle offer: Set an unrestricted number of alerts & remove ads all for a single price!",
            price = "GBP 1.00",
            isPurchased = false,
            imageResId = R.drawable.noads,
            purchaseTypeUI = PurchaseTypeUI.BuyAll
        ), onBuyClicked = { })
    }

}
