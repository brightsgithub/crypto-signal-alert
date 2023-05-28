package com.owusu.cryptosignalalert.views.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.owusu.cryptosignalalert.R
import com.owusu.cryptosignalalert.navigation.Graphs
import com.owusu.cryptosignalalert.navigation.HomeNavGraph
import com.owusu.cryptosignalalert.navigation.NavigationItem
import com.owusu.cryptosignalalert.viewmodels.SharedViewModel
import org.koin.androidx.compose.getViewModel

// Since bottom bar uses its own NavHost, we have to pass it a new NavHostController
@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {

    val onSearchBarClick = {
        navController.navigate(route = Graphs.SEARCH_NAV_GRAPH)
    }

    Scaffold(
        topBar = { TopBar(onSearchBarClick = onSearchBarClick) },
        bottomBar = { BottomNavigationBar(navController) },
        content = { padding -> // We have to pass the scaffold inner padding to our content. That's why we use Box.
            Box(modifier = Modifier.padding(padding)) {
                if (1 > 0) {
                    HomeNavGraph(navHostController = navController, onSearchBarClick = onSearchBarClick)
                } else {
                    Column() {
                        Row(modifier = Modifier.weight(0.9f)) {
                            HomeNavGraph(navHostController = navController, onSearchBarClick = onSearchBarClick)
                        }
                        Row(modifier = Modifier.weight(0.1f)) {
                            Box() {
                                Text(text = "Ads Banner", modifier = Modifier.align(Alignment.Center))
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
fun TopBar(onSearchBarClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name), fontSize = 18.sp) },
        backgroundColor = colorResource(id = R.color.colorPrimary),
        contentColor = Color.White,
        actions = {
            IconButton(
                modifier = Modifier,
                onClick = { onSearchBarClick() }) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = stringResource(id = R.string.icn_search_view_demo_app_bar_search)
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar(onSearchBarClick = {})
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
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
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
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
    }
}


