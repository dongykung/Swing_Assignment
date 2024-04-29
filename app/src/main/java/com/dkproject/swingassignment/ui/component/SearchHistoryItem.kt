package com.dkproject.swingassignment.ui.component

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SearchHistoryItem(
    modifier: Modifier = Modifier,
    text: String,
    removeHistory:(String)->Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.History, contentDescription = null)
        Spacer(modifier = Modifier.width(12.dp))
        //검색어
        Text(text = text)
        Spacer(modifier = Modifier.weight(1f))
        //검색어 삭제 아이콘
        IconButton(onClick = { removeHistory(text)}) {
            Icon(imageVector = Icons.Default.Clear, contentDescription = null)
        }
    }
}