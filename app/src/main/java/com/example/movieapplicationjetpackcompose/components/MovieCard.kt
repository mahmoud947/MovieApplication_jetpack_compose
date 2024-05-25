package com.example.movieapplicationjetpackcompose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.movieapplicationjetpackcompose.R


@Composable
fun MovieCard(
    movieImageUrl: String,
    title: String,
    rating: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .padding(16.dp)
            .clickable { }
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
            modifier = Modifier

                .fillMaxWidth()
        ) {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(movieImageUrl),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )

                Icon(
                    painter = painterResource(id = if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(24.dp)
                        .clickable { onFavoriteClick() }
                )

            }

        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_star),
                contentDescription = null,
                tint = Color.Yellow,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = rating,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}


@Preview
@Composable
private fun MovieCardPrev() {
    MovieCard(
        movieImageUrl = "data:image/jpeg;base64,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",
        title = "Spider-Man",
        rating = "2",
        isFavorite = true
    ) {

    }
}