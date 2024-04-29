package com.dkproject.swingassignment.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dkproject.swingassignment.data.local.LocalDataSource
import com.dkproject.swingassignment.data.remote.ImagePagingSource
import com.dkproject.swingassignment.data.remote.RemoteDataSource
import com.dkproject.swingassignment.data.remote.UserLikePagingSource
import com.dkproject.swingassignment.model.UserLikeDTO
import com.dkproject.swingassignment.model.likeDTO
import com.dkproject.swingassignment.model.results
import com.dkproject.swingassignment.retrofit.SearchImageService
import com.dkproject.swingassignment.util.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ImageRepository {
//    //room 에서 모든 데이터 가졍괴
//    fun getAllItemsStream(): Flow<List<ImageEntity>>
//
//    //room 데이터 저장
//    suspend fun insertItem(image: ImageEntity)
//
//    //room 데이터 삭제
//    suspend fun deleteItem(image: ImageEntity)

    //서버 좋아요 통신
    suspend fun likeImage(id: String): Result<likeDTO>

    //서버 좋아요 삭제 통신
    suspend fun deletelikeImage(id: String): Result<likeDTO>

    //서버 쿼리 이미지 통신
    suspend fun getQueryImages(query: String): Result<Flow<PagingData<results>>>

    //서버 랜덤 이미지 가져오기 - init 화면
    suspend fun getRandomImages():Result<Flow<PagingData<results>>>

    //서버 유저 좋아요 리스트 이미지 가져오기
    suspend fun getUserLikeImages():Result<Flow<PagingData<UserLikeDTO>>>
}

class ImageRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : ImageRepository {
    override suspend fun likeImage(id: String): Result<likeDTO> = runCatching {
        return remoteDataSource.likeImage(id)
    }

    override suspend fun deletelikeImage(id: String): Result<likeDTO> = runCatching {
        return remoteDataSource.deletelikeImage(id)
    }

    override suspend fun getQueryImages(
        query: String
    ): Result<Flow<PagingData<results>>> = runCatching {
        Pager(
            config = PagingConfig(
                pageSize = Constants.LOAD_PAGE_SIZE,  //로드 이미지 개수
                initialLoadSize = 30,  //처음 로드할 이미지 개수
                enablePlaceholders = true,
                prefetchDistance = 1 // 가져온 페이지 데이터 last -1 거리에 도달하면 다음 데이터 요청
            ),
            pagingSourceFactory = { ImagePagingSource(
                query=query,
                remoteDataSource=remoteDataSource,
                random = false
            )}
        ).flow
    }

    override suspend fun getRandomImages(): Result<Flow<PagingData<results>>> = runCatching {
        Pager(
            config = PagingConfig(
                pageSize = Constants.LOAD_PAGE_SIZE,  //로드 이미지 개수
                initialLoadSize = 30,  //처음 로드할 이미지 개수
                enablePlaceholders = true,
                prefetchDistance = 1 // 가져온 페이지 데이터 last -1 거리에 도달하면 다음 데이터 요청
            ),
            pagingSourceFactory = { ImagePagingSource(
                query="",
                remoteDataSource=remoteDataSource,
                random = true
            )}
        ).flow
    }

    override suspend fun getUserLikeImages(): Result<Flow<PagingData<UserLikeDTO>>> = runCatching{
        Pager(
            config = PagingConfig(
                pageSize = Constants.LOAD_USER_LIKE,
                initialLoadSize = 10,
                enablePlaceholders = true,
                prefetchDistance = 0
            ),
            pagingSourceFactory = {
                UserLikePagingSource(remoteDataSource = remoteDataSource)
            }
        ).flow
    }

}