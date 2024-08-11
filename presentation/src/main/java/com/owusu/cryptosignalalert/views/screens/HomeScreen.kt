package com.owusu.cryptosignalalert.views.screens

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.owusu.cryptosignalalert.models.SharedViewState
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.owusu.cryptosignalalert.BuildConfig
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.navigation.*
import com.owusu.cryptosignalalert.viewmodels.SharedViewModel
import com.owusu.cryptosignalalert.views.activities.MainActivity
import com.owusu.cryptosignalalert.views.theme.*
import org.koin.androidx.compose.getViewModel

const val TAG = "HomeScreen"
// Since bottom bar uses its own NavHost, we have to pass it a new NavHostController
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    preselectedScreen: MutableState<String?>,
    adRequest: AdRequest
) {
    val interstitialAd = remember { mutableStateOf<InterstitialAd?>(null) }

    val sharedViewModel = getViewModel<SharedViewModel>()
    val destinationChangeListener = rememberDestinationChangeListener(navController, sharedViewModel)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(Unit) {
        lifecycle.addObserver(destinationChangeListener)

        // Perform any additional initialization or actions if needed

        onDispose {
            lifecycle.removeObserver(destinationChangeListener)
        }
    }

    val sharedViewState = sharedViewModel.uiState.collectAsState(initial = SharedViewState())

    //val scaffoldState = rememberScaffoldState()
    val snackbarHostState = remember { SnackbarHostState() }
    //val snackbarHostState = scaffoldState.snackbarHostState

    // https://developer.android.com/jetpack/compose/navigation
    val onSearchBarClick = {
        navController.navigate(route = CoinSearchScreens.CoinSearch.route) {
            // Navigate to the "search” destination only if we’re not already on
            // the "search" destination, avoiding multiple copies on the top of the
            // back stack
            // YOU MUST USE ROUTE NOT GRAPH!
            launchSingleTop = true
        }
    }

    val onSettingsClicked = {
        navController.navigate(route = SettingsScreens.Settings.route) {
            // Navigate to the "settings” destination only if we’re not already on
            // the "settings" destination, avoiding multiple copies on the top of the
            // back stack
            // YOU MUST USE ROUTE NOT GRAPH!
            launchSingleTop = true
        }
    }


    val onShowSnackBar = {
            msg: String, actionLabel: String, shouldShowIndefinite: Boolean, actionCallback:() -> Unit ->
        sharedViewModel.showSnackBar(msg, actionLabel, actionCallback, shouldShowIndefinite)
    }

    val onHideSnackBar = {  ->
        sharedViewModel.hideSnackBar()
    }

    val onShowInterstitialAdAttempted = {
        sharedViewModel.showInterstitialAdAttempted()
    }

    // Show any snack bar message
    ShowSnackBarMessage(sharedViewState, snackbarHostState, onHideSnackBar)

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = { TopBar(
            onSearchBarClick = onSearchBarClick,
            onSettingsClicked = onSettingsClicked,
            sharedViewState,
        ) },
        bottomBar = { BottomNavigationBar(navController, preselectedScreen) },
        content = { padding -> // We have to pass the scaffold inner padding to our content. That's why we use Box.
            Box(modifier = Modifier.padding(padding)) {

                if (!sharedViewState.value.purchasedState.isAdsPurchased) {

//                    ShowInterstitialAd(
//                        sharedViewState,
//                        interstitialAd,
//                        onShowInterstitialAdAttempted,
//                        adRequest
//                    )

                    Column() {
                        Row(modifier = Modifier.weight(0.9f)) {
                            HomeNavGraph(
                                navHostController = navController,
                                onSearchBarClick = onSearchBarClick,
                                onShowSnackBar = onShowSnackBar
                            )
                        }
                        Row(modifier = Modifier.weight(0.1f)) {
                            Box {
                                BannerAdView()
                            }
                        }
                    }
                } else {
                    HomeNavGraph(
                        navHostController = navController,
                        onSearchBarClick = onSearchBarClick,
                        onShowSnackBar = onShowSnackBar
                    )
                }
            }
        },
        //backgroundColor = colorResource(R.color.colorPrimary) // Set background color to avoid the white flashing when you switch between screens
    )
}

@Composable
private fun ShowSnackBarMessage(
    sharedViewState: State<SharedViewState>,
    snackbarHostState: SnackbarHostState,
    onHideSnackBar: () -> Unit
) {
    if (sharedViewState.value.appSnackBar.shouldShowSnackBar) {

        // `LaunchedEffect` will cancel and re-launch if
        // `scaffoldState.snackbarHostState` changes
        LaunchedEffect(snackbarHostState) {
            // Show snackbar using a coroutine, when the coroutine is cancelled the
            // snackbar will automatically dismiss. This coroutine will cancel whenever
            // `state.hasError` is false, and only start when `state.hasError` is true
            // (due to the above if-check), or if `scaffoldState.snackbarHostState` changes.
            val snackbarResult = snackbarHostState.showSnackbar(
                message = sharedViewState.value.appSnackBar.snackBarMessage,
                actionLabel = sharedViewState.value.appSnackBar.actionLabel,
                withDismissAction = true,
                duration = SnackbarDuration.Short
            )

            // Check if the snackbar action was clicked
            if (snackbarResult == SnackbarResult.ActionPerformed) {
                // Perform the desired action here
                // This block of code will be executed when the action label is clicked
                sharedViewState.value.appSnackBar.actionCallback.invoke()
            }

            onHideSnackBar()
        }
    }
}

@Composable
private fun ShowInterstitialAd(
    sharedViewState: State<SharedViewState>,
    interstitialAd: MutableState<InterstitialAd?>,
    onShowInterstitialAdAttempted: () -> Unit,
    adRequest: AdRequest
) {
    val context = LocalContext.current
    val interstitialId = if (BuildConfig.DEBUG) {
        stringResource(id = R.string.test_interstitial_ad_unit_id)
    } else {
        stringResource(id = R.string.interstitial_ad_unit_id)
    }

    if (sharedViewState.value.shouldShowInterstitialAd) {

        LaunchedEffect(sharedViewState.value.shouldShowInterstitialAd) {
            InterstitialAd.load(
                context,
                interstitialId,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        interstitialAd.value = null
                        Log.d(TAG, "onAdFailedToLoad")
                    }

                    override fun onAdLoaded(interAd_: InterstitialAd) {
                        interstitialAd.value = interAd_

                        interstitialAd.value?.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdClicked() {
                                    // Called when a click is recorded for an ad.
                                    Log.d(TAG, "Ad was clicked.")
                                }

                                override fun onAdDismissedFullScreenContent() {
                                    // Called when ad is dismissed.
                                    Log.d(TAG, "Ad dismissed fullscreen content.")
                                    interstitialAd.value = null
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                    // Called when ad fails to show.
                                    Log.e(TAG, "Ad failed to show fullscreen content.")
                                    interstitialAd.value = null
                                }

                                override fun onAdImpression() {
                                    // Called when an impression is recorded for an ad.
                                    Log.d(TAG, "Ad recorded an impression.")
                                }

                                override fun onAdShowedFullScreenContent() {
                                    // Called when ad is shown.
                                    Log.d(TAG, "Ad showed fullscreen content.")
                                }
                            }

                        Log.d(TAG, "onAdLoaded")
                        if (interAd_ != null) {
                            Log.d(TAG, "calling show")
                            interstitialAd.value?.show(context as MainActivity)
                        }
                    }
                }
            )

            onShowInterstitialAdAttempted()
        }
    }
}

@Composable
fun BannerAdView() {

    val bannerId = if (BuildConfig.DEBUG) {
        stringResource(id = R.string.test_ad_mob_banner_id)
    } else {
        stringResource(id = R.string.ad_mob_banner_id)
    }

    AndroidView(factory = {

        AdView(it).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = bannerId
            loadAd(AdRequest.Builder().build())
        }
    }, modifier = Modifier.fillMaxSize())
}

// https://johncodeos.com/how-to-add-search-in-list-with-jetpack-compose/
// https://www.devbitsandbytes.com/configuring-searchview-in-jetpack-compose/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onSearchBarClick: () -> Unit, onSettingsClicked: () -> Unit, sharedViewState: State<SharedViewState>) {

    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    if (sharedViewState.value.actionButtonState.shouldShowToolTar) {
        TopAppBar(
            title = { Text(text = sharedViewState.value.actionButtonState.title, fontSize = 18.sp) },
            //backgroundColor = colorResource(id = R.color.colorPrimary),
           // contentColor = Color.White,
            navigationIcon = {
                if (sharedViewState.value.actionButtonState.shouldShowUpButtonIcon) {
                    IconButton(
                        onClick = { onBackPressedDispatcher?.onBackPressed() }
                    ) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.content_desc_up_button)
                        )
                    }
                }
            },
            actions = {
                if (sharedViewState.value.actionButtonState.shouldShowSearchIcon) {
                    IconButton(
                        modifier = Modifier,
                        onClick = { onSearchBarClick() }) {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = stringResource(id = R.string.icn_search_view_demo_app_bar_search)
                        )
                    }
                }

                if (sharedViewState.value.actionButtonState.shouldShowSettingsIcon) {
                    IconButton(
                        modifier = Modifier,
                        onClick = { onSettingsClicked() }) {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = stringResource(id = R.string.icn_settings)
                        )
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    val state = rememberSaveable { mutableStateOf(SharedViewState()) }
    TopBar(onSearchBarClick = {}, onSettingsClicked = {}, state)
}

@Composable
fun BottomNavigationBar(navController: NavHostController, preselectedScreen: MutableState<String?>) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.PriceTargets,
        NavigationItem.Purchase
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Only show Nav Bar if it is part of screens in the items list
    val bottomBarDestination = items.any { it.route == currentDestination?.route }
    if (!bottomBarDestination) return

    val selectedScreen = preselectedScreen.value ?: currentDestination?.route

    NavigationBar(
        containerColor = md_theme_dark_background,
        tonalElevation = 8.dp
//        contentColor = primaryColor
    ) {

        items.forEach { screen ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = primaryColor,
                    selectedIconColor = white
                ),
                icon = { Icon(painterResource(id = screen.icon), contentDescription = screen.title) },
                label = { Text(text = screen.title) },
                alwaysShowLabel = true,
                selected = selectedScreen == screen.route, //currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {

                        // seems to work
                        // https://github.com/scottschmitz/sample_compose_nav/blob/main/app/src/main/java/com/sschmitz/samplenav/ui/BottomNav.kt
                        if (currentDestination?.route != navController.graph.findStartDestination().route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = false
                                //saveState = true
                            }
                            launchSingleTop = true
                            //restoreState = true
                        } else {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                }
            )
        }

        preselectedScreen.value?.let {
            navController.navigate(route = it)
            preselectedScreen.value = null
        }
    }
}

@Composable
fun rememberDestinationChangeListener(navController: NavController, sharedViewModel: SharedViewModel): DestinationChangeListener {
    val destinationChangeListener = remember {
        DestinationChangeListener(sharedViewModel)
    }
    DisposableEffect(navController) {
        navController.addOnDestinationChangedListener(destinationChangeListener)
        onDispose {
            navController.removeOnDestinationChangedListener(destinationChangeListener)
        }
    }
    return destinationChangeListener
}

class DestinationChangeListener(private val sharedViewModel: SharedViewModel) :
    NavController.OnDestinationChangedListener, LifecycleObserver {

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        sharedViewModel.onDestinationChanged(destination.route)
    }
}


