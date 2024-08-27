package com.synerise.integration.app.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    NavHost(navController = navController,
        route =  Graph.ROOT,
        startDestination = Graph.HOME) {
        composable(route = Graph.HOME) {
            HomeNavigationGraph()
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val HOME = "home_graph"
    const val ACCOUNT = "account_graph"
}