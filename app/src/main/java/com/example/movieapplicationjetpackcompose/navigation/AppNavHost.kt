package com.example.movieapplicationjetpackcompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.movieapplicationjetpackcompose.ui.screens.details.DetailsViewModel
import com.example.movieapplicationjetpackcompose.ui.screens.details.MovieDetailsScreen
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
                onNavigateToDetails = { movie ->
                    navController.navigate(
                        NavigationItem.Details.passMovieData(
                            isFavorite = movie.isFavorite,
                            movieID = movie.id
                        )
                    )
                }
            )
        }

        composable(NavigationItem.Favorite.route) {
            val viewModel: FavoriteViewModel = hiltViewModel()
            val state by viewModel.viewState.collectAsState()
            FavoriteScreen(
                state = state,
                onEvent = viewModel::setEvent,
                effect = viewModel.effect,
                onNavigateToDetails = { movie ->
                    navController.navigate(
                        NavigationItem.Details.passMovieData(
                            isFavorite = movie.isFavorite,
                            movieID = movie.id
                        )
                    )
                }
            )
        }

        composable(
            route = NavigationItem.Details.route,
            arguments = listOf(
                navArgument(DetailsArgs.MOVIE_ID.name) { type = NavType.IntType },
                navArgument(DetailsArgs.IS_FAVORITE.name) { type = NavType.BoolType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt(DetailsArgs.MOVIE_ID.name) ?: 0
            val isFavorite =
                backStackEntry.arguments?.getBoolean(DetailsArgs.IS_FAVORITE.name) ?: false

            val viewModel: DetailsViewModel = hiltViewModel()
            val state by viewModel.viewState.collectAsState()
            MovieDetailsScreen(
                state = state,
                onEvent = viewModel::setEvent,
                effect = viewModel.effect,
                onNavigateBack = {
                                 navController.popBackStack(NavigationItem.Home.route,false)
                },
                isFavorite = isFavorite,
                movieId = movieId
            )
        }
    }
}

