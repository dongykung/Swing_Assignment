package com.dkproject.swingassignment.ui.screen.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dkproject.swingassignment.R
import com.dkproject.swingassignment.model.UserLikeDTO
import com.dkproject.swingassignment.model.results
import com.dkproject.swingassignment.ui.component.CenterTopAppBarComponent
import com.dkproject.swingassignment.ui.component.ImageCard
import com.dkproject.swingassignment.ui.component.LoadStateFooter
import com.dkproject.swingassignment.ui.screen.home.EmptyResultScreen
import com.dkproject.swingassignment.ui.screen.home.HomeViewModel
import com.dkproject.swingassignment.ui.screen.home.LoadErrorScreen
import com.dkproject.swingassignment.ui.screen.home.LoadingScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyFavoriteScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
) {
    val state = viewModel.state.collectAsState().value
    val myLikeList = state.userLikeList.collectAsLazyPagingItems()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val empty = myLikeList.itemCount == 0 //아이템 검색 결과 개수가 0개라면 검색결과 없음 화면 출력
    val loading = myLikeList.loadState.refresh is LoadState.Loading //로딩중이라면 로딩 출력
    val error = myLikeList.loadState.refresh is LoadState.Error  // 에러->에러 출력
    val snackbarHostState = remember { SnackbarHostState() }
    MyFavoriteScreen(modifier=modifier,
        scrollBehavior = scrollBehavior,
        imageList = myLikeList,
        likeManageMap = state.likeManageMap,
        snackbarHostState = snackbarHostState,
        empty=empty,
        loading=loading,
        error=error,
        deleteLike = {
            viewModel.updateDeleteLike(it, snackbarHostState = snackbarHostState)
        },
        retryClick = {
            viewModel.loadUsersLikeImages()
        })

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyFavoriteScreen(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    imageList: LazyPagingItems<UserLikeDTO>,
    likeManageMap: Map<String, Boolean>,
    snackbarHostState:SnackbarHostState,
    empty:Boolean,
    loading:Boolean,
    error:Boolean,
    deleteLike:(UserLikeDTO)->Unit,
    retryClick:()->Unit,
    ) {

    Scaffold(modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)},
        topBar = {
            CenterTopAppBarComponent(title = "즐겨찾기", scrollBehavior = scrollBehavior)
        }) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding))
        {
            when{
                error->{
                    LoadErrorScreen(modifier = Modifier.align(Alignment.Center),
                        snackbarHostState = snackbarHostState,
                        retryClick = retryClick)
                }
                loading->{
                    LoadingScreen(modifier = Modifier.align(Alignment.Center))
                }
                empty->{
                    EmptyResultScreen(modifier = Modifier.padding(padding),text="좋아요 데이터가 존재하지 않습니다")
                }
                else->{
                    FavoriteImageSection(
                        modifier = Modifier,
                        imageList = imageList,
                        deleteLike = deleteLike,
                        likeManageMap = likeManageMap,
                        snackbarHostState=snackbarHostState
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteImageSection(
    modifier: Modifier = Modifier,
    imageList: LazyPagingItems<UserLikeDTO>, //내가 좋아요 누른 데이터 페이징
    deleteLike: (UserLikeDTO) -> Unit,      // 좋아요 삭제 리스너
    likeManageMap: Map<String, Boolean>,    // 좋아요 관리 데이터
    snackbarHostState:SnackbarHostState
) {
    LazyColumn(modifier = modifier) {
        items(imageList.itemCount) { index ->
            val image = imageList[index]
            image?.run {
                //좋아요 삭제가 되지 않은 데이터일 때만 그림
                if (likeManageMap[this.id] != false)
                    ImageCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .height(270.dp),
                        imageUrl = this.urls.full,
                        description = this.description?:this.slug,
                        myLike = this.liked_by_user,
                        likesCount = this.likes,
                        onClickLike = {
                            deleteLike(this)
                        }
                    )
            }
        }
        item {
            LoadStateFooter(loadState = imageList.loadState.append,
                snackbarHostState=snackbarHostState,
                retryClick = {
                imageList.retry()
            })
        }
    }
}



