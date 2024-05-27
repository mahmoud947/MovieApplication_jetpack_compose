package com.example.movieapplicationjetpackcompose.shared

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerMovieList(
    isLoading: Boolean,
    contentAfterLoading: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isLoading) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(180.dp),
        ) {
            items(10){
                ShimmerMovieCard()
            }
        }
    } else {
        contentAfterLoading()
    }
}
