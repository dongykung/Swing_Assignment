package com.dkproject.swingassignment.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.io.Serial


//서버 통신 데이터
data class ImageDTO(
    val total:Long,
    val total_pages:Long,
    val results:List<results>
)


data class results(
    val id:String,
    val slug:String,
    val urls:Urls,
    var likes:Long,
    var liked_by_user:Boolean,
    val description:String?,
)




data class Urls(
    val raw:String,
    val full:String,
    val regular:String,
    val small:String,
    val thumb:String,
)

