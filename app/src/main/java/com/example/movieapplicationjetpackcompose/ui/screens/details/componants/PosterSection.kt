package com.example.movieapplicationjetpackcompose.ui.screens.details.componants

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.movieapplicationjetpackcompose.ui.screens.details.DetailsContract

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosterSection(
    modifier: Modifier = Modifier,
    state: DetailsContract.State,
    onEvent: (DetailsContract.Event) -> Unit,
    onNavigateBack:()->Unit
) {

    Box {
        AnimatedVisibility(visible = !state.isVideoStarted) {
            AsyncImage(
                model = state.movie?.backdropUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )
        }
        AnimatedVisibility(visible = state.isVideoStarted) {
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
                        onNavigateBack()
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
            AnimatedVisibility(
                visible = !state.isVideoStarted, modifier = Modifier.align(
                    Alignment.Center,
                )
            ) {
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

            AnimatedVisibility(visible = state.isVideoStarted) {
                IconButton(
                    onClick = {
                        onEvent(DetailsContract.Event.OnStopVideo)
                    },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.7f), shape = CircleShape)
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Close Trailer",
                        tint = Color.White
                    )
                }
            }

        }
    }
}