package com.owusu.cryptosignalalert.views.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.owusu.cryptosignalalert.views.theme.CryptoSignalAlertTheme

@Composable
fun PurchaseScreen() {
    CryptoSignalAlertTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = "Purchase", modifier = Modifier.align(Alignment.Center))
        }
    }
}