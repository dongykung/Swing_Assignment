package com.dkproject.swingassignment.ui.screen.home

import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.dkproject.swingassignment.model.UserLikeDTO
import com.dkproject.swingassignment.model.results
import com.dkproject.swingassignment.repository.ImageRepository
import com.dkproject.swingassignment.repository.SearchHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val imageRepository: ImageRepository,  //hilt imageRepository 의존성 주입
    private val searchHistoryRepository: SearchHistoryRepository
) : ViewModel() {
    companion object {
        private const val TAG = "HomeViewModel"
        private const val TIMEOUT_MILLIS = 5_000L
    }

    //검색 결과 리스트, 좋아요 Room data
    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    // 검색 기록 StateFlow
    val searchHistoryState: StateFlow<searchHistoryUiState> =
        searchHistoryRepository.getAllSearchHistory().map {
            searchHistoryUiState(it.reversed())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = searchHistoryUiState()
        )

    init {
        //처음 진입 시 랜덤 이미지 가져오기 및 내가 좋아요 누른 데이터 가져오기
        initRandomImage()
        loadUsersLikeImages()
    }


    //서버 - 랜덤 이미지 페이징 함수 (처음 화면)
    fun initRandomImage() {
        viewModelScope.launch {
            val imageFlow: Flow<PagingData<results>>
            imageRepository.getRandomImages().onSuccess {
                imageFlow = it.map { pagingData ->
                    pagingData
                }.cachedIn(viewModelScope)
                _state.update { uiState -> uiState.copy(imageList = imageFlow) }
            }
        }
    }

    // 서버 - 내가 좋아요 누른 데이터 페이징 로드 함수
    fun loadUsersLikeImages() {
        Log.d("loadUsersLikeImages", "loadUsersLikeImages: ")
        viewModelScope.launch {
            val imageFlow: Flow<PagingData<UserLikeDTO>>
            imageRepository.getUserLikeImages().onSuccess {
                imageFlow = it.map { pagingData ->
                    pagingData.map { userLikeDTO ->
                        userLikeDTO
                    }
                }.cachedIn(viewModelScope)
                //상태 업데이트
                _state.update { st -> st.copy(userLikeList = imageFlow) }
            }
        }
    }

    //서버 - 검색어 결과 -> 페이징 함수
    fun query(query: String) {
        viewModelScope.launch {
            val imageFlow: Flow<PagingData<results>>
            imageRepository.getQueryImages(query).onSuccess {
                imageFlow = it.map { pagingData ->
                    pagingData
                }.cachedIn(viewModelScope)
                //상태 업데이트
                _state.update { uiState -> uiState.copy(imageList = imageFlow) }
            }
        }
    }

    //피드 하트 클릭
    fun feadHeartClick(
        result: results,
        snackbarHostState: SnackbarHostState
    ) {
        viewModelScope.launch {
            // 만약 좋아요를 누르지 않은 상태였다면 없다면 서버에 좋아요 요청
            if (!result.liked_by_user) {
                Log.d(TAG, "Append_Like")
                imageRepository.likeImage(result.id).onSuccess {
                    //서버와 통신 성공 했다면
                    //좋아요, 좋아요 수 관리 map 데이터 설정 [서버에 데이터 재요청을 안하고 ui 업데이트 하기 위함]
                    val newLikeMap = state.value.likeManageMap.toMutableMap()
                    newLikeMap[result.id] = true
                    val newLikesCountMap = state.value.likesCountManageMap.toMutableMap()
                    newLikesCountMap[result.id] = result.likes + 1
                    _state.update {
                        it.copy(
                            likeManageMap = newLikeMap,
                            likesCountManageMap = newLikesCountMap
                        )
                    }
                    loadUsersLikeImages()
                }.onFailure {
                    //서버와 통신 실패 시 스낵바 표시 및 재요청 받음
                    val snackbar = snackbarHostState.showSnackbar(
                        message = "좋아요 요청에 실패하였습니다.",
                        duration = SnackbarDuration.Short,
                        actionLabel = "재시도"
                    )
                    when(snackbar){
                        SnackbarResult.Dismissed -> {}
                        SnackbarResult.ActionPerformed -> {feadHeartClick(result=result, snackbarHostState = snackbarHostState)}
                    }
                }
                // 그게 아니라면 서버에 좋아요 삭제 요청
            } else {
                imageRepository.deletelikeImage(result.id).onSuccess {
                    Log.d(TAG, "Delete_Like")
                    //서버와 통신 성공 했다면
                    //좋아요, 좋아요 수 관리 map 데이터 설정 [서버에 데이터 재요청을 안하고 ui 업데이트 하기 위함]
                    val newLikeHashMap = state.value.likeManageMap.toMutableMap()
                    newLikeHashMap[result.id] = false
                    val newLikesCountMap = state.value.likesCountManageMap.toMutableMap()
                    newLikesCountMap[result.id] = result.likes - 1
                    //상태 업데이트
                    _state.update {
                        it.copy(
                            likeManageMap = newLikeHashMap,
                            likesCountManageMap = newLikesCountMap
                        )
                    }
                }.onFailure {
                    //서버와 통신 실패 시 스낵바 표시 및 재요청 받음
                    val snackbar = snackbarHostState.showSnackbar(
                        message = "좋아요 삭제 요청에 실패하였습니다.",
                        duration = SnackbarDuration.Short,
                        actionLabel = "재시도"
                    )
                    when(snackbar){
                        SnackbarResult.Dismissed -> {}
                        SnackbarResult.ActionPerformed -> {feadHeartClick(result=result, snackbarHostState = snackbarHostState)}
                    }
                }
            }
        }
    }


    //즐겨찾기에서 좋아요 삭제
    fun updateDeleteLike(data: UserLikeDTO,snackbarHostState: SnackbarHostState) {
        viewModelScope.launch {
            imageRepository.deletelikeImage(data.id).onSuccess {
                //서버와 통신이 성공했다면
                //좋아요 삭제기 때문에 좋아요 관리 Map 에 id = false  저장, 좋아요 카운터 수 -1 [서버에 데이터 재요청을 안하고 ui 업데이트 하기 위함]
                val newLikeMap = state.value.likeManageMap.toMutableMap()
                newLikeMap[data.id] = false
                val newLikesCountMap = state.value.likesCountManageMap.toMutableMap()
                newLikesCountMap[data.id] = data.likes - 1
                //상태 업데이트
                _state.update {
                    it.copy(
                        likeManageMap = newLikeMap,
                        likesCountManageMap = newLikesCountMap
                    )
                }
            }.onFailure {
                //좋아요 삭제 통신에 실패하였다면 스낵바 표시 및 재시도 버튼
                val snackbar = snackbarHostState.showSnackbar(
                    message = "좋아요 삭제 요청에 실패하였습니다.",
                    duration = SnackbarDuration.Short,
                    actionLabel = "재시도"
                )
                when(snackbar){
                    SnackbarResult.Dismissed -> {}
                    SnackbarResult.ActionPerformed -> {updateDeleteLike(data=data, snackbarHostState = snackbarHostState)}
                }
            }
        }
    }


    //검색 기록 저장
    fun setSearchHistory(query: String) {
        val newList = searchHistoryState.value.searchHistoryList.toMutableList()
        viewModelScope.launch {
            if (!newList.contains(query)) {
                newList.add(query)
                searchHistoryRepository.setSearchHistory(newList)
            } else {
                newList.remove(query)
                newList.add(query)
                searchHistoryRepository.setSearchHistory(newList)
            }
        }
    }

    //검색 기록 삭제
    fun deleteSearchHistory(query: String) {
        val newList = searchHistoryState.value.searchHistoryList.toMutableList()
        viewModelScope.launch {
            if (newList.contains(query)) {
                newList.remove(query)
                searchHistoryRepository.setSearchHistory(newList)
            }
        }
    }


}

data class HomeUiState(
    val imageList: Flow<PagingData<results>> = emptyFlow(), //페이징 데이터
    val userLikeList: Flow<PagingData<UserLikeDTO>> = emptyFlow(), //페이징 데이터
    val likeManageMap: Map<String, Boolean> = emptyMap(),  //좋아요 관리 맵
    val likesCountManageMap: Map<String, Long> = emptyMap()  //좋아요 수 관리 맵
)

data class searchHistoryUiState(
    //검색 기록 리스트
    val searchHistoryList: List<String> = emptyList()
)


