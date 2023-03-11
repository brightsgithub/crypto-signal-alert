package com.owusu.cryptosignalalert.views.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owusu.cryptosignalalert.models.*
import com.owusu.cryptosignalalert.viewmodels.CoinSearchViewModel
import com.owusu.cryptosignalalert.viewmodels.PriceTargetEntryViewModel
import com.owusu.cryptosignalalert.viewmodels.SharedViewModel
import com.owusu.cryptosignalalert.views.screens.widgets.SearchBarUI
import org.koin.androidx.compose.getViewModel

//@ExperimentalComposeUiApi
//@ExperimentalAnimationApi
@Composable
fun CoinSearchScreen(sharedViewModel: SharedViewModel, navigateToPriceTargetEntryScreen:(coin: CoinUI) -> Unit) {


    val searchViewModel = getViewModel<CoinSearchViewModel>()
    val coinSearchState = searchViewModel.coinIdSearchModelState.collectAsState(initial = CoinSearchState.Empty)
    searchViewModel.coinSearchStateEvents.collectAsState(initial = CoinSearchStateEvents.NOTHING).value.let {
        when(it) {
            is CoinSearchStateEvents.NOTHING -> {}
            is CoinSearchStateEvents.NavigateToPriceTargetEntryScreen -> {
                navigateToPriceTargetEntryScreen(it.coinUI)
            }
        }
    }


    SearchBarUI(
        searchText = coinSearchState.value.searchStr,
        placeholderText = "Search coins",
        onSearchTextChanged = { searchViewModel.onSearchTextChanged(it) },
        onClearClick = { searchViewModel.onClearClick() },
        onNavigateBack = {
            //navHostController.popBackStack()
        },
        matchesFound = coinSearchState.value.coinIds.isNotEmpty(),
        resultsSize = coinSearchState.value.resultSize
    ) {

        DisplayCoinIdResults(coinIds = coinSearchState.value.coinIds, onClick = { coinIdUI ->
            searchViewModel.onSearchItemSelected(coinIdUI)
        })
    }
}

@Composable
fun DisplayCoinIdResults(coinIds: List<CoinIdUI>, onClick: (CoinIdUI) -> Unit) {

    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = coinIds) { coinId ->
            DisplayCoinIdRow(coinId, onClick)
            Divider()
        }
    }
}


@Composable
fun DisplayCoinIdRow(coinId: CoinIdUI, onClick: (CoinIdUI) -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable { onClick(coinId) }) {
        Text(coinId.name, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(coinId.symbol)
        Spacer(modifier = Modifier.height(4.dp))
    }
}