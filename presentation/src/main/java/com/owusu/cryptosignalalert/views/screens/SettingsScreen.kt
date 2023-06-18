package com.owusu.cryptosignalalert.views.screens

import android.widget.Space
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ChainStyle
import com.owusu.cryptosignalalert.models.SettingsViewState

@Composable
fun SettingsScreen() {
    CryptoSignalAlertTheme {

        val settingsViewModel = getViewModel<SettingsViewModel>()
        settingsViewModel.loadSettings()
        settingsViewModel.viewState.collectAsState(initial = SettingsViewState()).value.let {
            Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
                ShowSettings(it.settings)
            }
        }
    }
}

@Composable
fun ShowSettings(settings: List<SettingUI>) {
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = settings) { settingUI ->
            ShowSetting(settingUI, onSettingClicked = { sku ->

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
    ShowSettings(settings = listOf(
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
            title = "AppVersion",
            //subTitle = "Current version",
            //selectedValue = "Version 1.0.0"
        )
    ))
}
