package com.dkproject.swingassignment.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dkproject.swingassignment.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenterTopAppBarComponent(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    onBack: Boolean = false,
    onBackClick: () -> Unit = {}
) {
    //중앙 정렬 탑 바
    CenterAlignedTopAppBar(title = { Text(text = title) },
        navigationIcon = {
            if (onBack)  //true 일 시 뒤로가기 버튼 그리기
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack, contentDescription = stringResource(
                            id = R.string.arrowback
                        )
                    )
                }
        },
        scrollBehavior = scrollBehavior)
}