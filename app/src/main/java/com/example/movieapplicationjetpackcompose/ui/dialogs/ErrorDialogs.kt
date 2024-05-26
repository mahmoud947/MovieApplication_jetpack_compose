package com.example.movieapplicationjetpackcompose.ui.dialogs


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog


@Composable
fun ErrorDialog(
    modifier: Modifier = Modifier,
    message: String = "",
    showDialog: Boolean = true,
    title: String = "Error",
    onClick: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            content = {
                Card(
                    modifier = modifier.shadow(
                        elevation = 4.dp,
                        shape = MaterialTheme.shapes.medium,
                        clip = true,
                        ambientColor = Color.Gray,
                        spotColor = Color.Gray
                    ),
                    elevation = CardDefaults.cardElevation(8.dp),
                    shape = RoundedCornerShape(16.dp),
                    content = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.surface
                                )
                                .padding(top = 50.dp, bottom = 16.dp, start = 32.dp, end = 32.dp)
                        ) {
                            Text(
                                text = title,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 24.sp,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = message,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            TextButton(
                                onClick = onClick,
                                modifier = Modifier
                                    .width(122.dp)
                                    .height(50.dp),

                            ){
                                Text(text = "Ok")
                            }
                        }
                    }
                )
            }
        )
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewErrorDialog() {
    ErrorDialog(
        message = "",
        modifier = Modifier.height(250.dp),
        title = "Title",
        onClick = {},
    )
}