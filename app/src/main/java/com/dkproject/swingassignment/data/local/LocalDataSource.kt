package com.dkproject.swingassignment.data.local

import com.dkproject.swingassignment.model.results
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LocalDataSource {
    //dataStore 에 최근 검색어 저장
    suspend fun setSearchHistory(sh:List<String>)

    //dataStore 에 저장된 최근 검색어 가져오기
    fun getAllSearchHistory():Flow<List<String>>
}

class LocalDataSourceImpl @Inject constructor(
    private val dataStore: searchDataStore
):LocalDataSource {

    override suspend fun setSearchHistory(sh: List<String>) {
        dataStore.setSearchHistory(sh)
    }

    override fun getAllSearchHistory(): Flow<List<String>> {
        return dataStore.getAllSearchHistory()
    }
}