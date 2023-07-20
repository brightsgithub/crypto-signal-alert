package com.owusu.cryptosignalalert.views.screens.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

@Composable
fun LoadingWidget(
    boxModifier: Modifier? = null,
    loadingState: State<Boolean> = mutableStateOf(false)
) {

    val show = rememberSaveable { loadingState }

    val modifier = boxModifier ?: Modifier
        .fillMaxWidth()

    val alpha = if (show.value) 1.0f else 0.0f

    Box(modifier = modifier.padding(16.dp).alpha(alpha)) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.Center)
        )
    }
}