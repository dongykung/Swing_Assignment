package com.dkproject.swingassignment.data.remote

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dkproject.swingassignment.model.UserLikeDTO
import com.dkproject.swingassignment.util.Constants
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class UserLikePagingSource @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : PagingSource<Int, UserLikeDTO>() {
    override fun getRefreshKey(state: PagingState<Int, UserLikeDTO>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserLikeDTO> {
        try {
            val page: Int = params.key ?: Constants.START_PAGE_INDEX
            val loadSize = params.loadSize

            val data = remoteDataSource.getUsersLike(
                username = Constants.YOUSER_NAME,
                page = page,
                perPage = loadSize
            ).getOrThrow()

            Log.d("userloadsize", loadSize.toString())
            Log.d("usersize", data.size.toString())

            val prevKey = if(page==Constants.START_PAGE_INDEX) null else page-1
            val nextKey = if(data.isEmpty()) null else page+(params.loadSize/Constants.LOAD_USER_LIKE)
            Log.d("prevkey = ", prevKey.toString())
            Log.d("nextkey = ", nextKey.toString())
            return if(data.isEmpty()){
                LoadResult.Page(
                    data = emptyList(),
                    prevKey=null,
                    nextKey=null
                )
            } else
                LoadResult.Page(
                    data = data,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
        } catch (e: IOException) {
            Log.d("io", e.toString())
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            Log.d("ios", e.toString())
            return LoadResult.Error(e)
        }
    }
}