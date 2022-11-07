package com.owusu.cryptosignalalert.views.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavigationBar(navController) },
        content = { padding -> // We have to pass the scaffold inner padding to our content. That's why we use Box.
            Box(modifier = Modifier.padding(padding)) {
                if (1 > 0) {
                    HomeNavGraph(navHostController = navController)
                } else {
                    Column() {
                        Row(modifier = Modifier.weight(0.9f)) {
                            HomeNavGraph(navHostController = navController)
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

@Composable
fun TopBar() {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name), fontSize = 18.sp) },
        backgroundColor = colorResource(id = R.color.colorPrimary),
        contentColor = Color.White
    )
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar()
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
                unselectedContentColor = Color.White.copy(0.4f),
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

