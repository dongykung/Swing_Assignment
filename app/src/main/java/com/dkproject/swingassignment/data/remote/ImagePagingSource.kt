package com.dkproject.swingassignment.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dkproject.swingassignment.data.local.LocalDataSource
import com.dkproject.swingassignment.model.results
import com.dkproject.swingassignment.util.Constants
import kotlinx.coroutines.delay
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class ImagePagingSource @Inject constructor(
    private val query: String,
    private val remoteDataSource: RemoteDataSource,
    private val random:Boolean,
) : PagingSource<Int, results>() {
    override fun getRefreshKey(state: PagingState<Int, results>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, results> {
        try {
            val page: Int = params.key ?: Constants.START_PAGE_INDEX
            val loadSize = params.loadSize

            val data = if(random){
                remoteDataSource.getRandomImages(count=loadSize).getOrThrow()
            } else {
                remoteDataSource.getQueryImages(
                    query = query,
                    page = page,
                    perPage = loadSize
                ).getOrThrow()
            }

            Log.d("loadsize", loadSize.toString())
            Log.d("size", data.size.toString())


            val prevKey = if(page==Constants.START_PAGE_INDEX) null else page-1
            val nextKey = if(data.isEmpty()) null else page+(params.loadSize/Constants.LOAD_PAGE_SIZE)
            Log.d("prevkey = ", prevKey.toString())
            Log.d("nextkey = ", nextKey.toString())
            return LoadResult.Page(
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