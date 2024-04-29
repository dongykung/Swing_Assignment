package com.dkproject.swingassignment.retrofit

import com.dkproject.swingassignment.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class RetrofitInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request()
                .newBuilder()
                //인증 정보를 header 에 추가
                .addHeader("Authorization", BuildConfig.UNSPLASH_API_KEY)
                .build()
        )
    }
}


