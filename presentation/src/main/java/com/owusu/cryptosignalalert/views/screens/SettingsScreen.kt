package com.owusu.cryptosignalalert.views.screens

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.owusu.cryptosignalalert.models.SettingUI
import com.owusu.cryptosignalalert.viewmodels.SettingsViewModel
import com.owusu.cryptosignalalert.views.theme.AppTheme
import org.koin.androidx.compose.getViewModel
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ChainStyle
import coil.compose.rememberImagePainter
import com.owusu.cryptosignalalert.models.SettingTypeUI
import com.owusu.cryptosignalalert.models.SettingsViewState
import com.owusu.cryptosignalalert.settings.ContactDeveloperHelper
import com.owusu.cryptosignalalert.settings.SettingsHelper
import org.koin.androidx.compose.get
import com.owusu.cryptosignalalert.R

@Composable
fun SettingsScreen(onNavigateToWebView:(url: String) -> Unit) {
   // AppTheme {
        val settingsHelper = get<SettingsHelper>()
        val contactDeveloperHelper = get<ContactDeveloperHelper>()
        val context = LocalContext.current
        val settingsViewModel = getViewModel<SettingsViewModel>()
        settingsViewModel.loadSettings()
        settingsViewModel.viewState.collectAsState(initial = SettingsViewState()).value.let {
  //          Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
                ShowSettings(
                    it.settings,
                    settingsHelper,
                    contactDeveloperHelper,
                    context,
                    onNavigateToWebView = onNavigateToWebView
                )
        //    }
        }
   // }
}

@Composable
fun ShowSettings(
    settings: List<SettingUI>,
    settingsHelper: SettingsHelper,
    contactDeveloperHelper: ContactDeveloperHelper,
    context: Context,
    onNavigateToWebView:(url: String) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = settings) { settingUI ->
            ShowSetting(settingUI, onSettingClicked = { settingUI ->
                when (settingUI.settingTypeUI) {
                    SettingTypeUI.ContactDeveloper -> { contactDeveloperHelper.provideFeedback(context as Activity)}
                    SettingTypeUI.PrivacyPolicy -> { onNavigateToWebView("https://sites.google.com/view/crypto-price-target-alerts-pri") }
                    SettingTypeUI.RateTheApp -> { settingsHelper.openAppOnGooglePlayStore(context) }
                    SettingTypeUI.ShareApp -> { settingsHelper.shareApp(context)}
                    else -> {
                        SettingTypeUI.Nothing
                    }
                }
            })
        }
    }
}

@Composable
fun ShowSetting(settingUI: SettingUI, onSettingClicked:(settingUI: SettingUI) -> Unit) {

    var showPopup = rememberSaveable { mutableStateOf(false) }

    if (showPopup.value) {
        if (settingUI.settingTypeUI == SettingTypeUI.DonateBTC) {
            LargeImagePopup(showPopup, R.drawable.btc_qr_code, settingUI.title, stringResource(id = R.string.btc_donate_address))
        } else {
            LargeImagePopup(showPopup, R.drawable.eth_qr_code, settingUI.title, stringResource(id = R.string.eth_donate_address))
        }
    }

    ConstraintLayout(modifier = Modifier
        .clickable {
            if (settingUI.settingTypeUI == SettingTypeUI.DonateBTC ||
                settingUI.settingTypeUI == SettingTypeUI.DonateETH
            ) {
                showPopup.value = true
            } else {
                onSettingClicked(settingUI)
            }
        }
        .fillMaxWidth()
    ) {

        val spacerHeight = 16.dp

        val (
            spacerTop,
            title,
            subTitle,
            settingsValue,
            spacerBottom,
            divider,
            icon
        ) = createRefs()

        Spacer(modifier = Modifier
            .constrainAs(spacerTop) {
                top.linkTo(parent.top)
            }
            .height(spacerHeight))

        // Used to pack the items vertically. If an element is not displayed, then its space will be filled.
        createVerticalChain(
            spacerTop, title, subTitle, settingsValue, spacerBottom, divider, chainStyle = ChainStyle.Packed)

        if (settingUI.settingTypeUI == SettingTypeUI.DonateBTC || settingUI.settingTypeUI == SettingTypeUI.DonateETH) {
            Image(
                painter = rememberImagePainter(settingUI.iconId),
                contentDescription = null,
                modifier = Modifier.size(35.dp).constrainAs(icon) {
                    start.linkTo(parent.start, margin = 8.dp)
                    centerVerticallyTo(parent)
                }
            )
        }

        Text(text = settingUI.title, modifier = Modifier.constrainAs(title) {
            if (settingUI.settingTypeUI == SettingTypeUI.DonateBTC || settingUI.settingTypeUI == SettingTypeUI.DonateETH){
                start.linkTo(icon.end, margin = 8.dp)
            } else {
                start.linkTo(parent.start, margin = 8.dp)
            }


        }, fontWeight = FontWeight.Bold)

        settingUI.subTitle?.let {
            Text(text = settingUI.subTitle, modifier = Modifier.constrainAs(subTitle) {
                start.linkTo(title.start)
                top.linkTo(title.bottom, margin = 2.dp)
            })
        }

        settingUI.selectedValue?.let {
            Text(text = settingUI.selectedValue, modifier = Modifier.constrainAs(settingsValue) {
                start.linkTo(title.start)
                top.linkTo(subTitle.bottom, margin = 2.dp)
            })
        }

        Spacer(modifier = Modifier
            .constrainAs(spacerBottom) {
                top.linkTo(settingsValue.bottom)
            }
            .height(spacerHeight))

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .constrainAs(divider) {
                    //top.linkTo(settingsValue.bottom, margin = 4.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },//color = Color.White
        )
    }
}

@Composable
fun LargeImagePopup(
    showPopUpState: MutableState<Boolean>,
    imageUrl: Int,
    title: String,
    donateAddress: String
) {
    var showToast by remember { mutableStateOf(false) }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { showPopUpState.value = false },
        confirmButton = {
            Button(onClick = {

                // Copy the text to the clipboard
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copy address", donateAddress)
                clipboard.setPrimaryClip(clip)

                // Show a short toast message using LaunchedEffect
                showToast = true


            }) {
                Text(text = "Copy address")
            }
        },
        title = { Text(text = title,fontWeight = FontWeight.Bold)},
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            ) {
                Image(
                    painter = rememberImagePainter(imageUrl),
                    contentDescription = null,
                    modifier = Modifier.size(300.dp)
                )
            }
        }
    )

    // LaunchedEffect to show the toast message
    LaunchedEffect(showToast) {
        if (showToast) {
            Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
            showPopUpState.value = false // Close the popup after showing the toast message
        }
    }
}


@Preview
@Composable
fun ShowSettingsPreview() {

    val settingsHelper = get<SettingsHelper>()
    val contactDeveloperHelper = get<ContactDeveloperHelper>()
    val context = LocalContext.current
    val settings = listOf(
        SettingUI(
            isFirstSetting = true,
            title = "Vibrate",
            subTitle = "Current version",
            selectedValue = "Version 1.0.0",
        ),
        SettingUI(
            title = "Share with friends",
            //subTitle = "Current version",
            selectedValue = "Share with friends value"
        ),
        SettingUI(
            title = "Privacy Policy",
            subTitle = "Current version",
            // selectedValue = "Version 1.0.0"
        ),
        SettingUI(
            isLastSetting = true,
            title = "AppVersion"
            //subTitle = "Current version",
            //selectedValue = "Version 1.0.0"
        )
    )

    ShowSettings(
        settings = settings,
        settingsHelper = settingsHelper,
        context = context,
        contactDeveloperHelper = contactDeveloperHelper,
        onNavigateToWebView = {}
    )
}
