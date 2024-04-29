package com.dkproject.swingassignment.model

data class UserLikeDTO(
    val id:String,
    val slug:String,
    val liked_by_user:Boolean,
    val likes:Long,
    val description:String?,
    val urls: Urls,
)