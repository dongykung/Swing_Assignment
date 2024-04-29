package com.dkproject.swingassignment.DI

import android.content.Context
import androidx.room.Room
import com.dkproject.swingassignment.repository.ImageRepository
import com.dkproject.swingassignment.repository.ImageRepositoryImpl
import com.dkproject.swingassignment.retrofit.SearchImageService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideGson(): Gson {
        return Gson()
    }
}