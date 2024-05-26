package com.example.movieapplicationjetpackcompose.ui.screens.details.componants

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

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

