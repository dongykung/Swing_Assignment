package com.dkproject.swingassignment.repository

import com.dkproject.swingassignment.data.local.LocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SearchHistoryRepository {
    //최근 검색어에 저장
    suspend fun setSearchHistory(sh:List<String>)

    //최근 검색한 단어 flow 로 가져오기
    fun getAllSearchHistory():Flow<List<String>>
}


class SearchHistoryRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
):SearchHistoryRepository {
    override suspend fun setSearchHistory(sh: List<String>) {
        localDataSource.setSearchHistory(sh)
    }

    override fun getAllSearchHistory(): Flow<List<String>> {
        return localDataSource.getAllSearchHistory()
    }
}