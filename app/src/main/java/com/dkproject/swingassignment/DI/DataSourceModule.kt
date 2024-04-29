package com.dkproject.swingassignment.DI

import com.dkproject.swingassignment.data.local.LocalDataSource
import com.dkproject.swingassignment.data.local.LocalDataSourceImpl
import com.dkproject.swingassignment.data.remote.RemoteDataSource
import com.dkproject.swingassignment.data.remote.RemoteDataSourceImpl
import com.dkproject.swingassignment.repository.ImageRepository
import com.dkproject.swingassignment.repository.ImageRepositoryImpl
import com.dkproject.swingassignment.repository.SearchHistoryRepository
import com.dkproject.swingassignment.repository.SearchHistoryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindLocalDataSource(sc:LocalDataSourceImpl):LocalDataSource

    @Binds
    abstract fun bindRemoteDataSource(sc:RemoteDataSourceImpl):RemoteDataSource

    @Binds
    abstract fun bindImageRepository(repo:ImageRepositoryImpl):ImageRepository

    @Binds
    abstract fun bindSearchHistroyRepository(repo:SearchHistoryRepositoryImpl):SearchHistoryRepository
}