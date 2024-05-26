package com.example.movieapplicationjetpackcompose.ui.screens.details

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.core.base.ViewSideEffect
import com.example.domain.models.MovieDetails
import com.example.movieapplicationjetpackcompose.ui.screens.details.componants.Chip
import com.example.movieapplicationjetpackcompose.ui.theme.merriweatherFontFamily
import com.example.movieapplicationjetpackcompose.ui.theme.mulishFontFamily
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.flow.Flow
import androidx.compose.animation.AnimatedVisibility

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    modifier: Modifier = Modifier,
    state: DetailsContract.State,
    movieId: Int,
    isFavorite: Boolean,
    onEvent: (DetailsContract.Event) -> Unit,
    effect: Flow<ViewSideEffect>,
    navController: NavController
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = movieId) {
        onEvent(DetailsContract.Event.FetchMovieDetails(movieId = movieId))
        onEvent(DetailsContract.Event.FetchMovieVideos(movieId = movieId))
    }

    Scaffold(
        topBar = {

        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Box {
                    androidx.compose.animation.AnimatedVisibility(visible = !state.isVideoStarted) {
                        AsyncImage(
                            model = state.movie?.backdropUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    androidx.compose.animation.AnimatedVisibility(visible = state.isVideoStarted) {
                        state.videos?.first()?.key?.let {
                            YouTubePlayer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                videoKey = it
                            )
                        }
                    }
                    TopAppBar(
                        title = { },
                        navigationIcon = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                IconButton(onClick = {

                                }) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }

                                IconButton(onClick = {
                                }) {
                                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                                }
                            }

                        },
                        colors = TopAppBarColors(
                            containerColor = Color.Transparent,
                            actionIconContentColor = Color.White,
                            titleContentColor = Color.Gray,
                            navigationIconContentColor = Color.White,
                            scrolledContainerColor = Color.White
                        )
                    )

                    if (state.videos?.isNotEmpty() == true) {
                        IconButton(
                            onClick = {
                                onEvent(DetailsContract.Event.OnStartVideo)
                            },
                            modifier = Modifier
                                .align(Alignment.Center)
                                .background(Color.Black.copy(alpha = 0.7f), shape = CircleShape)
                        ) {
                            Icon(
                                Icons.Filled.PlayArrow,
                                contentDescription = "Play Trailer",
                                tint = Color.White
                            )
                        }
                    }
                }
                Column(modifier = Modifier.padding(all = 24.dp)) {


                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = state.movie?.title.orEmpty(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = merriweatherFontFamily,
                        fontWeight = FontWeight.Bold
                    )
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
                    Row {
                        Column {
                            Text(
                                text = "Language",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Light,
                                fontFamily = merriweatherFontFamily,
                            )

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = state.movie?.originalLanguage.orEmpty(),
                                style = MaterialTheme.typography.bodyLarge.copy(fontFamily = mulishFontFamily),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
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


@Composable
fun YouTubePlayer(videoKey: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val youTubePlayerView = remember { YouTubePlayerView(context) }

    DisposableEffect(Unit) {
        val listener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(videoKey, 0f)
            }
        }
        youTubePlayerView.addYouTubePlayerListener(listener)
        onDispose {
            youTubePlayerView.release()
        }
    }

    AndroidView(
        factory = {
            youTubePlayerView.apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        },
        modifier = modifier
    )
}

