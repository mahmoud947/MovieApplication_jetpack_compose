package com.example.movieapplicationjetpackcompose.ui.screens.favorite

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.core.base.ViewSideEffect
import com.example.domain.models.Movie
import com.example.movieapplicationjetpackcompose.components.MainAppBar
import com.example.movieapplicationjetpackcompose.components.MovieCard
import com.example.movieapplicationjetpackcompose.ui.dialogs.ErrorDialog
import com.example.movieapplicationjetpackcompose.ui.screens.home.OnEffect
import com.example.movieapplicationjetpackcompose.ui.theme.merriweatherFontFamily
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier, state: FavoriteContract.State,
    onEvent: (FavoriteContract.Event) -> Unit,
    effect: Flow<ViewSideEffect>,
    onNavigateToDetails: (Movie) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        onEvent(FavoriteContract.Event.FetchFavoriteMovies)
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val pullToRefreshState = rememberPullToRefreshState()


    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showSnackBar by remember { mutableStateOf(false) }
    var snackBarMessage by remember { mutableStateOf("") }

    effect.OnEffect { actions ->
        when (actions) {
            is FavoriteContract.SideEffects.ErrorMessageSideEffect -> {
                showErrorDialog = true
                errorMessage =
                    actions.message ?: "An unexpected error occurred. Please try again later."
            }

            is FavoriteContract.SideEffects.ShowSnackBar ->{
                showSnackBar = true
                snackBarMessage = actions.message?:""
            }
        }

    }
    if (showErrorDialog) {
        ErrorDialog(
            title = "Error",
            message = errorMessage,
            showDialog = showErrorDialog,
            onClick = {
                showErrorDialog = false
            },
            onDismiss = { showErrorDialog = false }
        )
    }

    if (showSnackBar){
        scope.launch {
            snackBarHostState.currentSnackbarData?.dismiss()
            snackBarHostState.showSnackbar(snackBarMessage,withDismissAction = true)
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState)},
        topBar = {
            MainAppBar(
                title = "Favorite Movie",
                searchWidgetState = state.searchWidgetState,
                searchTextState = state.searchQuery,
                onTextChange = {
                    onEvent(FavoriteContract.Event.OnSearchQueryChange(query = it))
                },
                onCloseClicked = {
                    onEvent(FavoriteContract.Event.OnCloseSearch)
                },
                onSearchClicked = {
                    onEvent(FavoriteContract.Event.OnSearchTriggered(query = it))
                },
                onSearchTriggered = {
                    onEvent(FavoriteContract.Event.OnOpenSearch)
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .nestedScroll(pullToRefreshState.nestedScrollConnection),
    ) { innerPadding ->

        Box(
            modifier = Modifier.padding(innerPadding),
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(180.dp),

                ) {
                item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                    Text(
                        text = "Favorite",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        fontFamily = merriweatherFontFamily,
                        modifier = Modifier.padding(24.dp)
                    )
                }
                if (pullToRefreshState.isRefreshing) {
                    onEvent(FavoriteContract.Event.FetchFavoriteMovies)
                }
                if (!state.loading) {
                    pullToRefreshState.endRefresh()
                }
                state.movies?.let { movies: List<Movie> ->
                    if (movies.isEmpty()) {
                        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                            Text(
                                text = "No favorite movies available",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontFamily = merriweatherFontFamily,
                                ),
                                modifier = Modifier.padding(24.dp)
                            )
                        }
                    } else {
                        items(movies) { movie ->
                            MovieCard(
                                movie = movie,
                                onFavoriteClick = { isFavorite ->
                                    if (isFavorite) {
                                        onEvent(FavoriteContract.Event.RemoveFromFavorite(movieId = movie.id))
                                    }
                                },
                                onClicked = {
                                    onNavigateToDetails(it)
                                }
                            )
                        }
                    }

                }
            }
            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = pullToRefreshState,
            )
        }
    }
}