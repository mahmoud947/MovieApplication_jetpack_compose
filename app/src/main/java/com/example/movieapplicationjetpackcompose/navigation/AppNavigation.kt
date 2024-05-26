package com.example.movieapplicationjetpackcompose.navigation

enum class DetailsArgs {
    MOVIE_ID,
    IS_FAVORITE
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
        NavigationItem("${Screen.DETAILS.name}/{${DetailsArgs.MOVIE_ID.name}}/{${DetailsArgs.IS_FAVORITE.name}}") {
        fun passMovieData(movieID: Int, isFavorite: Boolean) =
            "${Screen.DETAILS.name}/$movieID/$isFavorite"
    }
}