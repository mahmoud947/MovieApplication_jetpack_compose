package com.example.movieapplicationjetpackcompose.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.core.base.ViewSideEffect
import com.example.domain.models.Movie
import com.example.movieapplicationjetpackcompose.components.MainAppBar
import com.example.movieapplicationjetpackcompose.components.MovieCard
import com.example.movieapplicationjetpackcompose.ui.dialogs.ErrorDialog
import com.example.movieapplicationjetpackcompose.ui.dialogs.ShowSnackBar
import com.example.movieapplicationjetpackcompose.ui.dialogs.rememberSnackbarHostStateWithLifecycle
import com.example.movieapplicationjetpackcompose.ui.theme.merriweatherFontFamily
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, state: HomeContract.State,
    onEvent: (HomeContract.Event) -> Unit,
    effect: Flow<ViewSideEffect>,
    onNavigateToDetails: (Movie) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        onEvent(HomeContract.Event.FetchMovies)
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val pullToRefreshState = rememberPullToRefreshState()

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val snackBarHostState = rememberSnackbarHostStateWithLifecycle()
    val context = LocalContext.current

    var showSnackBar by remember { mutableStateOf(false) }
    var snackBarMessage by remember { mutableStateOf("") }

    effect.OnEffect { action ->
        when (action) {
            is HomeContract.SideEffects.ErrorMessageSideEffect -> {
                showErrorDialog = true
                errorMessage = action.message ?: "An unexpected error occurred. Please try again later."
            }
            is HomeContract.SideEffects.ShowSnackBar -> {
                showSnackBar = true
                snackBarMessage = action.message ?: ""
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
        context.ShowSnackBar(
            snackbarHostState = snackBarHostState,
            message = snackBarMessage,
            actionLabel = "Dismiss",
            onActionClick = {  }
        )
    }

    Scaffold(
        topBar = {
            MainAppBar(
                searchWidgetState = state.searchWidgetState,
                searchTextState = state.searchQuery,
                onTextChange = {
                    onEvent(HomeContract.Event.OnSearchQueryChange(query = it))
                },
                onCloseClicked = {
                    onEvent(HomeContract.Event.OnCloseSearch)
                },
                onSearchClicked = {
                    onEvent(HomeContract.Event.OnSearchTriggered(query = it))
                },
                onSearchTriggered = {
                    onEvent(HomeContract.Event.OnOpenSearch)
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
                        text = "Popular",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        fontFamily = merriweatherFontFamily,
                        modifier = Modifier.padding(24.dp)
                    )
                }
                if (pullToRefreshState.isRefreshing) {
                    onEvent(HomeContract.Event.FetchMovies)
                }
                if (!state.loading) {
                    pullToRefreshState.endRefresh()
                }
                state.movies?.let { movies: List<Movie> ->
                    items(movies) { movie ->
                        MovieCard(
                            movie = movie,
                            onFavoriteClick = { isFavorite ->
                                if (!isFavorite) {
                                    onEvent(HomeContract.Event.AddToFavorite(movie = movie))
                                } else {
                                    onEvent(HomeContract.Event.RemoveFromFavorite(movieId = movie.id))
                                }
                            },
                            onClicked = {
                                onNavigateToDetails(it)
                            }
                        )
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

@Composable
fun Flow<ViewSideEffect>.OnEffect(action: (effect: ViewSideEffect) -> Unit) {
    LaunchedEffect(Unit) {
        onEach { effect ->
            action(effect)
        }.collect()
    }
}