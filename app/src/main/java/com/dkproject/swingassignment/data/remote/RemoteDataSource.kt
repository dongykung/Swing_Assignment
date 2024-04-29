package com.dkproject.swingassignment.data.remote

import android.util.Log
import com.dkproject.swingassignment.model.ImageDTO
import com.dkproject.swingassignment.model.UserLikeDTO
import com.dkproject.swingassignment.model.likeDTO
import com.dkproject.swingassignment.model.results
import com.dkproject.swingassignment.retrofit.SearchImageService
import javax.inject.Inject

interface RemoteDataSource {
    //서버 좋아요 요청 통신
    suspend fun likeImage(id: String): Result<likeDTO>

    //서버 좋아요 삭제 요청 통신
    suspend fun deletelikeImage(id: String): Result<likeDTO>

    //서버 쿼리 이미지 요청 통신
    suspend fun getQueryImages(query: String,page:Int,perPage:Int):Result<List<results>>

    //서버 랜덤 이미지 요청 - 초기 화면 랜덤 이미지 가져와서 보여줌
    suspend fun getRandomImages(count:Int):Result<List<results>>

    suspend fun getUsersLike(username:String,page:Int,perPage:Int):Result<List<UserLikeDTO>>
}

class RemoteDataSourceImpl @Inject constructor(
    private val imageService: SearchImageService
):RemoteDataSource {
    override suspend fun likeImage(id: String): Result<likeDTO> = runCatching {
        imageService.likeImage(id)
    }

    override suspend fun deletelikeImage(id: String): Result<likeDTO>  = runCatching{
        imageService.deletelikeImage(id)
    }

    override suspend fun getQueryImages(query: String,page:Int,perPage:Int): Result<List<results>> = runCatching {
        imageService.getQueryImage(query=query, page = page,perPage=perPage).results
    }

    override suspend fun getRandomImages(count: Int): Result<List<results>> = runCatching {
        imageService.getRandomImage(count=count)
    }

    override suspend fun getUsersLike(
        username: String,
        page: Int,
        perPage: Int
    ): Result<List<UserLikeDTO>> = runCatching {
        imageService.getUsersLike(username=username,page=page,perPage=perPage)
    }


}