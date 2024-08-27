package com.synerise.integration.app.graphs

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.synerise.integration.app.LocalUserState
import com.synerise.integration.app.account.AccountScreen
import com.synerise.integration.app.home.HomeContainerScaffold
import com.synerise.integration.app.home.HomeContainerViewModel
import com.synerise.integration.app.login.LoginScreen
import com.synerise.integration.app.login.SignUpScreen

@Composable
fun AccountNavigationGraph(
    navHostController: NavHostController, sharedViewModel: HomeContainerViewModel
) {
    val userState = LocalUserState.current
    val accountNavHostController = rememberNavController()
    NavHost(
        navController = accountNavHostController,
        route = Graph.ACCOUNT,
        startDestination = if (userState.isLoggedIn) AccountContextScreen.AccountSettings.route else AccountContextScreen.LoginScreen.route
    ) {
        composable(route = AccountContextScreen.LoginScreen.route) {
            HomeContainerScaffold(
                sharedViewModel = sharedViewModel,
                navController = navHostController,
                screenContent = {
                    LoginScreen(paddingValues = it, onSignUp = {
                        accountNavHostController.navigate(AccountContextScreen.SignUpScreen.route)
                    })
                })
        }
        composable(route = AccountContextScreen.SignUpScreen.route) {
            HomeContainerScaffold(
                sharedViewModel = sharedViewModel,
                navController = navHostController,
                screenContent = {
                    SignUpScreen(paddingValues = it, onAccountCreate = {
                        accountNavHostController.navigate(AccountContextScreen.AccountSettings.route)
                    })
                })
        }
        composable(route = AccountContextScreen.AccountSettings.route) {
            BackHandler(true) {

            }
            HomeContainerScaffold(
                sharedViewModel = sharedViewModel,
                navController = navHostController,
                screenContent = {
                    AccountScreen(paddingValues = it)
                })
        }
    }
}

sealed class AccountContextScreen(val route: String) {
    data object LoginScreen : AccountContextScreen(route = "login")
    data object SignUpScreen : AccountContextScreen(route = "signUp")
    data object AccountSettings : AccountContextScreen(route = "account_settings")
}