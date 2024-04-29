package com.dkproject.swingassignment.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import com.dkproject.swingassignment.ui.screen.home.LoadErrorScreen
import com.dkproject.swingassignment.ui.screen.home.LoadingScreen

@Composable
fun LoadStateFooter(
    modifier: Modifier = Modifier,
    loadState: LoadState,
    snackbarHostState: SnackbarHostState,
    retryClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        when (loadState) {
            //페이징 에러일 시
            is LoadState.Error -> {
                LoadErrorScreen(retryClick = retryClick)
            }
            //페이징 로드중
            is LoadState.Loading -> {
                LoadingScreen()
            }
            //더 이상 데이터가 없다면 = 마지막 데이터라면
            else -> {
                LaunchedEffect(true) {
                    val snackbar = snackbarHostState.showSnackbar(message="마지막 데이터 입니다",
                        duration = SnackbarDuration.Short,
                        actionLabel = "닫기")
                    when(snackbar){
                        SnackbarResult.Dismissed ->{}
                        SnackbarResult.ActionPerformed -> {}
                    }
                }
            }
        }
    }
}