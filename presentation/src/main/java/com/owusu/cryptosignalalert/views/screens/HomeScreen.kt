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
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.navigation.*
import com.owusu.cryptosignalalert.viewmodels.SharedViewModel
import org.koin.androidx.compose.getViewModel

// Since bottom bar uses its own NavHost, we have to pass it a new NavHostController
@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    preselectedScreen: MutableState<String?>
) {
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

    val sharedViewState = sharedViewModel.sharedViewState.collectAsState(initial = SharedViewState())

    val scaffoldState = rememberScaffoldState()
    val snackbarHostState = scaffoldState.snackbarHostState

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


    val onShowSnackBar = { msg: String, actionLabel: String, actionCallback:() -> Unit ->
        sharedViewModel.showSnackBar(msg, actionLabel, actionCallback)
    }

    val onHideSnackBar = {  ->
        sharedViewModel.hideSnackBar()
    }



    // If the UI state contains an error, show snackbar
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

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = { TopBar(onSearchBarClick = onSearchBarClick, onSettingsClicked = onSettingsClicked, sharedViewState) },
        bottomBar = { BottomNavigationBar(navController, preselectedScreen) },
        content = { padding -> // We have to pass the scaffold inner padding to our content. That's why we use Box.
            Box(modifier = Modifier.padding(padding)) {
                if (sharedViewState.value.purchasedState.isAdsPurchased) {
                    HomeNavGraph(
                        navHostController = navController,
                        onSearchBarClick = onSearchBarClick,
                        onShowSnackBar = onShowSnackBar
                    )
                } else {
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
                                //Text(text = "Ads Banner", modifier = Modifier.align(Alignment.Center))
                                AndroidView(factory = {
                                    AdView(it).apply {
                                        adSize = AdSize.BANNER
                                        adUnitId = "ca-app-pub-3940256099942544/6300978111"
                                        loadAd(AdRequest.Builder().build())
                                    }
                                }, modifier = Modifier.fillMaxSize())
                            }
                        }
                    }
                }
            }
        },
        backgroundColor = colorResource(R.color.colorPrimary) // Set background color to avoid the white flashing when you switch between screens
    )
}

// https://johncodeos.com/how-to-add-search-in-list-with-jetpack-compose/
// https://www.devbitsandbytes.com/configuring-searchview-in-jetpack-compose/
@Composable
fun TopBar(onSearchBarClick: () -> Unit, onSettingsClicked: () -> Unit, sharedViewState: State<SharedViewState>) {

    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    TopAppBar(
        title = { Text(text = sharedViewState.value.actionButtonState.title, fontSize = 18.sp) },
        backgroundColor = colorResource(id = R.color.colorPrimary),
        contentColor = Color.White,
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

    BottomNavigation(
        backgroundColor = colorResource(id = R.color.colorPrimary),
        contentColor = Color.White
    ) {

        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = screen.icon), contentDescription = screen.title) },
                label = { Text(text = screen.title) },
                selectedContentColor = Color.White,
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
        sharedViewModel.handleToolBarIconVisibility(destination.route)
    }
}


