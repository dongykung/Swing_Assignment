package com.dkproject.swingassignment.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun ImageCard(
    modifier: Modifier = Modifier,
    imageUrl: String,
    description: String?,
    likesCount: Long,
    myLike: Boolean,
    onClickLike: () -> Unit
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            //사진
            Image(
                painter = rememberAsyncImagePainter(
                    model = imageUrl,
                    contentScale = ContentScale.Crop
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            //좋아요 아이콘
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clip(RoundedCornerShape(bottomStart = 16.dp))
                    .background(
                        brush = Brush.horizontalGradient(listOf(Color.Black, Color.Black)),
                        alpha = 0.3f
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier=Modifier.padding(start=4.dp),
                    text = likesCount.toString(),
                    style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = { onClickLike() }) {
                    Icon(
                        imageVector = if (!myLike) Icons.Outlined.FavoriteBorder else Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = if (!myLike) Color.White else Color.Red
                    )
                }
            }

            //사진 설명
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.horizontalGradient(listOf(Color.Black, Color.Black)),
                        alpha = 0.3f
                    )
            ) {
                Text(
                    modifier = Modifier
                        .padding(6.dp)
                        .padding(start = 4.dp),
                    text = description ?: "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
            }
        }
    }


}