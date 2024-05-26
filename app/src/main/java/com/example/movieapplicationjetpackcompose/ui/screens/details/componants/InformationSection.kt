package com.example.movieapplicationjetpackcompose.ui.screens.details.componants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.core.utils.toHoursAndMinutes
import com.example.movieapplicationjetpackcompose.ui.screens.details.DetailsContract
import com.example.movieapplicationjetpackcompose.ui.theme.merriweatherFontFamily
import com.example.movieapplicationjetpackcompose.ui.theme.mulishFontFamily

@Composable
fun InformationSection (modifier: Modifier = Modifier,state:DetailsContract.State) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = "Length",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.W400,
                fontFamily = merriweatherFontFamily,
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = state.movie?.runtime?.toHoursAndMinutes().orEmpty(),
                style = MaterialTheme.typography.bodyLarge.copy(fontFamily = mulishFontFamily),
            )
        }

        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = "Language",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.W400,
                fontFamily = merriweatherFontFamily,
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = state.movie?.originalLanguage.orEmpty(),
                style = MaterialTheme.typography.bodyLarge.copy(fontFamily = mulishFontFamily),
            )
        }


        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = "Release date",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.W400,
                fontFamily = merriweatherFontFamily,
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = state.movie?.releaseDate.orEmpty(),
                style = MaterialTheme.typography.bodyLarge.copy(fontFamily = mulishFontFamily),
            )
        }
    }
}
