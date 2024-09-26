package com.synerise.integration.app.graphs

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.synerise.integration.app.cart.CartScreen
import com.synerise.integration.app.favourites.FavouritesScreen
import com.synerise.integration.app.graphs.navigation.BottomNavigationScreens
import com.synerise.integration.app.home.HomeContainerScaffold
import com.synerise.integration.app.home.HomeContainerViewModel
import com.synerise.integration.app.home.HomeScreen
import com.synerise.integration.app.product.ProductScreen
import com.synerise.integration.app.products.ProductsScreen

@Composable
fun HomeNavigationGraph() {
    val navHostController = rememberNavController()
    val sharedViewModel: HomeContainerViewModel = hiltViewModel()

    NavHost(
        navController = navHostController,
        route = Graph.HOME,
        startDestination = BottomNavigationScreens.Home.route
    ) {
        composable(
            route = HomeBaseScreen.ProductDetailScreen.route,
            arguments = listOf(navArgument("sku") { type = NavType.StringType })
        ) { entry ->
            ProductScreen(onProductClick = { product ->
                navHostController.navigate(
                    HomeBaseScreen.ProductDetailScreen.route.replace(
                        oldValue = "{sku}",
                        product.sku
                    )
                )
            })
        }

        composable(route = BottomNavigationScreens.Home.route) {
            HomeContainerScaffold(sharedViewModel = sharedViewModel, navController = navHostController, screenContent = {
                HomeScreen(
                   paddingValues = it,
                    onProductClick = { product ->
                        navHostController.navigate(
                            HomeBaseScreen.ProductDetailScreen.route.replace(
                                oldValue = "{sku}",
                                product.sku
                            )
                        )
                    })
            })
        }
        composable(route = BottomNavigationScreens.Products.route) {
            HomeContainerScaffold(sharedViewModel = sharedViewModel, navController = navHostController, screenContent = {
                ProductsScreen(
                    paddingValues = it,
                    onProductClick = { product ->
                        navHostController.navigate(
                            HomeBaseScreen.ProductDetailScreen.route.replace(
                                oldValue = "{sku}",
                                product.sku
                            )
                        )
                    })
            })
        }
        composable(route = BottomNavigationScreens.Favourites.route) {
            HomeContainerScaffold(sharedViewModel = sharedViewModel, navController = navHostController, screenContent = {
                FavouritesScreen(paddingValues = it)
            })
        }
        composable(route = BottomNavigationScreens.Cart.route) {
            HomeContainerScaffold(sharedViewModel = sharedViewModel, navController = navHostController, screenContent = {
                CartScreen(paddingValues = it, onGoToHomeClicked = {
                    navHostController.navigate(
                        BottomNavigationScreens.Home.route
                    ) {
                        popUpTo(BottomNavigationScreens.Home.route) {
                            inclusive = true
                        }
                    }
                })
            })
        }
        composable(route = BottomNavigationScreens.Account.route) {
            AccountNavigationGraph(
                navHostController = navHostController,
                sharedViewModel = sharedViewModel,
            )
        }
    }
}

sealed class HomeBaseScreen(val route: String) {
    data object ProductDetailScreen : HomeBaseScreen(route = "product_detail/{sku}")
}