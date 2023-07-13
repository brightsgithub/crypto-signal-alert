package com.owusu.cryptosignalalert.views.screens

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.owusu.cryptosignalalert.models.SettingUI
import com.owusu.cryptosignalalert.viewmodels.SettingsViewModel
import com.owusu.cryptosignalalert.views.theme.CryptoSignalAlertTheme
import org.koin.androidx.compose.getViewModel
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ChainStyle
import com.owusu.cryptosignalalert.domain.models.ScreenProxy
import com.owusu.cryptosignalalert.models.SettingTypeUI
import com.owusu.cryptosignalalert.models.SettingsViewState
import com.owusu.cryptosignalalert.settings.ContactDeveloperHelper
import com.owusu.cryptosignalalert.settings.SettingsHelper
import org.koin.androidx.compose.get

@Composable
fun SettingsScreen(onNavigateToWebView:(url: String) -> Unit) {
    CryptoSignalAlertTheme {
        val settingsHelper = get<SettingsHelper>()
        val contactDeveloperHelper = get<ContactDeveloperHelper>()
        val context = LocalContext.current
        val settingsViewModel = getViewModel<SettingsViewModel>()
        settingsViewModel.loadSettings()
        settingsViewModel.viewState.collectAsState(initial = SettingsViewState()).value.let {
            Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
                ShowSettings(
                    it.settings,
                    settingsHelper,
                    contactDeveloperHelper,
                    context,
                    onNavigateToWebView = onNavigateToWebView
                )
            }
        }
    }
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
                    SettingTypeUI.Nothing -> { }
                    SettingTypeUI.PrivacyPolicy -> { onNavigateToWebView("https://sites.google.com/view/crypto-price-target-alerts-pri") }
                    SettingTypeUI.RateTheApp -> { settingsHelper.openAppOnGooglePlayStore(context) }
                    SettingTypeUI.ShareApp -> { settingsHelper.shareApp(context)}
                }
            })
        }
    }
}

@Composable
fun ShowSetting(settingUI: SettingUI, onSettingClicked:(settingUI: SettingUI) -> Unit) {
    ConstraintLayout(modifier = Modifier.clickable { onSettingClicked(settingUI) }
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
        ) = createRefs()

        Spacer(modifier = Modifier.constrainAs(spacerTop){
            top.linkTo(parent.top)
        }.height(spacerHeight))

        // Used to pack the items vertically. If an element is not displayed, then its space will be filled.
        createVerticalChain(
            spacerTop, title, subTitle, settingsValue, spacerBottom, divider, chainStyle = ChainStyle.Packed)

        Text(text = settingUI.title, modifier = Modifier.constrainAs(title) {
            start.linkTo(parent.start, margin = 8.dp)
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

        Spacer(modifier = Modifier.constrainAs(spacerBottom) {
            top.linkTo(settingsValue.bottom)
        }.height(spacerHeight))

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .constrainAs(divider) {
                    //top.linkTo(settingsValue.bottom, margin = 4.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },color = Color.White
        )
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
