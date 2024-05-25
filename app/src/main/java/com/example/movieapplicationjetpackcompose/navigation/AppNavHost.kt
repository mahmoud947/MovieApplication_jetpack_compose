package com.example.movieapplicationjetpackcompose.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movieapplicationjetpackcompose.ui.screens.favorite.FavoriteScreen
import com.example.movieapplicationjetpackcompose.ui.screens.favorite.FavoriteViewModel
import com.example.movieapplicationjetpackcompose.ui.screens.home.HomeScreen
import com.example.movieapplicationjetpackcompose.ui.screens.home.HomeViewModel


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavigationItem.Home.route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationItem.Home.route) {
            val viewModel: HomeViewModel = hiltViewModel()
            val state by viewModel.viewState.collectAsState()
            HomeScreen(
                state = state,
                onEvent = viewModel::setEvent,
                effect = viewModel.effect,
                navController = navController
            )
        }
        composable(NavigationItem.Favorite.route) {
            val viewModel: FavoriteViewModel = hiltViewModel()
            val state by viewModel.viewState.collectAsState()
            FavoriteScreen(
                state = state,
                onEvent = viewModel::setEvent,
                effect = viewModel.effect,
                navController = navController
            )
        }
        composable(NavigationItem.Details.route) {
        }
    }
}