package com.owusu.cryptosignalalert.views.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.lifecycleScope
import androidx.work.WorkInfo
import coil.compose.rememberImagePainter
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.mappers.PriceTargetDirectionUI
import com.owusu.cryptosignalalert.models.AlertListViewState
import com.owusu.cryptosignalalert.models.CoinUI
import com.owusu.cryptosignalalert.models.CoinsListUiState
import com.owusu.cryptosignalalert.models.PriceTargetUI
import com.owusu.cryptosignalalert.viewmodels.AlertListViewModel
import com.owusu.cryptosignalalert.views.theme.CryptoSignalAlertTheme
import com.owusu.cryptosignalalert.workmanager.Constants
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.core.component.KoinComponent
import org.koin.androidx.viewmodel.ext.android.viewModel

class PriceTargetsActivity : ComponentActivity(), KoinComponent {

    private lateinit var workManagerJob: Job
    private val viewModel: AlertListViewModel by viewModel()

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, PriceTargetsActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerViewStates()
        setContent {
            CryptoSignalAlertTheme {
                MyApp()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
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
            viewModel.workInfoLiveData.observe(this@PriceTargetsActivity) { workInfoList ->
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
}

@Composable
private fun MyApp() {
    val viewModel = getViewModel<AlertListViewModel>()
    viewModel.viewState.collectAsState(initial = AlertListViewState()).value.let {
        Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
            ShowPriceTargets(it.priceTargets)
        }
    }
}

@Composable
private fun ShowPriceTargets(priceTargets: List<PriceTargetUI>) {

    val viewModel = getViewModel<AlertListViewModel>()

    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = priceTargets) { priceTarget ->
            PriceTargetCard(priceTarget, onDeleteClicked = { userPriceTarget ->
                viewModel.deletePriceTarget(userPriceTarget)
            })
        }
    }
}

@Composable
private fun PriceTargetCard(priceTarget: PriceTargetUI,
                            onDeleteClicked:(target: PriceTargetUI) -> Unit) {

    val showPopup = rememberSaveable { mutableStateOf(false) }

    Card(
        backgroundColor = colorResource(id = R.color.dark_coin_row),
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp),
        elevation = 10.dp
    ) {
        ConstraintLayout(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()) {
            // Create references for the composables to constrain
            val (
                marketRank,
                coinImage,
                currentPriceLabel,
                currentPrice,
                targetPrice,
                targetPriceLabel,
                coinName,
                progressBar,
                deletePriceTarget,
                alertImage,
                upArrow,
                downArrow,
                targetPriceLabel2,
                percentageProgressDisplay
            ) = createRefs()

            Text(text = priceTarget!!.marketCapRank.toString(),
                modifier = Modifier.constrainAs(marketRank) {
                    top.linkTo(parent.top, margin = 4.dp)
                }, fontSize = 12.sp)

            Image(
                painter = rememberImagePainter(priceTarget?.image),
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

            Text(text = priceTarget.name!! + " ("+priceTarget.symbol?.uppercase()+")", modifier = Modifier
                .constrainAs(coinName) {
                    start.linkTo(currentPriceLabel.end, margin = 16.dp)
                }, fontWeight = FontWeight.Bold)

            Text(text = "Price target", modifier = Modifier.constrainAs(targetPriceLabel) {
                start.linkTo(coinImage.start)
                top.linkTo(coinImage.bottom, margin = 16.dp)
            })

            Text(text = "Current price", modifier = Modifier.constrainAs(currentPriceLabel) {
                start.linkTo(targetPriceLabel.start)
                top.linkTo(targetPriceLabel.bottom)
            }, fontSize = 14.sp)

            Text(text = priceTarget.userPriceTargetDisplay!!, modifier = Modifier.constrainAs(targetPrice) {
                start.linkTo(coinName.start)
                top.linkTo(targetPriceLabel.top)
            }, fontWeight = FontWeight.Bold)

            Text(text = priceTarget.currentPriceDisplay!!, modifier = Modifier.constrainAs(currentPrice) {
                start.linkTo(coinName.start)
                top.linkTo(currentPriceLabel.top)
            }, fontSize = 14.sp)

            if (priceTarget.priceTargetDirection.name == PriceTargetDirectionUI.BELOW.name) {


                Text(text = priceTarget.progressPercentageDisplay!!,
                    modifier = Modifier.constrainAs(percentageProgressDisplay) {
                        top.linkTo(progressBar.top)
                        start.linkTo(currentPriceLabel.start)
                    },color = getProgressColor(priceTarget = priceTarget) ,fontSize = 12.sp)


                LinearProgressIndicator(
                    modifier = Modifier
                        .rotate(180f)
                        .width(250.dp)
                        .height(12.dp)
                        //.padding(bottom = 16.dp)
                        .constrainAs(progressBar) {
                            start.linkTo(percentageProgressDisplay.end, margin = 4.dp)
                            top.linkTo(currentPriceLabel.bottom, margin = 16.dp)
                            //end.linkTo(alertImage.start)
                        },
                    backgroundColor = Color.White,
                    progress = priceTarget.progress,
                    color = getProgressColor(priceTarget)
                )

                Image(
                    painter = rememberImagePainter(R.drawable.ic_baseline_arrow_upward_24),
                    contentDescription = stringResource(R.string.alert_icon),
                    modifier = Modifier
                        .size(25.dp)
                        .constrainAs(upArrow) {
                            start.linkTo(progressBar.start, margin = -12.5.dp)
                            top.linkTo(progressBar.bottom)

                        }
                )

                Text(text = "Target\n" + priceTarget.userPriceTargetDisplay, modifier = Modifier.constrainAs(targetPriceLabel2) {
                    start.linkTo(progressBar.start)
                    top.linkTo(upArrow.bottom)
                },fontSize = 12.sp, textAlign = TextAlign.Center)

            } else {
                LinearProgressIndicator(
                    modifier = Modifier
                        .width(250.dp)
                        .height(12.dp)
                        //.padding(top = 16.dp)
                        .constrainAs(progressBar) {
                            start.linkTo(currentPriceLabel.start)
                            top.linkTo(currentPriceLabel.bottom, margin = 16.dp)
                            //end.linkTo(alertImage.start)
                        },
                    backgroundColor = Color.White,
                    progress = priceTarget.progress,
                    color = getProgressColor(priceTarget),
                )

                Image(
                    painter = rememberImagePainter(R.drawable.ic_baseline_arrow_upward_24),
                    contentDescription = stringResource(R.string.alert_icon),
                    modifier = Modifier
                        .size(25.dp)
                        .constrainAs(upArrow) {
                            end.linkTo(progressBar.end, margin = -12.5.dp)
                            top.linkTo(progressBar.bottom)

                        }
                )

                Text(text = "Target\n" + priceTarget.userPriceTargetDisplay, modifier = Modifier.constrainAs(targetPriceLabel2) {
                    top.linkTo(upArrow.bottom)
                    centerHorizontallyTo(upArrow)
                },fontSize = 12.sp, textAlign = TextAlign.Center)

                Text(text = priceTarget.progressPercentageDisplay!!,
                    modifier = Modifier.constrainAs(percentageProgressDisplay) {
                    top.linkTo(progressBar.top)
                    start.linkTo(progressBar.end, 4.dp)
                },color = getProgressColor(priceTarget = priceTarget) ,fontSize = 12.sp)
            }

            Image(
                painter = rememberImagePainter(R.drawable.ic_baseline_delete_24),
                contentDescription = stringResource(R.string.alert_icon),
                modifier = Modifier
                    .size(25.dp)
                    .constrainAs(deletePriceTarget) {
                        end.linkTo(parent.end)
                        top.linkTo(coinName.top, margin = 4.dp)
                    }
                    .clickable {
                        onDeleteClicked(priceTarget)
                    }
            )
        }
    }
}



@Composable
fun SimpleAlertDialog() {
    AlertDialog(
        confirmButton = { },
        onDismissRequest = { },
    )
}

@Composable
private fun getProgressColor(priceTarget: PriceTargetUI) : Color {
    return when (priceTarget.priceTargetDirection) {
        PriceTargetDirectionUI.ABOVE -> colorResource(R.color.percentage_gain_green)
        PriceTargetDirectionUI.BELOW -> colorResource(R.color.red)
        PriceTargetDirectionUI.NOT_SET -> colorResource(R.color.white)
    }
}

private fun getGreaterOrLessThanSymbol(priceTarget: PriceTargetUI) : String {
    return when (priceTarget.priceTargetDirection) {
        PriceTargetDirectionUI.ABOVE -> ">"
        PriceTargetDirectionUI.BELOW -> "<"
        PriceTargetDirectionUI.NOT_SET -> ""
    }
}