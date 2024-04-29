package com.dkproject.swingassignment.ui.screen.home

import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.dkproject.swingassignment.model.results
import com.dkproject.swingassignment.ui.component.ImageCard
import com.dkproject.swingassignment.ui.component.LoadStateFooter

@Composable
fun QueryResultScreen(
    modifier: Modifier = Modifier,
    imageList: LazyPagingItems<results>,
    onClick: (results) -> Unit,
    likeManageMap:Map<String,Boolean>,
    likesCountMap:Map<String,Long>,
    snackbarHostState:SnackbarHostState
) {
    LazyColumn(modifier = modifier) {
        items(imageList.itemCount) { index ->
            val image = imageList[index]
            image?.run {
                //좋아요 관리 Map data 가 존재한다면 Map data 로 표시
                if(likeManageMap[this.id]!=null){ //getValue 위해 null체크
                    this.liked_by_user = likeManageMap.getValue(this.id)
                }
                //좋아요 카운트 관리 Map data 가 존재한다면 Map data 로 표시
                if(likesCountMap[this.id]!=null){//getValue 위해 null체크
                    this.likes = likesCountMap.getValue(this.id)
                }
                ImageCard(
                    modifier = Modifier
                        .height(270.dp)
                        .padding(12.dp),
                    imageUrl = this.urls.full,
                    description = this.description ?: this.slug, //설명 null 이면 slug 로 대체
                    myLike = this.liked_by_user,
                    likesCount = this.likes,
                    onClickLike = {
                        onClick(this)
                    }
                )
            }
        }
        item {
            LoadStateFooter(
                loadState = imageList.loadState.append,
                retryClick = {
                    imageList.retry()
                },
                snackbarHostState=snackbarHostState
            )
        }
    }
}