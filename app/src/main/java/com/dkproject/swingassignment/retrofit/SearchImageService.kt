package com.dkproject.swingassignment.retrofit

import com.dkproject.swingassignment.model.ImageDTO
import com.dkproject.swingassignment.model.UserLikeDTO
import com.dkproject.swingassignment.model.likeDTO
import com.dkproject.swingassignment.model.results
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchImageService {

    @GET("search/photos")
    suspend fun getQueryImage(
        @Query("query") query:String,
        @Query("page") page:Int,
        @Query("per_page")perPage:Int,
    ):ImageDTO

    @POST("photos/{id}/like")
    suspend fun likeImage(
        @Path("id")id:String
    ):likeDTO

    @DELETE("photos/{id}/like")
    suspend fun deletelikeImage(
        @Path("id")id:String
    ):likeDTO

    @GET("photos/random")
    suspend fun getRandomImage(
        @Query("count") count:Int
    ):List<results>

    @GET("users/{username}/likes")
    suspend fun getUsersLike(
        @Path("username") username:String,
        @Query("page")page:Int,
        @Query("per_page")perPage:Int,
    ):List<UserLikeDTO>


}