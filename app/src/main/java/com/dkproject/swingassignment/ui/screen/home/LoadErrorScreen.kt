package com.dkproject.swingassignment.ui.screen.home

import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dkproject.swingassignment.R
import kotlinx.coroutines.launch


@Composable
fun LoadErrorScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    retryClick: () -> Unit,
) {
    LaunchedEffect(true) {
       val result =  snackbarHostState.showSnackbar(
            message = "데이터 로드에 실패하였습니다.",
            actionLabel = "닫기",
            duration = SnackbarDuration.Short
        )
        when(result){
            SnackbarResult.ActionPerformed -> {}
            SnackbarResult.Dismissed -> {}
        }
    }
    Button(modifier = modifier, onClick = retryClick) {
        Text(text = stringResource(id = R.string.retry))
    }
}