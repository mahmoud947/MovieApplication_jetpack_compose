package com.example.movieapplicationjetpackcompose.navigation

enum class DetailsArgs {
    MOVIE_ID,
}

enum class Screen {
    HOME,
    FAVORITE,
    DETAILS,
}

sealed class NavigationItem(val route: String) {
    data object Home : NavigationItem(Screen.HOME.name)
    data object Favorite : NavigationItem(Screen.FAVORITE.name)
    data object Details :
        NavigationItem("${Screen.DETAILS.name}/{${DetailsArgs.MOVIE_ID.name}}") {
        fun passMovieData(movieID: Int) =
            "${Screen.DETAILS.name}/$movieID"
    }
}