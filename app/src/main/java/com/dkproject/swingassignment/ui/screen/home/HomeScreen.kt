package com.dkproject.swingassignment.ui.screen.home

import android.util.Log
import androidx.collection.emptyFloatList
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.dkproject.swingassignment.R
import com.dkproject.swingassignment.model.Urls
import com.dkproject.swingassignment.model.results
import com.dkproject.swingassignment.ui.component.ImageCard
import com.dkproject.swingassignment.ui.component.SearchBarComponent
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {
    Log.d("TAG", "HomeScreen: ")
    val state = viewModel.state.collectAsState().value
    //페이징 데이터
    val imageList: LazyPagingItems<results> = state.imageList.collectAsLazyPagingItems()
    //검색 기록 상태
    val searchState = viewModel.searchHistoryState.collectAsState().value
    val empty = imageList.itemCount == 0 //아이템 검색 결과 개수가 0개라면 검색결과 없음 화면 출력
    val loading = imageList.loadState.refresh is LoadState.Loading //로딩중이라면 로딩 출력
    val error = imageList.loadState.refresh is LoadState.Error  // 에러->에러 출력
    //검색어
    var searchText by rememberSaveable {
        mutableStateOf("")
    }
    //스낵바 상태
    val snackbarHostState = remember { SnackbarHostState() }
    HomeScreen(
        modifier = modifier,
        searchText = searchText,
        searchhistory = searchState.searchHistoryList,
        onSearch = { query ->
            if(query.isNotEmpty()) {
                searchText=query.trim()
                viewModel.setSearchHistory(query.trim())
                viewModel.query(query.trim())
            }
        },
        imageList = imageList,
        empty = empty,
        loading = loading,
        error = error,
        snackbarHostState = snackbarHostState,
        onClick = {result->
            viewModel.feadHeartClick(result,snackbarHostState)
                  },
        removeHistory = {viewModel.deleteSearchHistory(it)},
        searchTextChange = { searchText = it },
        retryClick = {
            // searchText 빈 값이라면 없다면 랜덤 이미지 재호출
            if(searchText.isEmpty()) viewModel.initRandomImage()
            // 검색어가 잇었다면 검색어 다시 호출
            else viewModel.query(searchText)
        },
        likeManageMap = state.likeManageMap,
        likesCountMap=state.likesCountManageMap
    )
}


@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    searchText: String,                    //검색어
    empty: Boolean,                        //검색 결과 없는지 판별
    loading: Boolean,                      //로딩 중
    error: Boolean,                        //에러 판별
    searchhistory: List<String>,           //검색 기록 리스트
    imageList: LazyPagingItems<results>,   //검색 결과 이미지 리스트
    likeManageMap:Map<String,Boolean>,     // 좋아요 데이터 관리 Map
    likesCountMap:Map<String,Long>,        // 좋아요 수 데이터 관리 Map
    snackbarHostState: SnackbarHostState,  // 스케폴드 스낵바 스테이트
    searchTextChange: (String) -> Unit,    //검색어 값 변경
    onSearch: (String) -> Unit,            //검색 요청
    onClick: (results) -> Unit,            //사진 하트 클릭
    retryClick: () -> Unit ,               //재요청
    removeHistory:(String)->Unit,          //검색 기록 삭제,
) {

    Log.d("HomeScreen", "HomeScreen: ")
    Scaffold(modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)},
        topBar = {  //topBar, searchBar Component 호출
            SearchBarComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                searchText=searchText,
                searchTextChange=searchTextChange,
                searchList = searchhistory,
                onSearch = onSearch,
                removeHistory = removeHistory)

        }) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                error -> {
                    LoadErrorScreen(modifier = Modifier.align(Alignment.Center),
                        snackbarHostState = snackbarHostState,
                        retryClick = retryClick)
                }
                // 로딩 중이라면  empty보다 먼저 와야 함
                loading -> {
                    LoadingScreen(modifier = Modifier.align(Alignment.Center))
                }
                //데이터가 비어있다면
                empty -> {
                    EmptyResultScreen(modifier = Modifier.padding(padding),text="검색 결과가 없습니다")
                }// 데이터가 있으면 결과 표시
                else -> {
                    QueryResultScreen(
                        imageList = imageList,
                        onClick = onClick,
                        likeManageMap = likeManageMap,
                        likesCountMap=likesCountMap,
                        snackbarHostState=snackbarHostState
                    )
                }
            }
        }
    }
}










