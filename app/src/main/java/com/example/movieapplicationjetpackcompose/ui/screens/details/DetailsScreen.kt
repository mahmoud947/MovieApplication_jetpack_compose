package com.example.movieapplicationjetpackcompose.ui.screens.details

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.core.base.ViewSideEffect
import com.example.data.mapper.toMovie
import com.example.domain.models.MovieDetails
import com.example.movieapplicationjetpackcompose.ui.dialogs.ErrorDialog
import com.example.movieapplicationjetpackcompose.ui.screens.details.componants.Chip
import com.example.movieapplicationjetpackcompose.ui.screens.details.componants.InformationSection
import com.example.movieapplicationjetpackcompose.ui.screens.details.componants.PosterSection
import com.example.movieapplicationjetpackcompose.ui.screens.details.componants.ShimmerMovieDetailsScreen
import com.example.movieapplicationjetpackcompose.ui.screens.home.OnEffect
import com.example.movieapplicationjetpackcompose.ui.theme.merriweatherFontFamily
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MovieDetailsScreen(
    modifier: Modifier = Modifier,
    state: DetailsContract.State,
    movieId: Int,
    onEvent: (DetailsContract.Event) -> Unit,
    effect: Flow<ViewSideEffect>,
    onNavigateBack: () -> Unit
) {

    LaunchedEffect(key1 = movieId) {
        onEvent(DetailsContract.Event.FetchMovieDetails(movieId = movieId))
        onEvent(DetailsContract.Event.FetchMovieVideos(movieId = movieId))
    }

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var showSnackBar by remember { mutableStateOf(false) }
    var snackBarMessage by remember { mutableStateOf("") }

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    SnackbarHost(hostState = snackBarHostState)

    effect.OnEffect { actions ->
        when (actions) {
            is DetailsContract.SideEffects.ErrorMessageSideEffect -> {
                showErrorDialog = true
                errorMessage =
                    actions.message ?: "An unexpected error occurred. Please try again later."
            }

            is DetailsContract.SideEffects.ShowSnackBar -> {
                scope.launch {
                    snackBarHostState.currentSnackbarData?.dismiss()
                    snackBarHostState.showSnackbar(snackBarMessage, withDismissAction = true)
                }
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


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {

        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())) {
            if (state.loading) {
                ShimmerMovieDetailsScreen()

            } else {

                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    PosterSection(state = state, onEvent = onEvent, onNavigateBack = onNavigateBack)
                    Column(modifier = Modifier.padding(all = 24.dp)) {

                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = state.movie?.title.orEmpty(),
                                style = MaterialTheme.typography.headlineSmall,
                                fontFamily = merriweatherFontFamily,
                                fontWeight = FontWeight.Bold
                            )

                            AnimatedVisibility(visible = state.isFavorite) {
                                IconButton(onClick = {
                                    onEvent(
                                        DetailsContract.Event.RemoveFromFavorite(
                                            state.movie?.id ?: 0
                                        )
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Bookmark,
                                        contentDescription = null,
                                        tint = Color.Yellow,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                            }

                            AnimatedVisibility(visible = !state.isFavorite) {
                                IconButton(onClick = {
                                    state.movie?.toMovie()?.let {
                                        onEvent(DetailsContract.Event.AddToFavorite(it))
                                    }

                                }) {
                                    Icon(
                                        imageVector = Icons.Outlined.BookmarkBorder,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "⭐ ${state.movie?.voteAverage}/10 IMDb",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row {
                            state.movie?.genres.let { genres: List<MovieDetails.Genre>? ->
                                genres?.forEach {
                                    Chip(text = it.name.orEmpty())
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        InformationSection(state = state)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Rating: ${state.movie?.voteAverage}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Description",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.movie?.overview.orEmpty(),
                            style = MaterialTheme.typography.bodyMedium
                        )

                    }
                }
            }
        }
    }
}


