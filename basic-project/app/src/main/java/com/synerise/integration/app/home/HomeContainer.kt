package com.synerise.integration.app.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.synerise.integration.app.graphs.navigation.BottomNavigationScreens
import com.synerise.integration.app.home.topbar.HomeTopBar
import com.synerise.integration.app.ui.components.CustomBottomNavigation
import timber.log.Timber

@Composable
fun HomeContainerScaffold(
    sharedViewModel: HomeContainerViewModel,
    navController: NavHostController,
    screenContent: @Composable (padding: PaddingValues) -> Unit
) {
    val uiState by sharedViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = uiState.cartCount) {
        sharedViewModel.checkCartStatus()
    }

    val searchWidgetState by sharedViewModel.searchWidgetState
    val searchTextState by sharedViewModel.searchTextState

    val bottomNavigationItems = listOf(
        BottomNavigationScreens.Home,
        BottomNavigationScreens.Products,
        BottomNavigationScreens.Favourites,
        BottomNavigationScreens.Cart,
        BottomNavigationScreens.Account,
    )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HomeTopBar(
                searchWidgetState = searchWidgetState,
                searchTextState = searchTextState,
                onTextChange = { Timber.d("onTextChanged") },
                onCloseClicked = { Timber.d("Close clicked") },
                onSearchClicked = { Timber.d("On Search clicked") },
                onSearchTriggered = { Timber.d("On Search triggered") }
            )
        },
        bottomBar = {
            CustomBottomNavigation(
                navController = navController,
                items = bottomNavigationItems,
                cartItemsCount = uiState.cartCount
            )
        }
    ) { padding ->
        screenContent(padding)
    }
}