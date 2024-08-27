package com.synerise.integration.app.graphs.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.synerise.integration.app.R

sealed class BottomNavigationScreens(val route: String, val resourceId: Int, val icon: ImageVector) {
    data object Home: BottomNavigationScreens("home_navbar", R.string.home_navbar_label, Icons.Outlined.Home)
    data object Products: BottomNavigationScreens("products_navbar", R.string.products_navbar_label, Icons.Outlined.Apps)
    data object Favourites: BottomNavigationScreens("favouritres_navbar", R.string.fav_navbar_label, Icons.Outlined.FavoriteBorder)
    data object Cart: BottomNavigationScreens("cart_navbar", R.string.cart_navbar_label, Icons.Outlined.ShoppingCart)
    data object Account: BottomNavigationScreens("account_navbar", R.string.account_navbar_label, Icons.Outlined.Person)
}