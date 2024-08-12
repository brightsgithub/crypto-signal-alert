package com.owusu.cryptosignalalert.views.screens

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owusu.cryptosignalalert.models.*
import com.owusu.cryptosignalalert.viewmodels.CoinSearchViewModel
import com.owusu.cryptosignalalert.viewmodels.SharedViewModel
import com.owusu.cryptosignalalert.viewmodels.udf.coinserach.CoinSearchState
import com.owusu.cryptosignalalert.viewmodels.udf.coinserach.CoinSearchUdfAction
import com.owusu.cryptosignalalert.viewmodels.udf.coinserach.CoinSearchUdfEvent
import com.owusu.cryptosignalalert.viewmodels.udf.home.HomeUdfEvent
import com.owusu.cryptosignalalert.views.screens.widgets.SearchBarUI
import org.koin.androidx.compose.getViewModel

//@ExperimentalComposeUiApi
//@ExperimentalAnimationApi
@Composable
fun CoinSearchScreen(
    sharedViewModel: SharedViewModel
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val searchViewModel = getViewModel<CoinSearchViewModel>()
    val coinSearchState = searchViewModel.uiState.collectAsState(initial = CoinSearchState.Empty)
    val handleSearchViewModelEvent = searchViewModel::handleEvent

    LaunchedEffect(Unit) {
        searchViewModel.action.collect { action ->
            when (action) {
                is CoinSearchUdfAction.NavigateToPriceTargetEntryScreen -> {
                    sharedViewModel.handleEvent(HomeUdfEvent.NavigateToPriceTargetEntryFromSearch(action.coinUI))
                }
                else -> {}
            }
        }
    }

    if (coinSearchState.value.coinSearchStateMessage.shouldShowMessage) {
        val handleSharedViewModelEvent = sharedViewModel::handleEvent
        handleSharedViewModelEvent(
            HomeUdfEvent.ShowSnackBar(
                msg = coinSearchState.value.coinSearchStateMessage.message,
                actionLabel = coinSearchState.value.coinSearchStateMessage.ctaText,
                shouldShowIndefinite = false,
                actionCallback = {}
            )
        )

        //searchViewModel.hideSnackBar()
        handleSearchViewModelEvent(CoinSearchUdfEvent.HideSnackBar)
    }

    SearchBarUI(
        searchText = coinSearchState.value.searchStr,
        placeholderText = "Search coins",
        onSearchTextChanged = { handleSearchViewModelEvent(CoinSearchUdfEvent.OnSearchTextChanged(it)) },
        onClearClick = { handleSearchViewModelEvent(CoinSearchUdfEvent.OnClearClicked) },
        onNavigateBack = {
            onBackPressedDispatcher?.onBackPressed()
        },
        matchesFound = coinSearchState.value.coinIds.isNotEmpty(),
        resultsSize = coinSearchState.value.resultSize
    ) {

        DisplayCoinIdResults(coinIds = coinSearchState.value.coinIds, onClick = { coinIdUI ->
            handleSearchViewModelEvent(CoinSearchUdfEvent.OnSearchItemSelected(coinIdUI))
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