package com.synerise.integration.app.ui.components

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.synerise.integration.app.graphs.navigation.BottomNavigationScreens
import com.synerise.integration.app.ui.theme.BottomNavGray
import com.synerise.integration.app.ui.theme.BottomNavIndicator
import com.synerise.integration.app.ui.theme.FashionGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomNavigation(
    navController: NavController,
    items: List<BottomNavigationScreens>,
    cartItemsCount: Int
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = items.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {
        NavigationBar(
            contentColor = MaterialTheme.colorScheme.onBackground,
            containerColor = FashionGray
        ) {
            items.forEach { screen ->
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    label = { Text(stringResource(id = screen.resourceId)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BottomNavGray,
                        unselectedIconColor = BottomNavGray,
                        selectedTextColor = BottomNavGray,
                        indicatorColor = BottomNavIndicator
                    ),
                    onClick = {
                        navController.navigate(screen.route) {
                            launchSingleTop = true
                            restoreState = true

                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                        }
                    },
                    icon = {
                        if (screen.route == "cart_navbar") {
                            BadgedBox(badge = { TabBarBadgeView(cartItemsCount) }) {
                                Icon(screen.icon, contentDescription = null)
                            }
                        } else {
                            Icon(screen.icon, contentDescription = null)
                        }
                    })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBarBadgeView(count: Int? = null) {
    if (count != null) {
        Badge {
            Text(count.toString())
        }
    }
}