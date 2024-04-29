package com.dkproject.swingassignment.DI

import com.dkproject.swingassignment.retrofit.RetrofitInterceptor
import com.dkproject.swingassignment.retrofit.SearchImageService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val BASE_URL = "https://api.unsplash.com/"

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    fun provideOkhttpClient(retrofitInterceptor: RetrofitInterceptor):OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(retrofitInterceptor)
            .build()
    }

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient):Retrofit{
        val converterFactory =json.asConverterFactory("application/json".toMediaType())
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideImageService(retrofit: Retrofit):SearchImageService{
        return retrofit.create(SearchImageService::class.java)
    }
}